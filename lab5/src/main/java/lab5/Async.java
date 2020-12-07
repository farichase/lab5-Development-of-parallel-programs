package lab5;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.Query;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import javafx.util.Pair;

public class Async {
    private ActorRef cacheActor;
    private int PARALLELIZM_PARAMETR = 1;
    public Async(ActorSystem system){
        this.cacheActor = system.actorOf(CacheActor.props(), "cache");
    }
    public static Flow<> createRouteFlow(ActorMaterializer materializer){
        return Flow.of(HttpRequest.class).map( request -> {
            Query query = request.getUri().query();
            String url = query.get("testUrl").get();
            int count = Integer.parseInt(query.get("count").get());
            return new Pair<>(url, count);
        }).mapAsync(




        );
    }
}
