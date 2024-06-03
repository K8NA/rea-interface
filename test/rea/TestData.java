package rea;

import java.util.*;
import java.util.function.Function;

public class TestData {

    protected static final String NAMES[] = {"A random name", "Another random name", "Yet another random name"};

    protected static final String NAME = NAMES[0];

    protected static final String PATHNAMES[] = {"images/some_image.png", "images/another_image.png", "images/yet_another_image.png"};

    protected static final String PATHNAME = PATHNAMES[0];
    protected static final String BACKGROUND_IMAGE = "images/background.png";

    protected static final int WIDTH = 800;

    protected static final int HEIGHT = 600;

    protected static final String DESCRITIONS[] = {"A random description", "Another random description", "Yet another random description"};

    protected static final String DESCRITION = DESCRITIONS[0];

    protected static final String MESSAGE = "A random message";

    protected static final int X = 110;
    protected static final int Y = 220;

    protected static final int NEW_X = 330;
    protected static final int NEW_Y = 440;


    public class MockPool<T> extends ArrayList<T> {
        final int COUNT = 3;

        public MockPool(Function<Integer, T> create) {
            init(create, COUNT);
        }

        public MockPool(Function<Integer, T> create, int count) {
            init(create, count);
        }

        void init(Function<Integer, T> create, int count){
            for (int i = 0; i < count; i++) {
                add(create.apply(i));
            }
        }

        /**
         * A view of this list in reverse order.
         * This method should be in List on Java 21 ...
         * @return A view of this list in reverse order.
         */
        public List<T> reversed() {
            List<T> reversed = new ArrayList<>(this);
            Collections.reverse(reversed);
            return reversed;
        }

        /**
         * A view of this list as a Set.
         * @return {@linkplain} set
         */
        public Set<T> asSet() {
            return new HashSet<>(this);
        }
    }

}
