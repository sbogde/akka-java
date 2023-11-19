package com.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("actor-demo");
        final Random random = new Random();

        // Create the initial instance of ActorB
        ActorRef actorBRef = system.actorOf(Props.create(ActorB.class), "actorB");

        // Create ActorA and pass the reference to ActorB
        ActorRef actorARef = system.actorOf(ActorA.props(actorBRef), "actorA");


        // Generate random numbers and send to ActorB
        for (int i = 0; i < 100; i++) {
            int randomNumber = 1 + random.nextInt(5); // Random number between 1 and 5
            actorARef.tell(randomNumber, ActorRef.noSender());
        }

        // Add termination logic if needed
         system.terminate();
    }
}
