package rea.gameplay;

import java.util.Set;

public interface AbstractGameplayFactory {
    Set<String> getAvailableGameplays();
    Gameplay getGameplay(String name);
}
