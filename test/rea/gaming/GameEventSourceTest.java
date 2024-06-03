package rea.gaming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;
import rea.components.*;
import rea.components.Character;
import rea.ReaException;
import rea.events.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameEventSource tests
 *
 */
class GameEventSourceTest extends TestData {

    final static Visual VISUAL = new Visual(BACKGROUND_IMAGE, WIDTH, HEIGHT);

    GameEventSource gameEventSource;
    Character character;
    Player player;

    Place place;
    @BeforeEach
    void setUp() {
        final var avatars = MockAvatar.values();
        final var players = new MockPool<>(i -> new Character("Character"+i, avatars[i]), avatars.length);


        gameEventSource = new GameEventSource();
        character = players.getFirst();
        player = new Player(character);

        place = new Place(VISUAL,DESCRITION, new Position(0, 0));
    }

    /**
     * Bradcast game changed events are received.
     */
    @Test
    void gameChanged() {
        var listener = new MockListener<GameChangedEvent>();

        gameEventSource.addGameChangedListener(listener);

        assertEquals(0,listener.getCount());

        gameEventSource.broadcastGameChanged(null);

        assertEquals(1,listener.getCount());
    }

    /**
     * Inventory update events sent by multicast to an identified character are received
     */
    @Test
    void inventoryUpdate() throws ReaException {
        var listener = new MockListener<InventoryUpdateEvent>();

        gameEventSource.players.add(player);

        gameEventSource.addInventoryUpdateListener(player,listener);

        assertEquals(0,listener.getCount());

        gameEventSource.unicastInventoryUpdate(character);

        assertEquals(1,listener.getCount(),"event expected");
    }

    /**
     * If the character is not added to the game,
     * adding an inventory update listener throws an exception.
     */
    @Test
    void inventoryUpdate_identified_player_not_added() {
        var listener = new MockListener<InventoryUpdateEvent>();

        assertAll(
                () -> assertThrows(ReaException.class,()->gameEventSource.addInventoryUpdateListener( null ,listener),
                        "null character" ),
                () -> assertThrows(ReaException.class,()->gameEventSource.addInventoryUpdateListener(player,listener),
                        "character not added to players")
        );
    }

    /**
     * Message update events sent by multicast to an identified character are received
     */
    @Test
    void messageUpdate_multicast() throws ReaException {
        var listener = new MockListener<MessageUpdateEvent>();
        var place = new Place(VISUAL,DESCRITION, new Position(0, 0));

        gameEventSource.players.add(player);

        gameEventSource.addMessageUpdateListener(player,listener);

        assertEquals(0,listener.getCount());

        place.addGameComponent(character, new Position(0, 0));

        gameEventSource.multicastMessageUpdate(place, character,MESSAGE);

        assertEquals(1,listener.getCount(),"event expected");
    }


    /**
     * If the character is not added to the game,
     * adding a message listener throws an exception.
     */
    @Test
    void messageUpdate_no_identified_player() {
        var listener = new MockListener<MessageUpdateEvent>();

        assertAll(
                () -> assertThrows(ReaException.class,()->gameEventSource.addMessageUpdateListener( null ,listener),
                        "null character" ),
                () -> assertThrows(ReaException.class,()->gameEventSource.addMessageUpdateListener(player,listener),
                        "character not added to players")
        );
    }

    /**
     * Scene update events sent by multicast to an identified character are received
     */
    @Test
    void sceneUpdate() throws ReaException {
        var listener = new MockListener<SceneUpdateEvent>();

        gameEventSource.players.add(player);

        gameEventSource.addSceneUpdateListener(player,listener);

        assertEquals(0,listener.getCount());

        place.addGameComponent(character, new Position(0, 0));

        gameEventSource.multicastSceneUpdate(place);

        assertEquals(1,listener.getCount(),"event expected");
    }

    /**
     * If the character is not added to the game,
     * adding a scene update listener throws an exception.
     */
    @Test
    void sceneUpdate_no_identified_player() {
        var listener = new MockListener<SceneUpdateEvent>();

        assertAll(
                () -> assertThrows(ReaException.class,()->gameEventSource.addSceneUpdateListener( null ,listener),
                        "null character" ),
                () -> assertThrows(ReaException.class,()->gameEventSource.addSceneUpdateListener(player,listener),
                        "character not added to players")
        );

    }

    /**
     * Message update events sent by unicast are received.
     */
    @Test
    void messageUpdate_unicast() throws ReaException {
        var listener = new MockListener<MessageUpdateEvent>();

        gameEventSource.players.add(player);

        gameEventSource.addMessageUpdateListener(player,listener);

        assertEquals(0,listener.getCount());

        gameEventSource.unicastMessageUpdate(character,MESSAGE);

        assertEquals(1,listener.getCount(),"event expected");
    }

}