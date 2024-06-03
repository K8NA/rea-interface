package rea.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rea.TestData;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the Component class
 */
class ComponentTest extends TestData {

    static final Visual VISUAL = new Visual(PATHNAME,WIDTH,HEIGHT);

    static class MyComponent extends Component {
        MyComponent(Visual visual, String description) {
            super(visual, description);
        }

        @Override
        public void accept(Visitor visitor) {

        }
    }

    MyComponent component;

    @BeforeEach
    void setUp() {
        component = new MyComponent(VISUAL, DESCRITION);
    }

    /**
     * Test the visual of the component
     */
    @Test
    void getVisual() {
        assertEquals(VISUAL, component.getVisual());
    }

    /**
     * Test the description of the component
     */
    @Test
    void getDescription() {
        assertEquals(DESCRITION, component.getDescription());
    }

    /**
     * Test the string representation of the component
     */
    @Test
    void testToString() {
        assertEquals(DESCRITION, component.toString());
    }
}