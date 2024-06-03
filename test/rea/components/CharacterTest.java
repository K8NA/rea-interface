package rea.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest extends TestData {

    MockPool<Character> characters;
    MockPool<Visual> visuals;
    Character character;
    Item item;

    @BeforeEach
    void setUp() {
        var avatars = MockAvatar.values();
        characters = new MockPool<>(i -> new Character("Character" + i, avatars[i]), avatars.length);
        character = characters.getFirst();

        visuals = new MockPool<>(i -> new Visual(PATHNAMES[i],WIDTH,HEIGHT), PATHNAMES.length);

        item = new Item(visuals.getFirst(), DESCRITION);
    }

    /**
     * The character has a name with which it was instantiated.
     */
    @Test
    void getName() {
        assertEquals("Character0", character.getName());
    }

    /**
     * At start, the character is not in any place or position.
     */
    @Test
    void getPlace() {
        assertNull(character.getPlace());
        assertNull(character.getPosition());
    }

    /**
     * After moving to a place, the character should be in that place.
     * The character should be positioned at the entrance of the place.
     * The place should contain the character.
     */
    @Test
    void move() {
        Position entrance = new Position(0, 0);
        Place place = new Place(visuals.getFirst(), NAME, entrance);

        character.move(place);

        assertEquals(place, character.getPlace(), "Character should be in the place");
        assertEquals(entrance, character.getPosition(), "Character should be at the entrance of the place");

        assertTrue(place.getCharacters().contains(character),"Place should contain the character");
    }

    /**
     * After moving through places, the character should be able to move back.
     * When moving back, it should be positioned at the entrance.
     * The character should not be able to move back from the starting place.
     *
     */
    @Test
    void moveBack() {
        MockPool<Place> placePool = new MockPool<>(i -> new Place(visuals.get(i), NAMES[i], new Position(X + i, Y + i)));
        var entranceDescrition = placePool.getFirst().getDescription();

        for (Place place : placePool) {
            character.move(place);

            assertEquals(place, character.getPlace());
            assertEquals(place.getEntrance(), character.getPosition());
        }

        placePool.reversed().forEach(place -> {
            assertEquals(place, character.getPlace());
            assertEquals(place.getEntrance(), character.getPosition());

            var back = character.moveBack();

            if(place.getDescription().equals(entranceDescrition)) {
                assertNull(back, "Character should not be able to move back from the starting place");
                assertTrue(placePool.getFirst().getCharacters().contains(character), "The starting place should contain the character");
            } else {
                assertNotNull(back, "Character should be able to move back");

                assertFalse(place.getCharacters().contains(character),"Place should not contain the character");
                assertTrue(back.getCharacters().contains(character), "Previous place should contain the character");
            }
        });

    }

    /**
     * At start, the character has no inventory.
     */
    @Test
    void getInventory() {
        assertEquals(0, character.getInventory().size());
    }

    /**
     * Add an item to the character's inventory and check it has it
     */
    @Test
    void addItem() {
        character.addItem(item);

        assertEquals(1, character.getInventory().size());
        assertEquals(item, character.getInventory().getFirst());
    }


    @Test
    void dropItem() {
        character.addItem(item);

        assertEquals(1, character.getInventory().size());

        character.dropItem(item);

        assertEquals(0, character.getInventory().size());
    }

    @Test
    void holdItem() {

        character.holdItem(item);

        assertEquals(item, character.getHolding());

    }

    /**
     * At start, the character is not holding anything.
     */
    @Test
    void getHolding() {
        assertNull(character.getHolding());
    }

    /**
     * Check it accepts a visitor. Nothing to assert.
     */
    @Test
    void accept() {
        character.accept(new MockVisitor());
    }

}