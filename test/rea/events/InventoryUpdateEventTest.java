package rea.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;
import rea.components.Item;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryUpdateEventTest extends TestData {

    InventoryUpdateEvent event;
    List<Item> items;

    @BeforeEach
    void setUp() {
        items = new ArrayList<>();
        event = new InventoryUpdateEvent(items,null);
    }

    @Test
    void getInventory() {
        assertEquals(items,event.getInventory());
    }

    @Test
    void getHolding() {
        assertNull(event.getHolding());
    }

}