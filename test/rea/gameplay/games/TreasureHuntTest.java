package rea.gameplay.games;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import rea.components.Item;
import rea.components.Passage;
import rea.components.Place;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the TreasureHunt class
 */
@Disabled
class TreasureHuntTest {
    TreasureHunt treasureHunt;

    @BeforeEach
    void setUp() {
        treasureHunt = new TreasureHunt();
    }

    /**
     * Test the name of the game.
     */
    @Test
    void getName() {
        assertEquals("Treasure Hunt", treasureHunt.getName());
    }

    /**
     * Test the description of the game
     */
    @Test
    void getDescription() {
        var expected = "Find the treasure hidden inside the house with a closed door.";
        assertEquals(expected, treasureHunt.getDescription());
    }

    /**
     * Test the initial game map has a single place with a house and a key
     */
    @Test
    void makeGameMap() {
        var gameMap = treasureHunt.makeGameMap();
        var startPlace = gameMap.getStartPlace();

        assertNotNull(startPlace, "Start place must not be null");
        assertInstanceOf(Place.class, startPlace, "Start place must be a Place");

        var items = startPlace.getItems();

        assertEquals(2,items.size() , "Two item must be present");

        assertTrue(hasItemWith(items,"house"), "House must be present");
        assertTrue(hasItemWith(items,"key"), "key must be present");
    }

    boolean hasItemWith(Set<Item> itens,String text) {
        return itens
                .stream()
                .map( item -> item.getDescription().toLowerCase())
                .anyMatch(description -> description.contains(text));
    }

    /**
     * Make sure the map is different every time
     */
    @Test
    void makeGameMap_different() {
        var startPlace1 = treasureHunt.makeGameMap();
        var startPlace2 = treasureHunt.makeGameMap();

        assertNotEquals(startPlace1, startPlace2,"Start place must be different objects");
    }

    /**
     * Test the avatars available for the game
     */
    @Test
    void getAvatars() {
        var avatars = treasureHunt.getAvatars();
        var expected = Set.of(CartoonAvatar.class.getEnumConstants());

        assertEquals(expected, avatars, "Cartoon avatars expected");
    }

    /**
     * Test the maximum number of players.
     */
    @Test
    void getMaxPlayers() {
        assertEquals(2, treasureHunt.getMaxPlayers());
    }

    /**
     * Test the minimum number of players
     */
    @Test
    void getMinPlayers() {
        assertEquals(1, treasureHunt.getMinPlayers());
    }

    /**
     * Test the game ended.
     * The game ends when the treasure is removed from the house, but first the door must be open.
     */
    @Test
    void gamedEnded() {
        var gameMap = treasureHunt.makeGameMap();
        var startPlace = gameMap.getStartPlace();
        var itens = startPlace.getItems();
        Item houseCloseDoor = getItemWith(itens, "house");
        Item key = getItemWith(itens, "key");

        assertNotNull(key, "Key must be present");
        assertNotNull(houseCloseDoor, "House with closed door must be present");

        var modified = gameMap.getChange(houseCloseDoor, key);

        assertNotNull(modified, "House with open door must be present");
        assertInstanceOf(Passage.class, modified, "House with open door must be a passage");

        Passage houseOpenDoor = (Passage) modified;
        gameMap.getStartPlace().addGameComponent(houseOpenDoor, houseCloseDoor.getPosition());

        var insideTheHouse = houseOpenDoor.getPlace();
        var treasure = getItemWith(insideTheHouse.getItems(),"treasure");

        assertNotNull(treasure, "Treasure must be present");

        insideTheHouse.removeGameComponent(treasure);

        assertTrue(treasureHunt.gamedEnded(gameMap));
    }

    /**
     * Get the first item containing given text in the description
     * @param itens to search
     * @param text to lookup
     * @return item or null if not found
     */
    Item getItemWith(Set<Item> itens,String text) {
        return itens
                .stream()
                .filter(item -> item.getDescription().toLowerCase().contains(text))
                .findFirst()
                .orElse(null);
    }
}