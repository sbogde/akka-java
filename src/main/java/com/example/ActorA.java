package com.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;

import java.util.Random;

public class ActorA extends AbstractActor {

    private final Random random = new Random();
    private ActorRef actorBRef;

    public ActorA(ActorRef actorBRef) {
        this.actorBRef = actorBRef;
    }

    public static Props props(ActorRef actorBRef) {
        return Props.create(ActorA.class, () -> new ActorA(actorBRef));
    }

    @Override
    public void preStart() {
        getContext().watch(actorBRef);
        generateMessages();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActorB.Stop.class, this::onStop)
                .match(Terminated.class, t -> actorBRef == t.getActor(), t -> createNewActorB())
                .build();
    }

    private void onStop(ActorB.Stop stop) {
        actorBRef.tell(akka.actor.PoisonPill.getInstance(), ActorRef.noSender());
    }

    private void createNewActorB() {
        // Stop watching the old ActorB instance
        getContext().unwatch(actorBRef);

        // Create a new ActorB instance and update the actorBRef
        actorBRef = getContext().actorOf(ActorB.props(), "actorB" + System.currentTimeMillis());

        // Start watching the new ActorB instance
        getContext().watch(actorBRef);
    }


    @Override
    public void preRestart(Throwable reason, scala.Option<Object> message) {
        // No call to postStop
    }

    @Override
    public void postRestart(Throwable reason) {
        // No call to preStart
    }

    public void generateMessages() {
        for (int i = 0; i < 100; i++) {
            int randomNumber = 1 + random.nextInt(5);
            actorBRef.tell(randomNumber, getSelf());
        }
    }
}
