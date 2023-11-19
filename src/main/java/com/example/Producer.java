package com.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.util.Random;

public class Producer extends AbstractActor {

    private final Random random = new Random();
    private final ActorRef supervisor;
    private int count = 0;

    public Producer(ActorRef supervisor) {
        this.supervisor = supervisor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("start", s -> generateNumbers())
                .match(String.class, this::printMessage)
                .build();
    }

    private void generateNumbers() {
        for (int i = 0; i < 1000; i++) {
            long number = 10000L + (long) (random.nextDouble() * (90000L));
            supervisor.tell(number, getSelf());
            count++;
            if (count == 1000) {
                getContext().getSystem().terminate();
            }
        }
    }


    private void printMessage(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("PrimeNumberChecker");
        ActorRef supervisor = system.actorOf(Props.create(Supervisor.class), "supervisor");
        ActorRef producer = system.actorOf(Props.create(Producer.class, supervisor), "producer");

        producer.tell("start", ActorRef.noSender());
    }
}

