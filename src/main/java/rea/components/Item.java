package rea.components;

import java.util.Objects;

public class Item extends Positionable {

    Visual visual;
    String description;

    boolean pickable;
    boolean reusable;

    public Item(Visual visual, String description) {
        super(visual, description);
        pickable = true;
        reusable = false;
    }

    public boolean isPickable() {
        return pickable;
    }

    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }

    public boolean isReusable() {
        return reusable;
    }

    public void setReusable(boolean reusable) {
        this.reusable = reusable;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return pickable == item.pickable &&
                reusable == item.reusable &&
                Objects.equals(visual, item.visual) &&
                Objects.equals(description, item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visual, description);
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
