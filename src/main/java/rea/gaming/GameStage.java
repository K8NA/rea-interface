package rea.gaming;

import java.io.Serializable;

public enum GameStage implements Serializable {
    CREATED,
    PLAYING,
    ENDED;

    public static GameStage valueOfIgnoreCase(String name) {
        for (GameStage stage : values()) {
            if (stage.name().equalsIgnoreCase(name)) {
                return stage;
            }
        }
        throw new IllegalArgumentException("No enum constant " + GameStage.class.getName() + "." + name);
    }
}
