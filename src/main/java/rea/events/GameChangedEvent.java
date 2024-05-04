package rea.events;

import rea.gaming.GameInstance;

public class GameChangedEvent implements UpdateEvent {

    private final GameInstance gameInstance;

    public GameChangedEvent(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public GameInstance getGameInstance() {
        return gameInstance;
    }
}
