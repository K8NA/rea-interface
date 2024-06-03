package rea.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rea.TestData;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  Tests on {@link Positionable}
 *  Depends on {@link Position}. Test {@link PositionTest} first.
 */
class PositionableTest extends TestData {

    static final Visual VISUAL = new Visual(BACKGROUND_IMAGE, WIDTH, HEIGHT);

    MockPositionable positionable;

    Position position = new Position(X,Y);

    @BeforeEach
    void setUp() {
        positionable = new MockPositionable(VISUAL, DESCRITION);
    }

    /**
     * At first has no position.
     */
    @Test
    void getPosition() {
        assertNull( positionable.getPosition());
    }
    /**
     * Change position and check it.
     */
    @ParameterizedTest
    @CsvSource({"0,0", "100,100", "200,100", "300,200"})
    void moveTo(int x, int y) {
        Position position = new Position(x, y);

        positionable.moveTo(position);

        assertEquals(position, positionable.getPosition());
    }


}