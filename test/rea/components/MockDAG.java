package rea.components;

import rea.TestData;

/**
 * A mock directed acyclic graph (DAG) for testing purposes.
 */
public class MockDAG extends TestData {

    public static final int PLACES_COUNT = 3;
    public static final int MORE_PLACES_COUNT = 2;
    public static final int ITEMS_COUNT = 5;
    public static final int CHARACTERS_COUNT = 5;
    public static final int PASSAGES_COUNT = 2;

    public static final int VISUAL_COUNT = PLACES_COUNT + 2*MORE_PLACES_COUNT + ITEMS_COUNT + PASSAGES_COUNT;
    public static final int POSITIONS_COUNT = ITEMS_COUNT + CHARACTERS_COUNT + PASSAGES_COUNT;

    Place startPlace;

    TestData.MockPool<Place> morePlaces;
    TestData.MockPool<Passage> morePassages;

    public MockDAG() {
        TestData.MockPool<Visual> visuals = new TestData.MockPool<>(i -> new Visual(TestData.PATHNAMES[ i % TestData.PATHNAMES.length], TestData.WIDTH, TestData.HEIGHT), VISUAL_COUNT);
        TestData.MockPool<Place> places = new TestData.MockPool<>(i -> new Place(visuals.get(i), "Place #"+i, null), PLACES_COUNT);
        TestData.MockPool<Item> items = new TestData.MockPool<>(i -> new Item(visuals.get(PLACES_COUNT+i), "Item n#"+i+" description"), ITEMS_COUNT);
        TestData.MockPool<Character> characters = new TestData.MockPool<>(i -> new Character("Character #"+i, MockAvatar.getRandomAvatar()), CHARACTERS_COUNT);
        TestData.MockPool<Passage> passages  = new TestData.MockPool<>(i -> new Passage(visuals.get(PLACES_COUNT+ITEMS_COUNT+i), "Passage #"+i, places.get(i+1)), PASSAGES_COUNT);
        TestData.MockPool<Position> positions = new TestData.MockPool<>(i -> new Position(i, i), POSITIONS_COUNT);

        morePlaces = new TestData.MockPool<>(i -> new Place(visuals.get(PLACES_COUNT+ITEMS_COUNT+PASSAGES_COUNT+i), "Other Place #"+i, null), MORE_PLACES_COUNT);
        morePassages = new TestData.MockPool<>(i -> new Passage(visuals.get(PLACES_COUNT+ITEMS_COUNT+PASSAGES_COUNT+MORE_PLACES_COUNT+i), "Other Passage #"+i, morePlaces.get(i)), MORE_PLACES_COUNT);

        startPlace = places.get(0)
                .addGameComponent(items.get(0),positions.get(0))
                .addGameComponent(items.get(1),positions.get(1))
                .addGameComponent(characters.get(0),positions.get(2))
                .addGameComponent(characters.get(1),positions.get(3))
                .addGameComponent(passages.get(0),positions.get(4));

        places.get(1)
                .addGameComponent(items.get(2),positions.get(5))
                .addGameComponent(items.get(3),positions.get(6))
                .addGameComponent(characters.get(2),positions.get(7))
                .addGameComponent(characters.get(3),positions.get(8))
                .addGameComponent(passages.get(1),positions.get(9));

        places.get(2)
                .addGameComponent(items.get(4),positions.get(10))
                .addGameComponent(characters.get(4),positions.get(11));

    }

    public Place getStartPlace() {
        return startPlace;
    }

    public TestData.MockPool<Place> getMorePlaces() {
        return morePlaces;
    }

    public TestData.MockPool<Passage> getMorePassages() {
        return morePassages;
    }
}
