package rea.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rea.TestData;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest extends TestData {
    Item item;

    @BeforeEach
    void setUp() {
        item = new Item(new Visual(PATHNAME,WIDTH,HEIGHT), DESCRITION);
    }

    /**
     * Test {@link Item#isPickable()}. By default, must be true.
     */
    @Test
    void isPickable() {
        assertEquals(true, item.isPickable());
    }

    /**
     * Test {@link Item#setPickable(boolean)}.
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void setPickable(boolean pickable) {
        item.setPickable(pickable);
        assertEquals(pickable, item.isPickable());
    }

    /**
     * Test {@link Item#isReusable()}. By default, must be false.
     */
    @Test
    void isReusable() {
        assertEquals(false, item.isReusable());
    }

    /**
     * Check reusable can be changed
     * @param reusable the new value for reusable
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void setReusable(boolean reusable) {
        item.setReusable(reusable);
        assertEquals(reusable, item.isReusable());
    }

    /**
     * Check it accepts a visitor. Nothing to assert.
     */
    @Test
    void accept() {
        item.accept(new MockVisitor());
    }

    @Test
    void equals() {
        assertEquals(item, new Item(new Visual(PATHNAME,WIDTH,HEIGHT), DESCRITION));

        assertNotEquals(item, new Item(new Visual(PATHNAME,WIDTH,HEIGHT), "Different description"));
        assertNotEquals(item, "Not an item");
    }
}