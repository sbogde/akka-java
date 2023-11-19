package com.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class Worker extends AbstractActor {

    private boolean isPrime(long number) {
        if (number <= 1) return false;
        for (long i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Long.class, number -> {
                    if (isPrime(number)) {
                        getSender().tell("The number " + number + " is a prime number.", ActorRef.noSender());
                    }
                })
                .build();
    }
}
