package rea.gameplay.games;

import rea.components.Avatar;
import rea.components.Visual;

public enum CartoonAvatar implements Avatar {

    BUNNY("Bunny", new Visual("some/path/bunny.png", 100, 150)),
    CHICK("Chick", new Visual("some/path/chick.png", 100, 150)),
    LAMB("Lamb", new Visual("some/path/lamb.png", 100, 150));

    final String avatarName;
    final Visual avatarVisual;

    CartoonAvatar(String avatarName, Visual avatarVisual) {
        this.avatarName = avatarName;
        this.avatarVisual = avatarVisual;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public Visual getAvatarVisual() {
        return avatarVisual;
    }

    public static CartoonAvatar valueOfIgnoreCase(String name) {
        for (CartoonAvatar avatar : values()) {
            if (avatar.name().equalsIgnoreCase(name)) {
                return avatar;
            }
        }
        throw new IllegalArgumentException("No enum constant " + CartoonAvatar.class.getName() + "." + name);
    }
}
