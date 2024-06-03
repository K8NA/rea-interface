package rea.events;

public interface UpdateListener<T extends UpdateEvent> {
    void onUpdate(T updateEvent);
}
