package rea.gameplay;

import java.util.Set;

/**
 * Mock gameplau factory for testing.
 */
public class MockGameplayFactory implements AbstractGameplayFactory {


    /**
     * Get the available games. Use "Mock gameplay" for testing
     *
     * @return the available games.
     */
    @Override
    public Set<String> getAvailableGameplays() {
        return Set.of("Mock gameplay");
    }

    /**
     * Get the gameplay for a game. Use {@link MockGameplay} for testing for every game name
     *
     * @param ignore_name of the game .
     * @return the gameplay for the game.
     */
    @Override
    public Gameplay getGameplay(String ignore_name) {

        return new MockGameplay();

    }
}
