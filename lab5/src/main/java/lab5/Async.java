package lab5;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

public class Async {
    private ActorRef cacheActor;
    public Async(ActorSystem system){
        this.cacheActor = system.actorOf(CacheActor.props(), "cache");
    }
    public static Flow<> createRouteFlow(ActorMaterializer materializer){
        return Flow.of(HttpRequest.class).map( request -> {
                    return new Pair(request)
                }
        );
    }
}
