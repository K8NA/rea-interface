package rea.events;

import rea.gaming.GameInstance;

import java.util.List;

public class GamesUpdateEvent implements UpdateEvent {

    private final List<GameInstance> gamesToPlay;

    public GamesUpdateEvent(List<GameInstance> gamesToPlay) {
        this.gamesToPlay = gamesToPlay;
    }

    public List<GameInstance> getGamesToPlay() {
        return gamesToPlay;
    }
}
