package com.example;

//import akka.actor.typed.Behavior;
//import akka.actor.typed.javadsl.AbstractBehavior;
//import akka.actor.typed.javadsl.ActorContext;
//import akka.actor.typed.javadsl.Behaviors;
//import akka.actor.typed.javadsl.Receive;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import java.io.IOException;

class Main {

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create();

        ActorRef counterRef = system.actorOf(Props.create(Counter.class), "counter");

//        ActorRef actorARef = system.actorOf(Props.create(ActorA.class));
        ActorRef actorARef = system.actorOf(ActorA.props(counterRef), "actorA");


        actorARef.tell(new MessageA("Starting"),actorARef);

        // Send a different type of message for testing
        actorARef.tell("This is a test message", actorARef);

        actorARef.tell(24, actorARef); // int
        actorARef.tell(6.39, actorARef); // double
        actorARef.tell(true, actorARef); // boolean

        for (int i = 0; i < 20; i++) {
            ActorRef actorCounterRef = system.actorOf(ActorA.props(counterRef), "actorA" + i);
            actorCounterRef.tell(new Counter.IncrementMessage(), ActorRef.noSender());
        }

        try {
            System.out.println("Press ENTER twice to end program.");
            System.in.read();
        }
        catch (IOException ignored) { }
        finally {
            system.terminate();
            System.out.println("Terminated.");
        }
    }

}
