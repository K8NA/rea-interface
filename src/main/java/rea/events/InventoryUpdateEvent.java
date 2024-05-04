package rea.events;

import rea.components.Item;

import java.util.List;

public class InventoryUpdateEvent implements UpdateEvent {

    private final List<Item> items;
    private final Item holding;

    public InventoryUpdateEvent(List<Item> items, Item holding) {
        this.items = items;
        this.holding = holding;
    }

    public List<Item> getInventory() {
        return items;
    }

    public Object getHolding() {
        return holding;
    }
}
