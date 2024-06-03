package rea.gaming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import rea.ReaException;
import rea.TestData;
import rea.components.Character;
import rea.components.*;
import rea.events.*;
import rea.gameplay.MockGameplay;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static rea.gaming.GameStage.*;

@Disabled
public class GameInstanceTest extends TestData {

    MockGameplay gameplay;

    GameInstance gameInstance;

    MockPool<Character> characters;
    Character character;

    Item item;
    @BeforeEach
    void setUp() {

        var avatars = MockAvatar.values();
        characters = new MockPool<>(i -> new Character("Character" + i, avatars[i]), avatars.length);
        character = characters.getFirst();

        var itemVisual = new Visual(PATHNAME,WIDTH,HEIGHT);

        gameplay = new MockGameplay();

        gameInstance = new GameInstance(gameplay);

        item = new Item(itemVisual,DESCRITION);
    }

    /**
     * Creates different game instances every time.
     */
    @Test
    void getGameMap() {
        var gameMap = gameInstance.getGameMap();

        assertNotNull(gameMap);

        var otherGameInstance = new GameInstance(gameplay);
        var gameMapOfOtherInstance = otherGameInstance.getGameMap();

        assertNotSame(gameMap, gameMapOfOtherInstance, "GameMap should be a new instance each time");
    }

    /**
     * Check game name
     */
    @Test
    void getName() {
        assertEquals("Mock gameplay", gameInstance.getName());
    }

    /**
     * At start, there are no players yet.
     */
    @Test
    void getPlayerCount() {
        assertEquals(0, gameInstance.getPlayerCount(), "No players have been added yet");
    }

    /**
     * At start, game is created
     */
    @Test
    void getCurrentStage() {
        assertEquals(CREATED, gameInstance.getCurrentStage());
    }

    /**
     * Add a player and check the number of player increased.
     */
    @Test
    void addPlayer() {
        gameInstance.addPlayer(character);

        assertEquals(1, gameInstance.getPlayerCount());
    }

    /**
     * Check that players cannot be added beyond the gameplay limit.
     */
    @Test
    void addPlayer_already_started() {

        gameInstance.addPlayer(character);

        assertAll(
                () -> assertNull(gameInstance.addPlayer(character),
                "No more players can be added after the game has started"),

                () -> assertEquals(1, gameInstance.getPlayerCount(),
                "No players was added")
        );
    }

    /**
     * Check the came start if the player is ready and the
     * maximum number of players is reached.
     */
    @Test
    void playerReady() {
        var player = gameInstance.addPlayer(character);

        assertTrue(gameInstance.isNotPlayingYet());

        gameInstance.playerReady(player);

        assertFalse(gameInstance.isNotPlayingYet());
    }

    /**
     * The game doesn't start if a player is not provided.
     */
    @Test
    void playerReady_null() {
        assertTrue(gameInstance.isNotPlayingYet());

        gameInstance.playerReady(null);

        assertTrue(gameInstance.isNotPlayingYet());
    }

    /**
     * Players' avatars are retrieved.
     */
    @Test
    void getAvatars() {
        var avatars = gameInstance.getAvatars();

        assertNotNull(avatars);
        assertEquals(3, avatars.size());
    }

    /**
     * A newly created game can be deleted,
     * but after starting it cannot be deleted.
     */
    @Test
    void canDelete() {
        assertTrue(gameInstance.canDelete(), "Newly created game can be deleted");

        var player = gameInstance.addPlayer(character);
        gameInstance.playerReady(player);

        assertFalse(gameInstance.canDelete(), "Started game cannot be deleted");
    }

    /**
     * A game cannot start without players.
     * If it has the minimum number of players, it can start.
     * If it has already started, cannot start again.
     */
    @Test
    void canStart() {
        assertFalse(gameInstance.canStart(), "Game cannot start without players");

        var player = gameInstance.addPlayer(character);

        assertTrue(gameInstance.canStart(), "This game can start with a single players");

        gameInstance.playerReady(player);

        assertFalse(gameInstance.canStart(), "Playing game cannot start again");
    }

    /**
     * Check the moment a game started:
     */
    @Test
    void getPlayingSince() {
        assertNull(gameInstance.getPlayingSince(),"Game has not started yet");

        gameInstance.addPlayer(character);

        var beforeStart = new Date();

        gameInstance.startPlayingGame();

        var afterStart = new Date();

        var startMoment = gameInstance.getPlayingSince();

        assertFalse( beforeStart.after( startMoment ),
                "Game cannot start after a moment before it started" );
        assertFalse( afterStart.before( startMoment ),
                "Game cannot start before a moment after it started");
    }

    /**
     * Check the moment a game ended.
     */
    @Test
    void getPlayingUntil() {
        gameInstance.addPlayer(character);

        var beforeEnd = new Date();

        gameInstance.startPlayingGame();
        gameInstance.endPlayingGame();

        var afterEnd = new Date();

        var endMoment = gameInstance.getPlayingUntil();

        assertFalse( beforeEnd.after( endMoment ),
                "Game cannot end after a moment before it ended");
        assertFalse( afterEnd.before( endMoment ),
                "game cannot end before a moment after if ended");
    }

    /**
     * A game is only completed when {@code endPlaying()} is called.
     */
    @Test
    void isComplete() {
        assertFalse(gameInstance.isComplete(), "Game just created");

        gameInstance.addPlayer(character);

        assertFalse(gameInstance.isComplete(), "Game not started yet");

        gameInstance.startPlayingGame();

        assertFalse(gameInstance.isComplete(), "Game started but not ended");

        gameInstance.endPlayingGame();

        assertTrue(gameInstance.isComplete(), "Game has started and ended");
    }

    /**
     * When a game starts it send game and scene update events,
     * and it can only start once.
     *
     * @throws ReaException may be thrown by add listeners
     */
    @Test
    void startPlayingGame() throws ReaException {
        var gameChangedListener = new MockListener<GameChangedEvent>();
        var sceneUpdateListener = new MockListener<SceneUpdateEvent>();
        var player = gameInstance.addPlayer(character);

        gameInstance.addGameChangedListener(gameChangedListener);
        gameInstance.addGameChangedListener(player,sceneUpdateListener);

        assertAll("check status before start playing",
                () -> assertEquals(CREATED,gameInstance.getCurrentStage(),
                        "Game should have started"),
                () -> assertEquals(0,gameChangedListener.getCount(),
                        "Game change event should be received"),
                () -> assertEquals(0,sceneUpdateListener.getCount(),
                        "Scene change event should be received")
        );

        gameInstance.startPlayingGame();

        assertAll("changes expected after start playing",
                () -> assertEquals(PLAYING,gameInstance.getCurrentStage(),
                        "Game should have started"),
                () -> assertEquals(1,gameChangedListener.getCount(),
                        "Game change event should be received"),
                () -> assertEquals(1,sceneUpdateListener.getCount(),
                        "Scene change event should be received")
        );

        assertThrows(IllegalStateException.class,
                () -> gameInstance.startPlayingGame(),
                "Game already playing, cannot start again");

    }

    /**
     * A game is not playing initially,
     * but after starting, it is playing.
     */
    @Test
    void isNotPlayingYet() {
        gameInstance.addPlayer(character);

        assertTrue(gameInstance.isNotPlayingYet(), "Game has not started yet");

        gameInstance.startPlayingGame();

        assertFalse(gameInstance.isNotPlayingYet(), "Game has already started");
    }

    /**
     * Take a game to the end and check it is in the expected state.
     */
    @Test
    void endPlayingGame() {
        gameInstance.addPlayer(character);

        assertEquals(CREATED,gameInstance.getCurrentStage(),
                "Game should be created");

        gameInstance.startPlayingGame();

        assertEquals(PLAYING,gameInstance.getCurrentStage(),
                "Game should be playing");

        gameInstance.endPlayingGame();

        assertEquals(ENDED,gameInstance.getCurrentStage(),
                        "Game should have ended");

        assertThrows(IllegalStateException.class,
                () -> gameInstance.endPlayingGame(),
                "cannot end an ended game");
    }

    /**
     * Execute all possible action and check they dont raise exceptions.
     * @param action to execute
     */
    @ParameterizedTest
    @EnumSource(Action.class)
    void executeCommand(Action action) {
        var player = gameInstance.addPlayer(character);

        gameInstance.startPlayingGame();

        gameInstance.executeCommand(player, action, null);

        // no assertion, just to check that no exception is thrown
    }

    /**
     * Exceptions are raised if the game didn't start yet.
     * @param action to execute
     */
    @ParameterizedTest
    @EnumSource(Action.class)
    void executeCommand_game_not_started(Action action) {
        var player = gameInstance.addPlayer(character);

        assertThrows(IllegalStateException.class,
                () -> gameInstance.executeCommand(player, action, null),
                "An action cannot be executed if the game has not started");
    }

    @ParameterizedTest
    @EnumSource(Action.class)
    void executeCommand_game_ended(Action action) {
        var player = gameInstance.addPlayer(character);

        gameInstance.startPlayingGame();
        gameInstance.endPlayingGame();

        assertThrows(IllegalStateException.class,
                () -> gameInstance.executeCommand(player, action, null),
                "An action cannot be executed if the game has ended");
    }

    /**
     * Exceptions are raised if the player was not added to the game
     * @param action to execute
     */
    @ParameterizedTest
    @EnumSource(Action.class)
    void executeCommand_invalid_player(Action action) {
        var player = new Player(character); // not added to the game

        gameInstance.startPlayingGame();

        assertThrows(IllegalArgumentException.class,
                () -> gameInstance.executeCommand(player, action, null),
                "An action cannot be executed without a player");

    }

    /**
     * Test class grouping all tests on action execution methods.
     * Provides fixtures for handling with players and events.
     */
    @Nested
    class ExecuteMethodsTest {
        Map<Character, MockListener<InventoryUpdateEvent>> inventoryListenerMap;
        Map<Character, MockListener<SceneUpdateEvent>> sceneListenerMap;
        Map<Character, MockListener<MessageUpdateEvent>> messageListenerMap;
        /**
         * Create players and attach listeners to those players
         * to check if events are propagated
         *
         * @throws ReaException may be thrown by add listeners
         */
        @BeforeEach
        void setUp() throws ReaException {
            inventoryListenerMap = new HashMap<>();
            sceneListenerMap = new HashMap<>();
            messageListenerMap = new HashMap<>();

            gameplay.setMaxPlayers(characters.size()); // to avoid the game to end when the first player is added
            for (Character c : characters) {
                var inventoryListener = new MockListener<InventoryUpdateEvent>();
                var sceneListener = new MockListener<SceneUpdateEvent>();
                var messagListener = new MockListener<MessageUpdateEvent>();
                var player = gameInstance.addPlayer(c);

                gameInstance.addInventoryUpdateListener(player, inventoryListener);
                gameInstance.addSceneUpdateListener(player, sceneListener);
                gameInstance.addMessageUpdateListener(player,messagListener);

                inventoryListenerMap.put(c, inventoryListener);
                sceneListenerMap.put(c, sceneListener);
                messageListenerMap.put(c,messagListener);
            }
        }


        /**
         * Groups tests on executeLook()
         */
        @Nested
        class ExecuteLooKTest {

            /**
             * Look action sends a message update event just to the character
             * that executed the action.
             */
            @Test
            void executeLook() {

                assertNull(gameInstance.executeLook(character, item), "no error expected");

                for (Character c : characters) {
                    assertEquals(c == character ? 1 : 0, messageListenerMap.get(c).getCount(),
                            "event received just by the character that executed the action");
                }
            }

            /**
             * An error message is returned if there isn't an object to look.
             */
            @Test
            void executeLook_invalid() {
                assertNotNull(gameInstance.executeLook(character, null), "error message expected");
            }
        }

        /**
         * Groups tests on {@code executeTalk() }
         */
        @Nested
        class ExecuteTalkTest {

            /**
             * Talk in a place and check all other characters receive an event
             * with that message
             */
            @Test
            void executeTalk() {

                assertNull(gameInstance.executeTalk(character, MESSAGE), "no error expected");

                for (Character c : characters) {
                    assertEquals(1, messageListenerMap.get(c).getCount(),
                            "event should be received by all players");
                    var event = messageListenerMap.get(c).getEvents().getFirst();
                    assertEquals(character, event.getSpeaker(), "event should refer to the character that executed the action");
                    assertEquals(MESSAGE, event.getMessage(), "event should refer to the message that was sent");
                }
            }

            /**
             * Check an error message is returned if nothing is talked.
             */
            @Test
            void executeTalk_invalid() {
                assertNotNull(gameInstance.executeTalk(character, null), "error message expected");
            }
        }

        /**
         * Groups tests on {@code executeHold()}
         */
        @Nested
        class executeHoldTest {

            /**
             * Execute a hold action and check that just the player
             * controlling the character is notified.
             */
            @Test
            void executeHold() {

                character.addItem(item); // add item to character's inventory

                assertNull(character.getHolding(), "Nothing should be held yet");

                assertNull(gameInstance.executeHold(character, item), "no error expected");

                assertTrue(character.getInventory().contains(item), "Item should be in the inventory");
                assertEquals(item, character.getHolding(), "Item should be held");

                for (Character c : characters) {
                    assertEquals(c == character ? 1 : 0, inventoryListenerMap.get(c).getCount(),
                            "event should be received just by the character that executed the action");
                }
            }

            /**
             * Check an error message is returned if the item is not in the
             * character's inventory.
             */
            @Test
            void executeHold_not_in_inventory() {

                assertNotNull(gameInstance.executeHold(character, item),
                        "error expected since it is not in the inventory");
            }

            /**
             * Check an error message is returned if bo item was provided.
             */
            @Test
            void executeHold_invalid() {
                assertNotNull(gameInstance.executeHold(character, null),
                        "error expected since its is not a valid item");
            }
        }

        /**
         * Group tests on {@code executePick()}
         */
        @Nested
        class ExecutePickTest {

            @Test
            void executePick() {

                var startPlace = gameInstance.getGameMap().getStartPlace();

                startPlace.addGameComponent(item, new Position(X, Y));

                assertEquals(characters.size() + 1, startPlace.getPositionables().size(), "Item should be in the place");
                assertTrue(startPlace.getPositionables().contains(item), "Item should be in the place");
                assertFalse(character.getInventory().contains(item), "Item should not be in the inventory");

                assertNull(gameInstance.executePick(character, item), "no error expected");

                assertEquals(characters.size(), startPlace.getPositionables().size(), "Item should not be in the place");
                assertFalse(startPlace.getPositionables().contains(item), "Item should not be in the place");
                assertTrue(character.getInventory().contains(item), "Item should be in the inventory");

                for (Character c : characters) {
                    assertEquals(c == character ? 1 : 0, inventoryListenerMap.get(c).getCount(),
                            "event should be received just by the character that executed the action");
                    assertEquals(1, sceneListenerMap.get(c).getCount(), "event should be received by all players");

                    var event = sceneListenerMap.get(c).getEvents().getFirst();

                    assertEquals(startPlace.getVisual(), event.getBackground(),
                            "event should have background of start place");
                    assertFalse(event.getPositionables().contains(item),
                            "event should not refer to the item that was picked");
                }
            }


            @ParameterizedTest
            @ValueSource(booleans = {true, false})
            void executePick_pickable(boolean pickable) {
                gameInstance.addPlayer(character);

                gameInstance.getGameMap().getStartPlace().addGameComponent(item, new Position(X, Y));

                item.setPickable(pickable);

                assertEquals(pickable, null == gameInstance.executePick(character, item),
                        "error expected if not pickable");
            }


            @Test
            void executePick_item_not_in_place() {
                gameInstance.addPlayer(character);

                assertNotNull(gameInstance.executePick(character, item),
                        "error expected since item is not in place");
            }


            @Test
            void executePick_not_item() {
                gameInstance.addPlayer(character);

                assertNotNull(gameInstance.executePick(character, characters.get(1)),
                        "error expected since not an item");
            }
        }

        @Nested
        class ExecuteDrop {

            Place startPlace;

            @BeforeEach
            void setUp() {
                startPlace = gameInstance.getGameMap().getStartPlace();

                startPlace.addGameComponent(item, new Position(X, Y));

            }

            @Test
            void executeDrop() {

                assertEquals(characters.size() + 1, startPlace.getPositionables().size(), "Item should be in the place");
                assertTrue(startPlace.getPositionables().contains(item), "Item should be in the place");
                assertFalse(character.getInventory().contains(item), "Item should not be in the inventory");

                assertNull(gameInstance.executePick(character, item), "no error expected");

                assertEquals(characters.size(), startPlace.getPositionables().size(), "Item should not be in the place");
                assertFalse(startPlace.getPositionables().contains(item), "Item should not be in the place");
                assertTrue(character.getInventory().contains(item), "Item should be in the inventory");

                assertNull(gameInstance.executeDrop(character, item), "no error expected");

                assertEquals(characters.size() + 1, startPlace.getPositionables().size(), "Item should be in the place");
                assertTrue(startPlace.getPositionables().contains(item), "Item should be in the place");
                assertFalse(character.getInventory().contains(item), "Item should not be in the inventory");

                for (Character c : characters) {
                    assertEquals(c == character ? 2 : 0, inventoryListenerMap.get(c).getCount(),
                            "2 events should be received just by the character that executed the action");
                    assertEquals(2, sceneListenerMap.get(c).getCount(), "2 events should be received by all players");
                }
            }

            @Test
            void executeDrop_item_not_in_inventory() {
                gameInstance.addPlayer(character);

                assertNotNull(gameInstance.executeDrop(character, item),
                        "error expected since item is not in inventory");
            }

            @Test
            void executeDrop_not_an_item() {
                gameInstance.addPlayer(character);

                assertNotNull(gameInstance.executeDrop(character, characters.get(1)),
                        "error expected since is not an item");
            }
        }

        /**
         * Groups tests on {@code executeuse()}
         */
        @Nested
        class ExecuteUseTest {

            Place startPlace;
            Item modifiable, modifier, modified;

            GameMap gameMap;
            Position position;

            @BeforeEach
            void setUp() {
                var items = new MockPool<>(i -> new Item(new Visual(PATHNAMES[i], WIDTH, HEIGHT), DESCRITIONS[i]), PATHNAMES.length);

                gameMap = gameInstance.getGameMap();
                startPlace = gameInstance.getGameMap().getStartPlace();

                modifiable = items.get(0);
                modifier = items.get(1);
                modified = items.get(2);
                position = new Position(X, Y);

                startPlace.addGameComponent(modifiable, position);

                character.addItem(modifier);
                character.holdItem(modifier);
            }

            /**
             *
             */
            @ParameterizedTest
            @ValueSource(booleans = {true, false})
            void executeUse(boolean reusable) {

                modifier.setReusable(reusable);
                gameMap.defineChange(modifiable, modifier, modified);

                assertTrue(startPlace.getPositionables().contains(modifiable), "Modifiable item must be in place");
                assertFalse(startPlace.getPositionables().contains(modified), "Modified item cannot be in place");

                assertNull(gameInstance.executeUse(character, modifiable), "no error expected");

                assertFalse(startPlace.getPositionables().contains(modifiable), "Modifiable item cannot be in place");
                assertTrue(startPlace.getPositionables().contains(modified), "Modified item must be in place");

                assertEquals(reusable, character.getInventory().contains(modifier),
                        "Item should be " + (reusable ? "in" : "removed from") + " inventory");
                assertEquals(position, modified.getPosition(), "Must have the same position");

                for (Character c : characters) {
                    if (reusable) {
                        assertEquals(0, inventoryListenerMap.get(c).getCount(),
                                "event should be received by none of character that executed the action");
                    } else {
                        assertEquals(c == character ? 1 : 0, inventoryListenerMap.get(c).getCount(),
                                "event should be received just by the character that executed the action");
                    }
                    assertEquals(1, sceneListenerMap.get(c).getCount(), "event should be received by all players");

                    var event = sceneListenerMap.get(c).getEvents().getFirst();

                    assertEquals(startPlace.getVisual(), event.getBackground(),
                            "event should have background of start place");
                    assertFalse(event.getPositionables().contains(item),
                            "event should not refer to the item that was picked");
                }
            }

            /**
             * Expect an error message if item is not in scene,
             * or not an item.
             */
            @Test
            void executeUse_invalid() {
                assertAll(
                        () -> assertNotNull(gameInstance.executeUse(character, modified),
                                "error expected, item not in scene"),
                        () -> assertNotNull(gameInstance.executeUse(character, startPlace),
                                "error expected, not an item")
                );

            }
        }

        /**
         * Groups tests of commands  needing passages and multiple places.
         */
        @Nested
        class ExecuteMoveAndBackTest {

            MockPool<Visual> visuals;
            MockPool<Item> items;
            Place startPlace, otherPlace;
            Position position;
            Passage passage;

            /**
             * Fixture with places and passage
             */
            @BeforeEach
            void setUp() {
                var gameMap = gameInstance.getGameMap();

                visuals = new MockPool<>(i -> new Visual(PATHNAMES[i], WIDTH, HEIGHT), PATHNAMES.length);
                items = new MockPool<>(i -> new Item(visuals.get(i), DESCRITIONS[i]), 2);
                position = new Position(2 * X, 2 * Y);
                otherPlace = new Place(visuals.get(1), "Other Place", position);
                passage = new Passage(visuals.get(2), "Passage to other place", otherPlace);
                startPlace = gameMap.getStartPlace();

                startPlace.addGameComponent(passage, position);
            }


            @Test
            void executeMove_position() {

                assertNull(gameInstance.executeMove(character, position), "no error expected");

                assertEquals(position, character.getPosition(), "Character should be in the new position");

                for (Character c : characters) {
                    assertEquals(1, sceneListenerMap.get(c).getCount(),
                            "event should be received by all players");
                }
            }

            @Test
            void executeMove_place() {

                assertEquals(startPlace, character.getPlace(), "initially should be in start place");

                assertNull(gameInstance.executeMove(character, passage), "no error message expected");

                assertEquals(otherPlace, character.getPlace(), "should be in other place");

                for (Character c : characters) {
                    assertEquals(1, sceneListenerMap.get(c).getCount(),
                            "events should be received by all players ");

                    var event = sceneListenerMap.get(c).getEvents().getFirst();
                    var place = c == character ? otherPlace : startPlace;

                    assertEquals(place.getVisual(), event.getBackground(), "not the expected visual");
                    assertTrue(event.getPositionables().contains(c),
                            "character should be in scene");
                }
            }

            /**
             * Check errors on invalid destinations
             */
            @Test
            void executeMove_invalid() {
                assertAll("error expected since not position or passage given",
                        () -> assertNotNull(gameInstance.executeMove(character, null)),
                        () -> assertNotNull(gameInstance.executeMove(character, otherPlace)),
                        () -> assertNotNull(gameInstance.executeMove(character, items.getFirst()))
                );
            }

            @Test
            void executeBack() {
                character.move(otherPlace);

                assertEquals(otherPlace, character.getPlace(), "must be in other place");

                assertNull(gameInstance.executeBack(character, null), "no error message expected");

                assertEquals(startPlace, character.getPlace(), "must be back to start place");

                for (Character c : characters) {
                    assertEquals(1, sceneListenerMap.get(c).getCount(),
                            "events should be received by all players ");

                    var event = sceneListenerMap.get(c).getEvents().getFirst();

                    assertEquals(startPlace.getVisual(), event.getBackground(), "not the expected visual");
                    assertTrue(event.getPositionables().contains(c),
                            "all characters should be in start place's scene");
                }

                assertNotNull(gameInstance.executeBack(character, null),
                        "cannot caktrack any more, error message expected");
            }
        }
    }
}