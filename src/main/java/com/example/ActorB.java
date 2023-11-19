package com.example;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class ActorB extends AbstractActor {

    public static Props props() {
        return Props.create(ActorB.class, ActorB::new);
    }

    public ActorB() {
        getContext().setReceiveTimeout(Duration.create(2, TimeUnit.SECONDS));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, this::onIntegerMessage)
                .match(ReceiveTimeout.class, x -> getContext().getParent().tell(new Stop(), getSelf()))
                .build();
    }

    private void onIntegerMessage(Integer num) {
        try {
            System.out.println("ActorB processing for " + num + " seconds");
            Thread.sleep(num * 1000L);
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }
    }

    public static class Stop {}
}
