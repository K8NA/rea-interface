package rea.events;
import rea.components.Character;

public class MessageUpdateEvent implements UpdateEvent {

    private Character speaker;
    private final String message;

    public MessageUpdateEvent(String message) {
        this.message = message;
    }

    public MessageUpdateEvent(Character speaker, String message) {
        this.speaker = speaker;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public Character getSpeaker() {
        return speaker;
    }
}
