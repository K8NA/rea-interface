package rea.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;

import static org.junit.jupiter.api.Assertions.assertNull;

class GameChangedEventTest extends TestData {

    GameChangedEvent event;

    @BeforeEach
    void setUp() {
        event = new GameChangedEvent(null);
    }

    @Test
    void getgameInstance() {

        assertNull(event.getGameInstance());
    }

}