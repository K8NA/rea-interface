package rea.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rea.ReaException;

import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link GameplayFactory}.
 */
class GameplayFactoryTest {

    public static final String REA_GAMES = "rea.gameplay";
    GameplayFactory gameplayFactory;

    @BeforeEach
    void setUp() throws ReaException {
        gameplayFactory = new GameplayFactory(REA_GAMES);
    }


    static final Pattern RE = Pattern.compile("^(.*)\\.[^.]*$");

    @ParameterizedTest
    @CsvSource({
            "src/main/java/rea/games/MockGameplay.class, rea.gameplay.MockGameplay",
            "src/main/java/rea/gameplay/games/EasterEggRace.class, rea.gameplay.games.EasterEggRace"
    })
    void getClassName( String pathname, String expected) throws ReaException {
        Path path = Path.of(pathname);
        Matcher matcher = RE.matcher(expected);

        assertTrue(matcher.matches());

        String gameplayPackage = matcher.group(1);
        var packageGameplayFactory = new GameplayFactory(gameplayPackage);

        String actual = packageGameplayFactory.getClassName(path);
        assertEquals(expected,actual);
    }

    @Test
    void getAvailableGames() {
        Set<String> expected = Set.of("Mock gameplay");
        Set<String> actual = gameplayFactory.getAvailableGameplays();

        assertEquals(expected,actual);
    }

    @Test
    void getGameplay() {
        String name = "Mock gameplay";

        Gameplay gameplay = gameplayFactory.getGameplay(name);

        assertEquals(name, gameplay.getName());
    }

    /**
     * Test that {@code getGameplay()} returns {@code null} when the game name is unknown.
     */
    @Test
    void getGameplay_unknown_game_name() {

        assertNull( gameplayFactory.getGameplay("What game?!"));
    }

    /**
     * Test that {@code getGameplayInstance()} can be instantiated with a valid class.
     */
    @Test
    void getGameplayInstance_valid_gameplay() {
        Gameplay gameplay = gameplayFactory.getGameplayInstance("rea.gameplay.games.EasterEggRace");

        assertNotNull(gameplay);
    }

    /**
     * Test that {@code getGameplayInstance()} returns {@code null} when the class is missing.
     */
    @Test
    void getGameplayInstance_missing_gameplay() {
        Gameplay gameplay = gameplayFactory.getGameplayInstance("rea.gameplay.games.MissingGameplay");

        assertNull(gameplay);
    }

    /**
     * Test that {@code getGameplayInstance()} returns {@code null} when the class is invalid.
     */
    @Test
    void getGameplayInstance_invalid_gameplay() {
        assertNull( gameplayFactory.getGameplayInstance("rea.gameplay.GameplayFactory") );
    }

    @Test
    void collectGameplaysInPackage() throws ReaException {
        var expectedName = "Mock gameplay";

        var map = gameplayFactory.collectGameplayInPackage(REA_GAMES);

        assertEquals(1, map.size());
        assertTrue(map.containsKey(expectedName));
    }

    @Test
    void collectGameplayInPackage_invalid() {
        var buggyGameplay = "rea.gameplay.BuggyGameplay";

        assertThrows(ReaException.class, () ->
            gameplayFactory.collectGameplayInPackage(buggyGameplay)
        );
    }


}