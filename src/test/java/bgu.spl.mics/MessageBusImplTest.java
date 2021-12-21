package bgu.spl.mics;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.services.ExampleMessageSenderService;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;


public class MessageBusImplTest {

    private static MessageBusImpl mbs;
    private static MicroService mes;


    @After
    public void tearDown() throws Exception {
    }

    @Before
    public void setUp() throws Exception{
        mbs = new MessageBusImpl();
    }

    @Test
    public void subscribeEvent() {
        ExampleEvent e = new ExampleEvent("Amit");
        String[] str = {"event"};
        mes = new ExampleMessageSenderService("Daniel", str);
        mbs.subscribeEvent(e.getClass(),mes);
        assertTrue(mbs.isSubscribedToEvent(e.getClass(), mes));
    }

    @Test
    public void subscribeBroadcast() {
        Broadcast b = new ExampleBroadcast("Test");
        String[] str = {"broadcast"};
        mes = new ExampleMessageSenderService("Amit", str);
        mbs.subscribeBroadcast(b.getClass(), mes);
        assertTrue(mbs.isSubscribedToBroadcast(b.getClass(), mes));
    }

    @Test
    public void complete() {
        Event<String> e = new ExampleEvent("Amit");
        mbs.complete(e, "Daniel");
        assertTrue(mbs.isComplete(e, "Daniel"));
    }

    @Test
    public void sendBroadcast() {
        Broadcast b = new ExampleBroadcast("Hi");
        mbs.sendBroadcast(b);
        assertTrue(mbs.isBroadcastSent(b));


    }

    @Test
    public void sendEvent() {
        Event<String> e = new ExampleEvent("Amit");
        mbs.sendEvent(e);
        assertTrue(mbs.isEventSent(e));
    }

    @Test
    public void register() {
        mbs.register(mes);
        assertTrue(mbs.isRegisterd(mes));
    }

    @Test
    public void unregister() {
        mbs.unregister(mes);
        assertTrue(mbs.isUnregisterd(mes));
    }

    @Test
    public void awaitMessage() {
    }
}