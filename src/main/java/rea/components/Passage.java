package rea.components;

public class Passage extends Positionable {
    Place place;

    public Passage(Visual visual, String description, Place place) {
        super(visual, description);
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
