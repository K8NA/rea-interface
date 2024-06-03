package rea.events;

import java.util.ArrayList;
import java.util.List;

/**
 * A mock listener for testing broadcast and multicast.
 * It counts the number of times it is notified.
 *
 * @param <T>
 */
public class MockListener<T extends UpdateEvent> implements UpdateListener<T> {
    List<T> events = new ArrayList<>();

    boolean active = true;

    @Override
    public void onUpdate(UpdateEvent event) {
        if(active)
            events.add((T) event);
        else
            throw new RuntimeException("Listener is inactive");
    }


    public int getCount() {
        return events.size();
    }

    public List<T> getEvents() {
        return events;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
