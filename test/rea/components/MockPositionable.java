package rea.components;

class MockPositionable extends Positionable {
    MockPositionable(Visual visual, String description) {
        super(visual, description);
    }


    @Override
    public void accept(Visitor visitor) {

    }
}
