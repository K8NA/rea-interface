package rea.gaming;

import rea.ReaException;
import rea.components.Item;
import rea.components.Place;
import rea.components.Character;
import rea.events.*;
import rea.gameplay.Gameplay;

import java.util.Set;

public class GameEventSource {
    private final EventBroadcast<GameChangedEvent> gameChanged = new EventBroadcast<>();
    private final EventMulticast<InventoryUpdateEvent> inventoryUpdate = new EventMulticast<>();
    private final EventMulticast<MessageUpdateEvent> messageUpdate = new EventMulticast<>();
    private final EventMulticast<SceneUpdateEvent> sceneUpdate = new EventMulticast<>();
    Set<Player> players = null;

    public GameEventSource(Set<Player> players) {
        this.players = players;
    }

    protected GameEventSource(Gameplay gameplay) {
    }

    public GameEventSource() {

    }

    public void addGameChangedListener(UpdateListener<GameChangedEvent> listener) {
        gameChanged.addListener(listener);
    }
    public void addGameChangedListener(Player player, UpdateListener<SceneUpdateEvent> sceneUpdateListener) {
    }

    public void addInventoryUpdateListener(Player player, UpdateListener<InventoryUpdateEvent> listener) throws ReaException {
        if (!players.contains(player)) {
            throw new ReaException("Player not in this game");
        }
        inventoryUpdate.addListener(player.getCharacter(), listener);
    }

    public void addMessageUpdateListener(Player player, UpdateListener<MessageUpdateEvent> listener) throws ReaException {
        if (!players.contains(player)) {
            throw new ReaException("Player not in this game");
        }
        messageUpdate.addListener(player.getCharacter(), listener);
    }

    public void addSceneUpdateListener(Player player, UpdateListener<SceneUpdateEvent> listener) throws ReaException {
        if (!players.contains(player)) {
            throw new ReaException("Player not in this game");
        }
        sceneUpdate.addListener(player.getCharacter(), listener);
    }

    // Broadcast major game changes to all registered listeners
    void broadcastGameChanged(GameInstance gameInstance) {
        gameChanged.broadcast(new GameChangedEvent(gameInstance));
    }

    // Multicast players in the given scene with the current state of that scene
    void multicastSceneUpdate(Place place) {
        sceneUpdate.multicast(place.getCharacters(), new SceneUpdateEvent(place.getVisual(), place.getPositionables()));
        //place, new SceneUpdateEvent(place.getVisual(), place.getPositionables()));
    }

    // Multicasts players in the given place with a message
    void multicastMessageUpdate(Place place, Character character, String message) {
        messageUpdate.multicast(place.getCharacters(), new MessageUpdateEvent(character, message));
    }

    // Sends a MessageUpdateEvent to the character
    void unicastMessageUpdate(Character character, String message) {
        messageUpdate.unicast(character, new MessageUpdateEvent(character, message));
    }

    // Sends an InventoryUpdateEvent to the character given as argument
    void unicastInventoryUpdate(Character character) {
        inventoryUpdate.unicast(character, new InventoryUpdateEvent(character.getInventory(), character.getHolding()));
    }

}
