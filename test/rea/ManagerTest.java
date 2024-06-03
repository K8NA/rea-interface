package rea;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rea.gameplay.MockGameplayFactory;

/**
 * Test for {@link Manager}
 *
 *
 */
class ManagerTest {

    MockGameplayFactory gameplayFactory = new MockGameplayFactory();

    static Manager pool;
    @BeforeAll
    static void setUpClass() throws ReaException {

    }

    @BeforeEach
    void setUp() throws ReaException {

    }
    @Test
    void getInstance() {

    }

    /**
     * There is a default gameplay factory
     */
    @Test
    void getGameplayFactory() {

    }

    /**
     * Gameplay factory can be changed
     */
    @Test
    void setGameplayFactory() {

    }

    /**
     * Check available game names in mock gameplay factory
     */
    @Test
    void getAvailableGames_mock_gameplay_factory() {

    }


    /**
     * Check available game names in default gameplay factory,
     * that should include Easter Egg race and Treasure Hunt
     */
    @Test
    void getAvailableGames_default_gameplay_factory() {

    }


    /**
     * Create a game instance and it is available
     */
    @Test
    void createGameInstance() {
    }

    /**
     * Delete a game instance and it is not available anymore.
     * An event notifying this should be broadcast.
     */
    @Test
    void deleteGameInstance() {
    }

    /**
     * Get game instances about to start
     */
    @Test
    void getGamesInstancesAboutToStart() {
    }

    /**
     * By default, the time to keep a game after it ends is 5 minutes
     */
    @Test
    void getKeepAfterEnd() {

    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 2, 3, 4, 5, 10, 100, 1000})
    void setKeepAfterEnd(long keepAfterEnd) {

    }

    /**
     * Check if the game instances are recycled.
     * Create a game instance, start it, end it and check if it is removed from the pool
     * after the set delay
     * @throws InterruptedException might happen on sleep
     */
    @ParameterizedTest
    @ValueSource(longs = {0, 1, 2, 3, 4, 5, 10, 100})
    void recycleGameInstances(long delay) throws InterruptedException {

    }
}