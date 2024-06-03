package rea.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests on {@link Passage}
 * Depends on {@link Place}. Test {@link PlaceTest} first.
 * Depends on {@link Position}. Test {@link PositionTest} first.
 */
class PassageTest extends TestData {

    Passage passage;

    Visual BACKGROUND = new Visual(BACKGROUND_IMAGE, WIDTH, HEIGHT);
    Place place = new Place(BACKGROUND, DESCRITION, new Position(0, 0));
    @BeforeEach
    void setUp() {
        passage = new Passage(BACKGROUND, DESCRITION, place);
    }

    /**
     * Test {@link Passage#getPlace()}.
     */
    @Test
    void getPlace() {
        assertEquals(place, passage.getPlace());
    }

    /**
     * Check if accepts a visitor. Nothing to assert.
     */
    @Test
    void accept() {
        passage.accept(new MockVisitor());
    }
}