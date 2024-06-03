    package rea.gameplay;

    import rea.components.Avatar;
    import rea.components.GameMap;

    import java.util.Set;

    public interface Gameplay {
        String getName();
        String getDescription();
        GameMap makeGameMap();
        Set<Avatar> getAvatars();
        int getMaxPlayers();
        int getMinPlayers();
        boolean gamedEnded(GameMap gameMap);
    }
