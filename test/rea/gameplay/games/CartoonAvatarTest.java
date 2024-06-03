package rea.gameplay.games;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *  Test the CartoonAvatar enum
 */
class CartoonAvatarTest {


    /**
     * The avatar name is the same as the enum name in lowercase
     * but with the first letter in upper case
     */
    @ParameterizedTest
    @EnumSource(CartoonAvatar.class)
    void getAvatarName(CartoonAvatar avatar) {
        assertEquals(avatar.name(), avatar.getAvatarName().toUpperCase());
        assertEquals(avatar.name().charAt(0), avatar.getAvatarName().charAt(0));
        assertEquals(avatar.name().substring(1).toLowerCase(), avatar.getAvatarName().substring(1));
    }

    /**
     * The avatar visual is not null
     */
    @ParameterizedTest
    @EnumSource(CartoonAvatar.class)
    void getAvatarVisual(CartoonAvatar avatar) {
        assertNotNull(avatar.getAvatarVisual());
    }
}