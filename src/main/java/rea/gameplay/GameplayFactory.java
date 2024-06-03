package rea.gameplay;

import rea.ReaException;
import rea.gameplay.games.TreasureHunt;

import java.nio.file.Path;
import java.util.*;

public class GameplayFactory implements AbstractGameplayFactory {

    private static final String GAMEPLAY_PACKAGE = "rea.gameplay.games";

    private static final ClassLoader LOADER = GameplayFactory.class.getClassLoader();

    public GameplayFactory() throws ReaException {
    }

    public GameplayFactory(String gameplayPackage) throws ReaException {

    }

    Map<String, Gameplay> collectGameplayInPackage(String gameplayPackage)
            throws ReaException {

        return null;
    }

    String getClassName(Path path) {
        return null;
    }


    Gameplay getGameplayInstance(String classname) {
        return null;
    }


    public Set<String> getAvailableGameplays() {
        HashSet<String> availableGames = new HashSet<>();
        availableGames.add("Treasure Hunt");
        availableGames.add("Easter Egg Race");

        return availableGames;
    }

    @Override
    public Gameplay getGameplay(String name) {
        return null;
    }
}
