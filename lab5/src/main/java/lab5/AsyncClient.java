package lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import javafx.util.Pair;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class AsyncClient {
    private ActorRef cacheActor;
    private static int PARALLELIZM = 1;
    private static long ZERO = 0L;
    private static Duration TIMEOUT = Duration.ofSeconds(5);
    private String URL = "testUrl";
    private String COUNT = "count";
    public AsyncClient(ActorSystem system){
        this.cacheActor = system.actorOf(CacheActor.props(), "cache");
    }

    private static Sink<Pair<String, Integer>, CompletionStage<Long>> testSink(){

    }

    final Flow<HttpRequest, HttpResponse, NotUsed> createRouteFlow(ActorMaterializer materializer){
        return Flow.of(HttpRequest.class)
            .map( request -> {
                Query query = request.getUri().query();
                String url = query.get(URL).get();
                int count = Integer.parseInt(query.get(COUNT).get());
                return new Pair<>(url, count);
        }).mapAsync(PARALLELIZM, (pair) ->
             Patterns.ask(this.cacheActor, pair, TIMEOUT).thenCompose(res -> {
                if ((Long)res >= ZERO) {
                    return CompletableFuture.completedFuture(new Pair<>(pair.getKey(), (Long)res));
                }
                Flow<Pair<String, Integer>, Long, NotUsed> flow = Flow.<Pair<String, Integer>>create()
                        .mapConcat(p ->
                             new ArrayList<>(Collections.nCopies(p.getValue(), p.getKey()))
                        )
                        .mapAsync(pair.getValue(), (req) -> {
                            long startTime = System.currentTimeMillis();
                            asyncHttpClient().prepareGet(req).execute();
                            long stopTime = System.currentTimeMillis();
                            return CompletableFuture.completedFuture(stopTime - startTime);
                        });
                return Source.single(pair).via(flow)
                        .toMat(testSink(), Keep.right())
                        .run(materializer)
                        .thenApply(sum ->
                            new Pair<>(pair.getKey(), sum / pair.getValue())
                        );
            }))
            .map((Pair<String, Long> p) -> {
                this.cacheActor.tell(p, ActorRef.noSender());
                return HttpResponse.create().withEntity(HttpEntities.create(p.getValue().toString()));
            });
    }
}
