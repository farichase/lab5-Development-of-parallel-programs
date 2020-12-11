package lab5;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor{
    private Map<String, Long> cache = new HashMap<>();
    public CacheActor(){}
    @Override
    public Receive createReceive(){
        return ReceiveBuilder.create()
                .match(String.class, msg -> {
                    getSender().tell(cache.getOrDefault(msg, (long)-1), ActorRef.noSender());
                })
                .match(Message.class, msg -> {
                    cache.put(msg.getUrl(), msg.getTime());
                })
                .build();
    }
    static Props props() {
        return Props.create(CacheActor.class, "");
    }

}
