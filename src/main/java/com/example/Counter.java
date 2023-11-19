package com.example;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class Counter extends AbstractActor {

    static public Props props() {
        return Props.create(Counter.class, Counter::new);
    }

    static public class IncrementMessage {}

    private int count = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(IncrementMessage.class, this::onIncrementMessage)
                .build();
    }

    private void onIncrementMessage(IncrementMessage message) {
        count++;
        System.out.println("Counter incremented to: " + count);
    }
}
