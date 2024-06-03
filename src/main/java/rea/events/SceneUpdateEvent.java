package rea.events;

import rea.components.Positionable;
import rea.components.Visual;

import java.util.List;

public class SceneUpdateEvent implements UpdateEvent {
    private final Visual background;
    private final List<Positionable> positionables;

    public SceneUpdateEvent(Visual background, List<Positionable> positionables) {
        this.background = background;
        this.positionables = positionables;
    }

    public Visual getBackground() {
        return background;
    }

    public List<Positionable> getPositionables() {
        return positionables;
    }
}
