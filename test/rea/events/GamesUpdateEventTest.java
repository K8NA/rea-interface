package rea.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;
import rea.gaming.GameInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GamesUpdateEventTest extends TestData {

    GamesUpdateEvent event;
    List<GameInstance> games;

    @BeforeEach
    void setUp() {
        games = new ArrayList<>();
        event = new GamesUpdateEvent(games);
    }

    @Test
    void getGamesToPlay() {
        assertEquals(games,event.getGamesToPlay());
    }
}