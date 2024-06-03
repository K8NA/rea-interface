package rea.gameplay.games;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.components.Item;
import rea.components.Place;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the EasterEggRace class
 */
class EasterEggRaceTest {

    EasterEggRace easterEggRace;

    @BeforeEach
    void setUp() {
        easterEggRace = new EasterEggRace();
    }

    @Test
    void getName() {
        assertEquals("Easter Egg Race", easterEggRace.getName());
    }

    @Test
    void getDescription() {
        assertEquals("Collect all the games in the lawn", easterEggRace.getDescription());
    }

    /**
     * Test the game map has a single place with a lawn and some eggs
     */
    @Test
    void makeGameMap() {
        var gameMap = easterEggRace.makeGameMap();
        var startPlace = gameMap.getStartPlace();

        assertNotNull(startPlace, "Start place must not be null");
        assertInstanceOf(Place.class, startPlace, "Start place must be a Place");
        assertTrue(startPlace.getDescription().contains("lawn"), "Start place must be a lawn");


        var positionables = startPlace.getPositionables();

        assertFalse(positionables.isEmpty(), "At least one item must be present");

        positionables.forEach(positionable -> {
            assertInstanceOf(Item.class, positionable, "All positionables must be items");
            assertTrue(positionable.getDescription().contains("egg"), "All items are eggs");
        });
    }

    /**
     * Make sure the map is different every time
     */
    @Test
    void makeGameMap_different() {
        var startPlace1 = easterEggRace.makeGameMap();
        var startPlace2 = easterEggRace.makeGameMap();

        assertNotEquals(startPlace1, startPlace2,"Start place must be different objects");
    }

    /**
     * Test the avatars available for the game
     */
    @Test
    void getAvatars() {
        var avatars = easterEggRace.getAvatars();
        var expected = Set.of(CartoonAvatar.class.getEnumConstants());

        assertEquals(expected, avatars, "Cartoon avatars expected");
    }

    /**
     * Test the maximum number of players
     */
    @Test
    void getMaxPlayers() {
        assertEquals(1, easterEggRace.getMaxPlayers());
    }

    /**
     * Test the minimum number of players
     */
    @Test
    void getMinPlayers() {
        assertEquals(1, easterEggRace.getMinPlayers());
    }

    /**
     * Remove all eggs and checks that game only ends after collecting the last one
     */
    @Test
    void gamedEnded() {
        var gameMap = easterEggRace.makeGameMap();
        var startPlace = gameMap.getStartPlace();
        var positionables = new HashSet<>(startPlace.getPositionables());

        positionables
                .stream()
                .filter(positionable -> positionable instanceof Item)
                .map(positionable -> (Item) positionable)
                .forEach(startPlace::removeGameComponent);

        assertTrue(easterEggRace.gamedEnded(gameMap), "game ends after collecting all eggs");
    }
}