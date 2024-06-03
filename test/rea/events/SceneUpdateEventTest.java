package rea.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;
import rea.components.Positionable;
import rea.components.Visual;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SceneUpdateEventTest extends TestData {

    static final Visual VISUAL = new Visual(BACKGROUND_IMAGE, WIDTH, HEIGHT);
    SceneUpdateEvent event;
    List<Positionable> positionables;

    @BeforeEach
    void setUp() {
        positionables = new ArrayList<>();
        event = new SceneUpdateEvent(VISUAL,positionables);
    }

    @Test
    void getBackground() {
        assertEquals(VISUAL,event.getBackground());
    }

    @Test
    void getPositionables() {
        assertEquals(positionables,event.getPositionables());
    }
}