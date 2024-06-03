package rea.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Place extends Component {

    final Position entrance;
    List<Positionable> positionables;

    public Place(Visual visual, String description, Position entrance) {
        super(visual, description);
        this.entrance = entrance;
        this.positionables = new ArrayList<>();
    }

    public List<Positionable> getPositionables() {
        return new ArrayList<>(positionables);
    }

    public Set<Item> getItems() {
        Set<Item> items = new HashSet<>();
        for (Positionable positionable : positionables) {
            if (positionable instanceof Item) {
                items.add((Item) positionable);
            }
        }
        return items;
    }

    public Position getEntrance() {
        return entrance;
    }

    public Set<Character> getCharacters() {
        Set<Character> characters = new HashSet<>();
        for (Positionable positionable : positionables) {
            if (positionable instanceof Character) {
                characters.add((Character) positionable);
            }
        }
        return characters;
    }

    public Place addGameComponent(Positionable positionable, Position position) {
        positionable.moveTo(position);
        positionables.add(positionable);
        return this;
    }

    public boolean removeGameComponent(Positionable positionable) {
        return positionables.remove(positionable);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

