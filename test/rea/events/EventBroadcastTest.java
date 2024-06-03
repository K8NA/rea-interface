package rea.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the EventBroadcast class.
 */
class EventBroadcastTest {

    EventBroadcast<MockEvent> eventBroadcast;
    @BeforeEach
    void setUp() {
        eventBroadcast = new EventBroadcast<MockEvent>();

    }

    /**
     * By default, there are no listeners.
     */
    @Test
    void getListeners() {
        assertEquals(0, eventBroadcast.getListeners().size());
    }

    /**
     * Add a listener to the broadcast and check that it is in the list of listeners.
     */
    @Test
    void addListener() {
        var listener = new MockListener<MockEvent>();
        eventBroadcast.addListener(listener);

        assertEquals(1, eventBroadcast.getListeners().size());
        assertTrue(eventBroadcast.getListeners().contains(listener));
    }

    /**
     * Add a listener and then remove it. The list of listeners should be empty.
     */
    @Test
    void removeListener() {
        var listener = new MockListener<MockEvent>();
        eventBroadcast.addListener(listener);

        assertEquals(1, eventBroadcast.getListeners().size());
        assertTrue(eventBroadcast.getListeners().contains(listener));

        eventBroadcast.removeListener(listener);

        assertEquals(0, eventBroadcast.getListeners().size());
    }


    /**
     * Broadcast an event to a single listener and check that the listener is called.
     */
    @Test
    void broadcast_single_listener() {
        var listener = new MockListener<MockEvent>();
        eventBroadcast.addListener(listener);

        assertEquals(0, listener.getCount());

        eventBroadcast.broadcast(new MockEvent());

        assertEquals(1, listener.getCount());
    }

    /**
     * Broadcast an event to multiple listeners and check that all listeners are called.
     */
    @Test
    void broadcast_multiple_listeners() {
        var listeners = new HashSet<MockListener<MockEvent>>();

        for(int i=0; i<3; i++) {
            var listener = new MockListener<MockEvent>();
            eventBroadcast.addListener(listener);
            listeners.add(listener);

            assertEquals(0, listener.getCount());
        }


        eventBroadcast.broadcast(new MockEvent());

        for(var listener : listeners)
            assertEquals(1, listener.getCount());
    }


    /**
     * Broadcast an event to a listener that throws an exception. The listener should be removed.
     */
    @Test
    void broadcast_to_lost_listener() {
       var listener = new MockListener<MockEvent>();

       eventBroadcast.addListener(listener);

       assertEquals(0, listener.getCount());

       eventBroadcast.broadcast(new MockEvent());

       assertEquals(1, listener.getCount());

       assertEquals(1, eventBroadcast.getListeners().size());

       listener.active = false;

       eventBroadcast.broadcast(new MockEvent());

       assertEquals(0, eventBroadcast.getListeners().size());

    }


}