package lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import javafx.util.Pair;
import org.asynchttpclient.Dsl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class Async {
    private ActorRef cacheActor;
    private static int PARALLELIZM = 3;
    private static Duration TIMEOUT = Duration.ofSeconds(5);
    public Async(ActorSystem system){
        this.cacheActor = system.actorOf(CacheActor.props(), "cache");
    }
    final Flow<HttpRequest, HttpResponse, NotUsed> createRouteFlow(ActorMaterializer materializer){
        return Flow.of(HttpRequest.class).map( request -> {
            Query query = request.getUri().query();
            String url = query.get("testUrl").get();
            int count = Integer.parseInt(query.get("count").get());
            return new Pair<>(url, count);
        }).mapAsync(PARALLELIZM, pair -> {
            return Patterns.ask(this.cacheActor, pair, TIMEOUT).thenCompose(res -> {
                if ((long)res >= 0) {
                    return CompletableFuture.completedFuture(new Pair<>(pair.getKey(), (long)res));
                }
                Flow<Pair<String, Integer>, Object, NotUsed> flow = Flow.<Pair<String, Integer>>create()
                        .mapConcat(p -> new ArrayList<>(Collections.nCopies(p.getValue(), p.getKey())))
                        .mapAsync(1, req -> {
                            Long startTime = System.currentTimeMillis();
                            Dsl.asyncHttpClient().prepareGet(req).execute();
                            Long stopTime = System.currentTimeMillis();
                            return CompletableFuture.completedFuture(stopTime - startTime);
                        });
                return flow.toMap)
            })



        });
    }
}
