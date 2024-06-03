package rea.components;

import java.util.HashSet;
import java.util.Set;

/**
 * Mock visitor for testing. Collects in sets the objects it visits.
 */
public class MockVisitor implements Visitor {
    Set<Character> characters = new HashSet<>();
    Set<Place> places = new HashSet<>();
    Set<Item> items = new HashSet<>();
    Set<Passage> passages = new HashSet<>();

    @Override
    public void visit(Character character) {
        characters.add(character);
    }

    @Override
    public void visit(Place place) {
        places.add(place);
        place.getPositionables().forEach(item -> item.accept(this));
    }

    @Override
    public void visit(Item item) {
        items.add(item);
    }

    @Override
    public void visit(Passage passage) {
        passages.add(passage);
        passage.getPlace().accept(this);
    }
}
