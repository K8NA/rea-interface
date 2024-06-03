package rea.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;
import rea.components.MockAvatar;
import rea.components.Character;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class EventMulticastTest extends TestData {
    EventMulticast<MockEvent> eventMulticast;
    MockPool<Character> characters;

    @BeforeEach
    void setUp() {
        var avatars = MockAvatar.values();
        characters = new MockPool<>(i -> new Character("Character"+i, avatars[i]), avatars.length);

        eventMulticast = new EventMulticast<>();
    }

    @Test
    void getListeners() {
        assertEquals(0, eventMulticast.getListeners().size());
    }

    @Test
    void addListener() {

        for(Character character : characters) {
            var listener = new MockListener<MockEvent>();
            eventMulticast.addListener(character, listener);
        }

        assertEquals(characters.size(), eventMulticast.getListeners().size());
    }

    @Test
    void removeListener() {

        for(Character character : characters) {
            var listener = new MockListener<MockEvent>();

            eventMulticast.addListener(character, listener);

        }

        var listeners = eventMulticast.getListeners();
        var count = listeners.size();

        for(Character character : characters) {

            eventMulticast.removeListener(character);
            assertEquals(--count, eventMulticast.getListeners().size());
        }

    }


    @Test
    void unicast() {

        for(Character character : characters) {
            MockListener<MockEvent> listener = new MockListener<>();

            eventMulticast.addListener(character, listener);

            assertEquals(0, listener.getCount());
        }

        var listeners = eventMulticast.getListeners();
        var event = new MockEvent();
        var first = characters.getFirst();

        eventMulticast.unicast(first, event);

        for(var player : characters) {
            var expected = player == first ? 1 : 0;
            var listener = (MockListener<MockEvent>) listeners.get(player);

            assertEquals(expected, listener.getCount());
        }
    }

    @Test
    void unicast_to_lost_listener() {

        for(Character character : characters) {
            var listener = new MockListener<MockEvent>();

            eventMulticast.addListener(character, listener);
        }

        var listeners = eventMulticast.getListeners();

        int count = listeners.size();
        for(Character character : characters) {
            var event = new MockEvent();
            var listener = (MockListener<MockEvent>) listeners.get(character);

            listener.active = false;

            eventMulticast.unicast(character, event);

            count--;

            assertEquals(count, eventMulticast.getListeners().size());
        }
    }


    @Test
    void multicast() {
        List<MockListener<MockEvent>> listeners = new ArrayList<>();
        Set<Character> characterSet = new HashSet<>(characters);

        for(Character character : characters) {
            MockListener<MockEvent> listener = new MockListener<>();

            listeners.add(listener);

            eventMulticast.addListener(character, listener);

            assertEquals(0, listener.getCount());
        }

        IntStream.range(1,10).forEach( count -> {
            MockEvent event = new MockEvent();

            eventMulticast.multicast(characterSet, event);

            for (MockListener<MockEvent> listener : listeners) {

                assertEquals(count, listener.getCount());
            }
        });

    }

    @Test
    void multicast_to_lost_listener() {

        for(Character character : characters) {
            var listener = new MockListener<MockEvent>();

            eventMulticast.addListener(character, listener);
        }

        var listeners = eventMulticast.getListeners();

        int count = listeners.size();
        for(Character character : characters) {
            var event = new MockEvent();

            ((MockListener<MockEvent>) listeners.get(character)).active = false;

            eventMulticast.multicast(characters.asSet(), event);

            count--;

            assertEquals(count, eventMulticast.getListeners().size());
        }
    }

}