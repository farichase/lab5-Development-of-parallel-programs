package lab5;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.Query;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import javafx.util.Pair;

public class Async {
    private ActorRef cacheActor;
    private static int PARALLELIZM = 1;
    private static int TIMEOUT = 
    public Async(ActorSystem system){
        this.cacheActor = system.actorOf(CacheActor.props(), "cache");
    }
    public static Flow<> createRouteFlow(ActorMaterializer materializer){
        return Flow.of(HttpRequest.class).map( request -> {
            Query query = request.getUri().query();
            String url = query.get("testUrl").get();
            int count = Integer.parseInt(query.get("count").get());
            return new Pair<>(url, count);
        }).mapAsync(PARALLELIZM, (Pair<String, Integer> pair) -> {
            return Patterns.ask(this.cacheActor, pair, )


        });
    }
}
