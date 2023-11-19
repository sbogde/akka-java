package com.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;


public class ActorA extends AbstractActor {

    public static Props props() {
        return Props.create(ActorA.class, ActorA::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MessageA.class, this::onMessageA)
                .match(MessageB.class, this::onMessageB)
                .match(Integer.class, this::onIntegerMessage)
                .match(Double.class, this::onDoubleMessage)
                .match(Byte.class, this::onByteMessage)
                .match(Short.class, this::onShortMessage)
                .match(Long.class, this::onLongMessage)
                .match(Float.class, this::onFloatMessage)
                .match(Boolean.class, this::onBooleanMessage)
                .match(Character.class, this::onCharacterMessage)
                .matchAny(this::onAnyMessage) 
                .build();
    }

    private void onCharacterMessage(Character character) {
        System.out.println("Received a Character: " + character);
    }

    private void onBooleanMessage(Boolean aBoolean) {
        System.out.println("Received a Boolean: " + aBoolean);
    }

    private void onFloatMessage(Float aFloat) {
        System.out.println("Received a Float: " + aFloat);
    }

    private void onLongMessage(Long aLong) {
        System.out.println("Received a Long: " + aLong);
    }
    private void onShortMessage(Short aShort) {
        System.out.println("Received a Short: " + aShort);
    }
    private void onByteMessage(Byte aByte) {
        System.out.println("Received a Byte: " + aByte);
    }

    private void onDoubleMessage(Double aDouble) {
        System.out.println("Received a Double: " + aDouble);
    }

    private void onIntegerMessage(Integer integer) {
        System.out.println("Received an Integer: " + integer);
    }

    private void onAnyMessage(Object o) {
        System.out.println("Received an unhandled message: " + o);
    }

    private void onMessageA(MessageA msg) {
        System.out.println("Actor A received Message A : "+ msg.text + " from " + getSender());
        if(msg.text.equalsIgnoreCase("Goodbye!")) {
          getContext().getSystem().terminate();
        }
        else {
            ActorRef actorBRef = getContext().getSystem().actorOf(Props.create(ActorB.class));
            actorBRef.tell(new MessageA("Hello!"), getSelf());
        }
        for (int i=0; i<10; i++){
            System.out.println("Actor A doing work "+i);
        }
    }

    private void onMessageB(MessageB msg) {
        System.out.println("Actor A received Message B : "+ msg.number + " from " + getSender());
        getSender().tell(new MessageB(999),getSelf());
        for (int i=0; i<10; i++){
            System.out.println("Actor A doing more work "+i);
        }

    }
}