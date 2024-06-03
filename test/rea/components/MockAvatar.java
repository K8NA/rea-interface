package rea.components;

/**
 *  Mock avatar for testing.
 */
public enum MockAvatar implements Avatar {
    AVATAR1("Avatar1", "Avatar1.png", 200, 100),
    AVATAR2("Avatar2", "Avatar2.png", 200, 100),
    AVATAR3("Avatar3", "Avatar3.png", 200, 100);

    final String avatarName;
    final Visual avatarVisual;

    MockAvatar(String avatarName, String pathname,int width, int height) {
        this.avatarName = avatarName;
        this.avatarVisual = new Visual(pathname,width,height);
    }

    @Override
    public String getAvatarName() {
        return avatarName;
    }

    @Override
    public Visual getAvatarVisual() {
        return avatarVisual;
    }

    /**
     *  Get a random avatar.
     * @return a random avatar
     */
    static public Avatar getRandomAvatar() {
        return values()[(int) (Math.random() * values().length)];
    }
}
