package rea.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 *  Test class for {@link Visual}

 */
class VisualTest extends TestData {

    Visual visual;

    @BeforeEach
    void setUp() {
        visual = new Visual(PATHNAME, WIDTH, HEIGHT);
    }

    /**
     * Pathname must be the same as the one used to create the visual
     */
    @Test
    void getPathname() {
        assertEquals(PATHNAME, visual.getPathname());
    }

    /**
     * Width must be the same as the one used to create the visual
     */
    @Test
    void getWidth() {
        assertEquals(WIDTH, visual.getWidth());
    }

    /**
     * Height must be the same as the one used to create the visual
     */
    @Test
    void getHeight() {
        assertEquals(HEIGHT, visual.getHeight());
    }

    /**
     * Test {@link Visual#equals(Object)}.
     */
    @Test
    void testEquals() {
        assertEquals(visual, new Visual(PATHNAME, WIDTH, HEIGHT));
        assertNotEquals(visual, new Visual(PATHNAMES[1], WIDTH, HEIGHT));
        assertNotEquals(visual, new Visual(PATHNAME, WIDTH + 1, HEIGHT));
        assertNotEquals(visual, new Visual(PATHNAME, WIDTH , HEIGHT + 1));
    }
}