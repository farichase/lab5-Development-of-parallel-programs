package lab5;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.Map;

public class CacheActor extends AbstractActor{
    private Map<String, Float> cache = new HashMap<>();
    @Override
    public Receive createReceive(){
        return ReceiveBuilder.create(
                
        ).build();
    }
    static Props props() {
        return Props.create(CacheActor.class, "");
    }

}
