package rea.gameplay.games;

import rea.components.Avatar;
import rea.components.Visual;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public enum CartoonAvatar implements Avatar {

    BUNNY("Bunny", new Visual("images/bunny.png", 100, 150)),
    CHICK("Chick", new Visual("images/chick.png", 100, 150)),
    LAMB("Lamb", new Visual("images/lamb.png", 100, 150));

    final String avatarName;
    final Visual avatarVisual;

    CartoonAvatar(String avatarName, Visual avatarVisual) {
        this.avatarName = avatarName;
        this.avatarVisual = avatarVisual;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        avatarName = this.avatarName;
    }

    public void setAvatarVisual(Visual avatarVisual) {
        avatarVisual = this.avatarVisual;
    }

    public ArrayList<String> getAvatars() {
        return new ArrayList<>(Arrays.asList(BUNNY.avatarName, CHICK.avatarName, LAMB.avatarName));
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
