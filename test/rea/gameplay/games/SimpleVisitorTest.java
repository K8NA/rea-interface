package rea.gameplay.games;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;
import rea.components.MockDAG;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static rea.components.MockDAG.*;

/**
 * Test the SimpleVisitor class
 */
class SimpleVisitorTest extends TestData {

    SimpleVisitor simpleVisitor;
    MockDAG dag;

    @BeforeEach
    void setUp() {
        simpleVisitor = new SimpleVisitor();

        dag = new MockDAG();
    }



    @Test
    void visit() {
        dag.getStartPlace().accept(simpleVisitor);

        assertAll(
                () -> assertEquals(PLACES_COUNT, simpleVisitor.getPlaces().size()),
                () -> assertEquals(ITEMS_COUNT, simpleVisitor.getItems().size()),
                () -> assertEquals(CHARACTERS_COUNT, simpleVisitor.getCharacters().size()),
                () -> assertEquals(PASSAGES_COUNT, simpleVisitor.getPassages().size())
        );

    }
}