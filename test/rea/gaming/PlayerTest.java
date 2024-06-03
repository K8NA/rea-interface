package rea.gaming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;
import rea.components.Character;
import rea.components.MockAvatar;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest extends TestData {

    Character character;
    Player player;

    @BeforeEach
    void setUp() {
        var avatars = MockAvatar.values();
        var players = new TestData.MockPool<Character>(i -> new Character("Character"+i, avatars[i]), avatars.length);

        character = players.getFirst();
        player = new Player(character);
    }

    @Test
    void getPlayer() {
        assertEquals(character, player.getCharacter());
    }

    @Test
    void testEquals()  {
        var other = new Player(character);
        assertEquals(player, other);
    }

    @Test
    void testHashCode() {
        var other = new Player(character);
        assertEquals(player.hashCode(), other.hashCode());
    }
}