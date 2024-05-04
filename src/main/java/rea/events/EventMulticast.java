package rea.events;

import rea.components.Character;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventMulticast<T extends UpdateEvent> {

    private Map<Character, UpdateListener<T>> listeners;

    public EventMulticast() {
        listeners = new HashMap<>();
    }

    public Map<Character, UpdateListener<T>> getListeners() {
        return listeners;
    }

    public void addListener(Character character, UpdateListener<T> listener) {
        if (listeners == null) {
            listeners = new HashMap<>();
        }
        listeners.put(character, listener);
    }

    public void removeListener(Character character) {
        if (listeners != null) {
            listeners.remove(character);
        }
    }

    public void unicast(Character character, T event) {
        UpdateListener<T> listener = listeners.get(character);
        if (listener != null) {
            try {
                listener.onUpdate(event);
            } catch (Exception e) {
                removeListener(character);
            }
        }
    }


    public void multicast(Set<Character> characterSet, T event) {
        for (Character character : characterSet) {
            UpdateListener<T> listener = listeners.get(character);
            if (listener != null) {
                try {
                    listener.onUpdate(event);
                } catch (Exception e) {
                    removeListener(character);
                }
            }
        }
    }

}
