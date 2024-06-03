package rea.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rea.TestData;

import java.util.HashSet;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PlaceTest extends TestData {

    MockPool<Visual> visuals;
    Place place;

    @BeforeEach
    void setUp() {
        visuals = new MockPool<>(i -> new Visual(PATHNAMES[i],WIDTH,HEIGHT), PATHNAMES.length);
        place = new Place(visuals.getFirst(),DESCRITION, new Position(X, Y));
    }

    /**
     * At first, a place has no positionables.
     */
    @Test
    void getPositionables() {
        var positionables = place.getPositionables();

        assertNotNull(positionables);
        assertEquals(0,positionables.size());
    }

    /**
     * Add a positionable component to this place in a given position.
     * Check that the positionable has the position and that the place has the positionable.
     *
     * @param x coordinate of the position.
     * @param y coordinate of the position.
     */
    @ParameterizedTest
    @CsvSource({"0,0", "100,100", "200,100", "300,200"})
    void addGameComponent(int x, int y) {
        Positionable positionable = new MockPositionable(visuals.getFirst(), DESCRITION);
        Position position = new Position(x, y);

        assertNull(positionable.getPosition());

        place.addGameComponent(positionable, position);

        assertNotNull(positionable.getPosition());
        assertEquals(position, positionable.getPosition());

        var positionables = place.getPositionables();

        assertNotNull(positionables);
        assertEquals(1, positionables.size());
        assertTrue(positionables.contains(positionable));
    }

    /**
     * Add several positionable components to this place in different positions.
     * Remove then and check they are removed.
     */
    @Test
    void removeGameComponent() {
        IntStream.range(0,10).forEach(i -> {
            Positionable positionable = new MockPositionable(visuals.getFirst(), DESCRITION);
            Position position = new Position(i, i);

            place.addGameComponent(positionable, position);
        });

        var positionables = new HashSet<>(place.getPositionables());

        assertNotNull(positionables);

        var count = positionables.size();
        for(Positionable positionable : positionables){
            assertTrue(place.removeGameComponent(positionable));

            assertEquals(--count, place.getPositionables().size());
        }

    }

    /**
     * Cannot remove a non-existing positionable.
     */
    @Test
    void removeGameComponent_null() {
        assertFalse(place.removeGameComponent(null));
    }

    @Test
    void removeGameComponent_Item_Character() {
        Item item = new Item(visuals.getFirst(), DESCRITION);
        Character character = new Character("Character",MockAvatar.AVATAR1);

       assertEquals(0, place.getPositionables().size());

        place.addGameComponent( item, new Position(X, Y));

        assertEquals(1, place.getPositionables().size());

        place.addGameComponent(character, new Position(X+100, Y));

        assertEquals(2, place.getPositionables().size());

        assertTrue(place.removeGameComponent(item));

        assertEquals(1, place.getPositionables().size());
    }


    /**
     * Remove a missing component from the place.
     * It should return false.
     */
    @Test
    void removeGameComponent_missing() {
        Positionable positionable = new MockPositionable(visuals.getFirst(), DESCRITION);

        assertFalse(place.removeGameComponent(positionable));
    }

    /**
     * Add characters to the place and check that they are returned.
     * but not other positionables.
     */
    @Test
    void getCharacters() {
        var avatars = MockAvatar.values();
        var characters = new MockPool<>(i -> new Character("Character" + i, avatars[i]), avatars.length);

        characters.forEach(player -> place.addGameComponent(player, new Position(X, Y)));

        place.addGameComponent(new MockPositionable(visuals.getFirst(), DESCRITION), new Position(X, Y));

        var placePlayers = place.getCharacters();

        assertNotNull(placePlayers);
        assertEquals(characters.size(), placePlayers.size());
        assertTrue(placePlayers.containsAll(characters));
    }

    /**
     * Add items to the place and check that they are returned,
     * but not other positionables.
     */
    @Test
    void getItems() {
        var items = new MockPool<>(i -> new Item(visuals.get(i), DESCRITIONS[i]), DESCRITIONS.length);

        items.forEach(player -> place.addGameComponent(player, new Position(X, Y)));

        place.addGameComponent(new MockPositionable(visuals.getFirst(), DESCRITION), new Position(X, Y));

        var placeItems = place.getItems();

        assertNotNull(placeItems);
        assertEquals(items.size(), placeItems.size());
        assertTrue(placeItems.containsAll(items));
    }

    /**
     * Check it accepts a visitor. Nothing to assert.
     */
    @Test
    void accept() {
        place.accept(new MockVisitor());
    }

}