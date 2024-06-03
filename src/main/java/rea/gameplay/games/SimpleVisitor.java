package rea.gameplay.games;

import java.util.HashSet;
import java.util.Set;
import rea.components.*;
import rea.components.Character;

public class SimpleVisitor implements Visitor {
    private final Set<Place> places = new HashSet<>();
    private final Set<Item> items = new HashSet<>();
    private final Set<Character> characters = new HashSet<>();
    private final Set<Passage> passages = new HashSet<>();


    public void visit(Place place) {
        places.add(place);
    }

    public void visit(Item item) {
        items.add(item);
    }

    public void visit(Character character) {
        characters.add(character);
    }

    public void visit(Passage passage) {
        passages.add(passage);
    }

    public Set<Place> getPlaces() {
        return places;
    }

    public Set<Item> getItems() {
        return items;
    }

    public Set<Character> getCharacters() {
        return characters;
    }

    public Set<Passage> getPassages() {
        return passages;
    }
}
