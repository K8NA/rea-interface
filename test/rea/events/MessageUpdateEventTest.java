package rea.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;

import static org.junit.jupiter.api.Assertions.*;

class MessageUpdateEventTest extends TestData {

    MessageUpdateEvent event;
    @BeforeEach
    void setUp() {
        event = new MessageUpdateEvent(MESSAGE);
    }

    @Test
    void getMessage() {
        assertEquals(MESSAGE,event.getMessage());
    }
}