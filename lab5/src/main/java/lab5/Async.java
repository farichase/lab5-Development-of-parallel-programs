package lab5;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Async {
    private ActorRef cacheActor;
    public Async(ActorSystem system){
        this.cacheActor = system.actorOf(CacheActor.props(), "cache");

    }
}
