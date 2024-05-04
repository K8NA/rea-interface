package rea.gaming;

import rea.components.Avatar;
import rea.components.Character;
import rea.components.GameMap;
import rea.events.SceneUpdateEvent;
import rea.gameplay.Gameplay;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GameInstance extends GameEventSource {
    private final Gameplay gameplay;
    private final GameMap gameMap;
    private final Set<Player> players;
    private final Date playingSince;
    private Date playingUntil;

    public GameInstance(Gameplay gameplay) {
        super();
        this.gameplay = gameplay;
        this.gameMap = gameplay.makeGameMap();
        this.players = new HashSet<>();
        this.playingSince = new Date();
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public String getName() {
        return gameplay.getName();
    }

    public int getPlayerCount() {
        return players.size();
    }

    public GameStage getCurrentStage() {
        return GameStage.PLAYING;
    }

    public Player addPlayer(Character character) {
        if (getCurrentStage() != GameStage.CREATED || getPlayerCount() >= gameplay.getMaxPlayers()) {
            return null;
        }
        Player player = new Player(character);
        players.add(player);
        broadcastGameChanged(this);
        return player;
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
        if (!canStart()) {
            throw new IllegalStateException("Game cannot start");
        }

    }

    public void endPlayingGame() {
        if (getCurrentStage() != GameStage.PLAYING) {
            throw new IllegalStateException("Game is not currently playing");
        }
        playingUntil = new Date();
    }

    public String executeCommand(Player player, Action action, Object object) {
        if (getCurrentStage() != GameStage.PLAYING) {
            throw new IllegalStateException("Game is not currently playing");
        }
        if (!players.contains(player)) {
            throw new IllegalArgumentException("Player is not in the game");
        }
        // Logic to execute the command
        return null; // Successful execution
    }

    String executeBack(Character character, Object ignored) {
        return null;
    }

    String executeDrop(Character character, Object object) {
        return null;
    }

    String executeHold(Character character, Object object) {
        return null;
    }

    String executeLook(Character character, Object object) {
        return null;
    }

    String executeMove(Character character, Object object) {
        return null;
    }

    String executePick(Character character, Object object) {
        return null;
    }

    String executeTalk(Character character, Object object) {
        return null;
    }

    String executeUse(Character character, Object object) {
        return null;
    }




}
