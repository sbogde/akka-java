package com.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.RoundRobinGroup;

import java.util.ArrayList;
import java.util.List;

public class Supervisor extends AbstractActor {

    private final ActorRef workerRouter;

    public Supervisor() {
        List<String> workerPaths = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ActorRef worker = getContext().actorOf(Props.create(Worker.class), "worker" + i);
            getContext().watch(worker);
            workerPaths.add(worker.path().toStringWithoutAddress());
        }
        workerRouter = getContext().actorOf(new RoundRobinGroup(workerPaths).props());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Long.class, number -> workerRouter.forward(number, getContext()))
                .build();
    }
}
