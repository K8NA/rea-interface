package rea.components;

public abstract class Component implements Element {

    private final String description;
    private final Visual visual;

    public Component(Visual visual, String description) {
        this.visual = visual;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Visual getVisual() {
        return visual;
    }

    @Override
    public String toString() {
        return description;
    }
}


