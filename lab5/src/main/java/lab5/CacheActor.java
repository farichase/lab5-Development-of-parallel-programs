package lab5;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class CacheActor extends AbstractActor{
    @Override
    public Receive createReceive(){
        return ReceiveBuilder.create().build();
    }
    static Props props() {
        return Props.create(CacheActor.class, "");
    }

}
