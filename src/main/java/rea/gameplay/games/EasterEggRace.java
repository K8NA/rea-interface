package rea.gameplay.games;

import rea.components.*;
import rea.gameplay.Gameplay;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EasterEggRace implements Gameplay {

    private static final int BACKGROUND_HEIGHT = 600;
    private static final int BACKGROUND_WIDTH = 800;
    private static final int EGG_HEIGHT = 70;
    private static final int EGG_WIDTH = 50;
    private static final Visual[] EGG_VISUAL = {
            new Visual("some/path/egg1.png", EGG_WIDTH, EGG_HEIGHT),
            new Visual("some/path/egg2.png", EGG_WIDTH, EGG_HEIGHT),
            new Visual("some/path/egg3.png", EGG_WIDTH, EGG_HEIGHT)
    };

    private static final Visual LAWN_VISUAL = new Visual("some/path/lawn.png", BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

    public EasterEggRace() {
    }

    @Override
    public String getName() {
        return "Easter Egg Race";
    }

    @Override
    public String getDescription() {
        return "Collect all the games in the lawn";
    }

    @Override
    public GameMap makeGameMap() {
        Place startPlace = new Place(LAWN_VISUAL, "lawn", new Position(40, 80));
        return new GameMap(startPlace);
    }

    @Override
    public Set<Avatar> getAvatars() {
        return new HashSet<>(Arrays.asList(CartoonAvatar.values()));
    }

    @Override
    public int getMaxPlayers() {
        return 1;
    }

    @Override
    public int getMinPlayers() {
        return 1;
    }

    @Override
    public boolean gamedEnded(GameMap gameMap) {
        return false;
    }

}
