package rea.gameplay;

import rea.components.Avatar;
import rea.components.GameMap;
import rea.components.MockAvatar;
import rea.components.Place;
import rea.components.Position;
import rea.components.Visual;

import java.util.Set;

/**
 * Mock gameplay for testing.
 */
public class MockGameplay implements Gameplay {

    int maxPlayers = 1;

    @Override
    public String getName() {
        return "Mock gameplay";
    }

    @Override
    public String getDescription() {
        return "Mock gameplay description";
    }

    @Override
    public GameMap makeGameMap() {
        Visual visual = new Visual("/images/background.jpg", 800, 600);
        Position entrance = new Position(100, 4000);
        Place startPlace = new Place(visual, "Start place", entrance);

        return new GameMap(startPlace);
    }

    @Override
    public Set<Avatar> getAvatars() {
        return Set.of(MockAvatar.values());
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
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
