package rea.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Character extends Positionable {

    private final String name;
    private Item holding;
    private List<Item> inventory;
    private Stack<Place> path;

    public Character(String name, Avatar avatar) {
        super(avatar.getAvatarVisual(), name);
        this.name = name;
        this.inventory = new ArrayList<>();
        this.path = new Stack<>();
    }

    public String getName() {
        return name;
    }

    public Place getPlace() {
        if(!path.isEmpty()) {
            return path.peek();
        } else {
        return null;
        }
    }

    public void move(Place place) {
        if(place != null) {
            if (path == null) {
                path = new Stack<>();
            }
            path.push(place);
            this.position = place.getEntrance();
            place.addGameComponent(this, this.position);
        }
    }

    public Place moveBack() {
        if (path != null && !path.isEmpty()) {
            Place currentPlace = getPlace();
            if (currentPlace != null) {
                currentPlace.removeGameComponent(this);
            }
            Place previousPlace = path.pop();
            if (previousPlace != null) {
                previousPlace.addGameComponent(this, previousPlace.getEntrance());
            }

            return previousPlace;
        } else {
            return null;
        }
    }



    public List<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item) {
        if(inventory == null) {
            inventory = new ArrayList<>();
        }
        inventory.add(item);
    }

    public void dropItem(Item item) {
        if(inventory != null) {
            inventory.remove(item);
        }
    }

    public void holdItem(Item item) {
        this.holding = item;
    }

    public Item getHolding() {
        return holding;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
