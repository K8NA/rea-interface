package rea.gaming;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import rea.TestData;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest extends TestData {

    /**
     * Check action titles.
     * The title must be equal the label ignoring case.
     * The first letter must be uppercase and the following lowercase.
     *
     * @param action enumerated value
     */
    @ParameterizedTest
    @EnumSource(Action.class)
    void getTitle(Action action) {
        var label = action.toString();
        var title = action.getTitle();

        assertEquals(label,title.toUpperCase());
        assertEquals(label.substring(0,1).toUpperCase(),title.substring(0,1));
        assertEquals(label.substring(1).toLowerCase(),title.substring(1));

    }

    @ParameterizedTest
    @ValueSource(strings = {"move", "pick", "drop", "use", "look"})
    void valueOf(String value) {

        assertNotNull( Action.valueOf(value.toUpperCase() ));
    }

    @ParameterizedTest
    @EnumSource(Action.class)
    void values(Action value) {
        var values = Set.of(Action.values());

        assertTrue(values.contains(value));
    }
}