package rea.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static rea.components.MockDAG.*;
import static rea.components.MockDAG.PASSAGES_COUNT;

class GameMapTest extends TestData {

    GameMap gameMap;

    MockDAG dag;
    @BeforeEach
    void setUp() {
        dag = new MockDAG();

        gameMap = new GameMap(dag.getStartPlace());
    }

    @Test
    void getStartPlace() {
        assertEquals(dag.getStartPlace(), gameMap.getStartPlace());
    }

    @Test
    void setStartPlace() {
        Place otherPlace = dag.getMorePlaces().getFirst();

        gameMap.setStartPlace(otherPlace);
        assertEquals(otherPlace, gameMap.getStartPlace());
    }


    @Test
    void change() {
        var startPlace = gameMap.getStartPlace();
        var itemIterator = startPlace.getItems().iterator();
        var modifiable = itemIterator.next();
        var tool = itemIterator.next();
        var modified = dag.getMorePassages().getFirst();

        gameMap.defineChange(modifiable, tool, modified);

        assertEquals(modified, gameMap.getChange(modifiable,tool));
    }

    @Test
    void visitMap() {
        var mockVisitor = new MockVisitor();

        gameMap.visitMap( mockVisitor );

        assertAll(
                () -> assertEquals(PLACES_COUNT, mockVisitor.places.size()),
                () -> assertEquals(ITEMS_COUNT, mockVisitor.items.size()),
                () -> assertEquals(CHARACTERS_COUNT, mockVisitor.characters.size()),
                () -> assertEquals(PASSAGES_COUNT, mockVisitor.passages.size())
        );
    }
}