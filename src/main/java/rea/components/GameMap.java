package rea.components;

import rea.gameplay.games.SimpleVisitor;

import java.util.HashMap;
import java.util.Map;

public class GameMap {

    private final Map<Positionable, Map<Item,Positionable>> changes;
    private Place startPlace;

    public GameMap(Place startPlace) {
        this.startPlace = startPlace;
        this.changes = new HashMap<>();
    }

    public Place getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(Place startPlace) {
        this.startPlace = startPlace;
    }

    public void defineChange(Positionable modifiable, Item tool, Positionable modified) {
        Map<Item, Positionable> toolModified = changes.computeIfAbsent(modifiable, k -> new HashMap<>());
        toolModified.put(tool, modified);
    }

    public Positionable getChange(Positionable modifiable, Item tool) {
        Map<Item,Positionable> toolModified = changes.get(modifiable);
        if (toolModified == null || toolModified.containsKey(tool)) {
            return null;
        }
        return toolModified.get(tool);
    }

    public void visitMap(Visitor visitor) {
        visitor.visit(this.getStartPlace());
    }

    public void accept(SimpleVisitor visitor) {
        visitor.visit(this.getStartPlace());
    }
}
