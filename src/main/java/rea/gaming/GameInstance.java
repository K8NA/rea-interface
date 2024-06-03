package rea.gaming;

import rea.ReaException;
import rea.components.*;
import rea.components.Character;
import rea.events.*;
import rea.gameplay.Gameplay;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GameInstance extends GameEventSource {
    private final Gameplay gameplay;
    private final GameMap gameMap;
    private Set<Player> players;
    private final Date playingSince;
    private Date playingUntil;
    private GameStage currentStage;

    public GameInstance(Gameplay gameplay) {
        super();
        this.gameplay = gameplay;
        this.gameMap = gameplay.makeGameMap();
        this.players = new HashSet<>();
        this.playingSince = new Date();
        this.currentStage = GameStage.CREATED;
    }

    public Set<Player> getPlayers() {
        return players;
    }


    public GameMap getGameMap() {
        return gameMap;
    }

    public String getDescription(){
        return gameplay.getDescription();
    }

    public String getName() {
        return gameplay.getName();
    }

    public int getPlayerCount() {
        return players.size();
    }

    public GameStage getCurrentStage() {
        return currentStage;
    }

    public Player addPlayer(Character character) {
        if (getCurrentStage() == GameStage.ENDED || getPlayerCount() >= gameplay.getMaxPlayers()) {
            return null;
        }
        Player player = new Player(character);
        players.add(player);
        broadcastGameChanged(this);
        return player;
    }

    public void removePlayer(Player player) {
        players.remove(player);
        broadcastGameChanged(this);
    }


    public void playerReady(Player player) {
        if (getPlayerCount() == gameplay.getMaxPlayers()) {
            startPlayingGame();
        }
    }

    public Set<Avatar> getAvatars() {
        return gameplay.getAvatars();
    }

    public boolean canDelete() {
        GameStage currentStage = getCurrentStage();
        return (currentStage == GameStage.CREATED && getPlayerCount() == 0) ||
                currentStage == GameStage.ENDED;
    }

    public boolean canStart() {
        return getCurrentStage() == GameStage.CREATED && getPlayerCount() >= gameplay.getMinPlayers();
    }

    public boolean canJoin() {
        return getCurrentStage() == GameStage.PLAYING && getPlayerCount() <= gameplay.getMaxPlayers();
    }

    public Date getPlayingSince() {
        return playingSince;
    }

    public Date getPlayingUntil() {
        return playingUntil;
    }

    public boolean isComplete() {
        return getCurrentStage() == GameStage.ENDED;
    }

    public boolean isNotPlayingYet() {
        return getCurrentStage() == GameStage.CREATED;
    }

    public void startPlayingGame() {
        currentStage = GameStage.PLAYING;
        broadcastGameChanged(this);
        multicastSceneUpdate(gameMap.getStartPlace());
    }

    public void endPlayingGame() {
        if (getCurrentStage() != GameStage.PLAYING) {
            throw new IllegalStateException("Game is not currently playing");
        }
        currentStage = GameStage.ENDED;
        players.clear();
        playingUntil = new Date();
        broadcastGameChanged(this);
    }

    public String executeCommand(Player player, Action action, Object object) {
        if (currentStage != GameStage.PLAYING) {
            throw new IllegalStateException("Game is not currently playing");
        }
        if (!players.contains(player)) {
            throw new IllegalArgumentException("Player not in the game");
        }
        Character character = player.getCharacter();
        return switch (action) {
            case LOOK -> executeLook(character, object);
            case MOVE -> executeMove(character, object);
            case PICK -> executePick(character, object);
            case DROP -> executeDrop(character, object);
            case HOLD -> executeHold(character, object);
            case USE -> executeUse(character, object);
            case TALK -> executeTalk(character, object);
            case BACK -> executeBack(character, object);
            default -> "Invalid action";
        };
    }

    String executeLook(Character character, Object object) {
        String description;
        if (object instanceof Positionable) {
            description = ((Positionable) object).getDescription();
        } else {
            return "Object not found";
        }
        unicastMessageUpdate(character, description);
        return null;
    }

    String executeMove(Character character, Object object) {
        if (!(object instanceof Passage passage)) {
            return "Invalid move target";
        }
        Place newPlace = passage.getPlace();
        character.move(newPlace);
        multicastSceneUpdate(newPlace);
        return null;
    }

    String executePick(Character character, Object object) {
        if (!(object instanceof Item item)) {
            return "Cannot pick up this object";
        }
        Place place = character.getPlace();
        if (place != null && place.removeGameComponent(item)) {
            character.addItem(item);
            unicastInventoryUpdate(character);
            multicastSceneUpdate(place);
            return null;
        } else {
            return "Item not found in the current place";
        }
    }

    String executeDrop(Character character, Object object) {
        if (!(object instanceof Item item)) {
            return "Cannot drop this object";
        }
        Place place = character.getPlace();
        if (place != null) {
            character.dropItem(item);
            place.addGameComponent(item, place.getEntrance());
            unicastInventoryUpdate(character);
            multicastSceneUpdate(place);
            return null;
        } else {
            return "Character is not in a place";
        }
    }

    String executeHold(Character character, Object object) {
        if (!(object instanceof Item item)) {
            return "Cannot hold this object";
        }
        if (!character.getInventory().contains(item)) {
            return "Item not in inventory";
        }
        character.holdItem(item);
        unicastInventoryUpdate(character);
        return null;
    }

    String executeUse(Character character, Object object) {
        if (!(object instanceof Positionable target)) {
            return "Cannot use item on this object";
        }
        Item item = character.getHolding();
        if (item == null) {
            return "No item is being held";
        }
        Positionable modified = gameMap.getChange(target, item);
        if (modified == null) {
            return "Item cannot be used here";
        }

        Place place = character.getPlace();
        if (place != null) {
            place.addGameComponent(modified, target.getPosition());
            multicastSceneUpdate(place);
            return null;
        } else {
            return "Character is not in a place";
        }
    }

    String executeTalk(Character character, Object object) {
        if (!(object instanceof String message)) {
            return "Invalid message";
        }
        Place place = character.getPlace();
        if (place != null) {
            multicastMessageUpdate(place, character, message);
            return null;
        } else {
            return "Character is not in a place";
        }
    }

    String executeBack(Character character, Object ignored) {
        Place previousPlace = character.moveBack();
        if (previousPlace == null) {
            return "Cannot move back";
        }
        multicastSceneUpdate(previousPlace);
        return null;
    }
}
