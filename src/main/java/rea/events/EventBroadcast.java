package rea.events;

import java.util.HashSet;
import java.util.Set;

public class EventBroadcast<T extends UpdateEvent> {

    private final Set<UpdateListener<T>> listeners;

    public EventBroadcast() {
        listeners = new HashSet<>();
    }

    public Set<UpdateListener<T>> getListeners() {
        return listeners;
    }

    public void addListener(UpdateListener<T> listener) {
        listeners.add(listener);
    }

    public void removeListener(UpdateListener<T> listener) {
        listeners.remove(listener);
    }

    public void broadcast(T event) {
        Set<UpdateListener<T>> listenersToRemove = new HashSet<>();
        for (UpdateListener<T> listener : listeners) {
            try {
                listener.onUpdate(event);
            } catch (Exception e) {
                listenersToRemove.add(listener);
            }
        }
        listeners.removeAll(listenersToRemove);
    }

}
