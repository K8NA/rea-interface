package rea;

import rea.gameplay.Gameplay;
import rea.gameplay.games.EasterEggRace;
import rea.gameplay.games.TreasureHunt;
import rea.gaming.GameInstance;
import rea.events.GamesUpdateEvent;
import rea.events.UpdateListener;
import rea.gameplay.AbstractGameplayFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public final class Manager {

    static final long KEEP_AFTER_END = 300000L;
    long keepAfterEnd;
    private static Manager gameInstance;
    private static AbstractGameplayFactory gameplayFactory;
    private final Set<UpdateListener<GamesUpdateEvent>> listeners;
    private final List<GameInstance> gameInstances;

    private Manager() {
        keepAfterEnd = 0L;
        listeners = new HashSet<>();
        gameInstances = new ArrayList<>();
        gameplayFactory = new AbstractGameplayFactory() {
            @Override
            public Set<String> getAvailableGameplays() {
                return null;
            }
            @Override
            public Gameplay getGameplay(String name) {
                return null;
            }
        };
    }

    public static Manager getInstance() throws ReaException {
        if (gameInstance == null) {
            try {
                gameInstance = new Manager();
            } catch (Exception e) {
                throw new ReaException("Error creating Manager instance: " + e.getMessage());
            }
        }
        return gameInstance;
    }

    public void setGameplayFactory(AbstractGameplayFactory gameplayFactory) {
        Manager.gameplayFactory = gameplayFactory;
    }

    public AbstractGameplayFactory getGameplayFactory() {
        return gameplayFactory;
    }

    void reset() throws ReaException {
        gameInstances.clear();
        listeners.clear();
        gameInstance = null;

        try {
            gameInstance = getInstance();
        } catch (Exception e) {
            throw new ReaException("Failed to create a new instance of the gameplay factory", e);
        }
    }

    public void addGamesUpdateListener(UpdateListener<GamesUpdateEvent> listener) {
        listeners.add(listener);
    }

    public Set<String> getAvailableGames() {
        if (gameplayFactory != null) {
            return gameplayFactory.getAvailableGameplays();
        } else {
            return new HashSet<>();
        }
    }

    public GameInstance createGameInstance(String gameName) {
        GameInstance game = null;

        if (gameName.equals("Treasure Hunt"))
            game = new GameInstance(new TreasureHunt());
        if (gameName.equals("Easter Egg Race"))
            game = new GameInstance(new EasterEggRace());

        gameInstances.add(game);
        recycleGameInstances();
        broadcastGamesUpdate();

        return game;
    }

    public void deleteGameInstance(GameInstance gameInstance) {
        if (gameInstance.canDelete()) {
            gameInstances.remove(gameInstance);
            broadcastGamesUpdate();
        }
    }

    public List<GameInstance> getGameInstances() {
        return gameInstances;
    }

    public void broadcastGamesUpdate() {
        GamesUpdateEvent event = new GamesUpdateEvent(gameInstances);
        for (UpdateListener<GamesUpdateEvent> listener : listeners) {
            listener.onUpdate(event);
        }
    }

    public List<GameInstance> getGamesInstancesAboutToStart() {
        List<GameInstance> gamesAboutToStart = new ArrayList<>();
        for (GameInstance gameInstance : gameInstances)
            if (gameInstance.canStart())
                gamesAboutToStart.add(gameInstance);
        return gamesAboutToStart;
    }

    public static long getKeepAfterEnd() {
        return gameInstance.keepAfterEnd;
    }

    public static void setKeepAfterEnd(long keepAfterEnd) {
        gameInstance.keepAfterEnd = keepAfterEnd;
    }

    public void recycleGameInstances() {
        gameInstances.removeIf(GameInstance::isComplete);
    }

}
