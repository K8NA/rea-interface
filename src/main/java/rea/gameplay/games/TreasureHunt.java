package rea.gameplay.games;

import rea.components.*;
import rea.gameplay.Gameplay;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TreasureHunt implements Gameplay {

    private static final int BACKGROUND_HEIGHT = 600;
    private static final int BACKGROUND_WIDTH = 800;
    private static final Visual HOUSE_VISUAL = new Visual("images/house.jpg", BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    private static final Visual EMPTY_ROOM_VISUAL = new Visual("images/empty_room.jpg", BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    private static final int KEY_HEIGHT = 50;
    private static final int KEY_WIDTH = 70;
    private static final Visual KEY_VISUAL = new Visual("images/key.png", KEY_WIDTH, KEY_HEIGHT);
    private static final int TREASURE_HEIGHT = 200;
    private static final int TREASURE_WIDTH = 300;
    private static final Visual TREASURE_VISUAL = new Visual("images/treasure.png", TREASURE_WIDTH, TREASURE_HEIGHT);
    private static final int CHARACTER_X = 100;
    private static final int CHARACTER_Y = 100;
    private static final int HOUSE_WIDTH = 400;
    private static final int HOUSE_HEIGHT = 300;
    private static final Visual HOUSE_CLOSE_DOOR_VISUAL = new Visual("images/house_close_door.png", HOUSE_WIDTH, HOUSE_HEIGHT);
    private static final Visual HOUSE_OPEN_DOOR_VISUAL = new Visual("images/house_open_door.png", HOUSE_WIDTH, HOUSE_HEIGHT);

    public TreasureHunt() {

    }

    @Override
    public String getName() {
        return "Treasure Hunt";
    }

    @Override
    public String getDescription() {
        return "Find the treasure hidden inside the house with a closed door.";
    }

    @Override
    public GameMap makeGameMap() {
        Place startPlace = new Place(HOUSE_VISUAL, "Start", new Position(20, 60));
        Place housePlace = new Place(HOUSE_CLOSE_DOOR_VISUAL, "House", new Position(10, 90));
        new Passage(HOUSE_OPEN_DOOR_VISUAL, "open door", housePlace);
        housePlace.addGameComponent(new Item(KEY_VISUAL, "Key"), new Position(100, 100));
        housePlace.addGameComponent(new Item(TREASURE_VISUAL, "Treasure"), new Position(30, 70));


        return new GameMap(startPlace);
    }

    @Override
    public Set<Avatar> getAvatars() {
        return new HashSet<>(Arrays.asList(CartoonAvatar.values()));
    }

    @Override
    public int getMaxPlayers() {
        return 2;
    }

    @Override
    public int getMinPlayers() {
        return 1;
    }

    @Override
    public boolean gamedEnded(GameMap gameMap) {
        SimpleVisitor visitor = new SimpleVisitor();
        gameMap.accept(visitor);
        return !visitor.getPassages().isEmpty() && visitor.getItems().isEmpty();
    }
}
