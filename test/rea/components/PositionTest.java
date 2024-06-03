package rea.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest extends TestData {

    Position position;
    @BeforeEach
    void setUp() {
        position = new Position(X,Y);
    }

    @Test
    void getX() {
        assertEquals(X, position.getX());
    }

    @Test
    void getY() {
        assertEquals(Y, position.getY());
    }

    @Test
    void testToString() {
        assertEquals("("+X+","+Y+")", position.toString());
    }
}