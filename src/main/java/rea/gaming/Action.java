package rea.gaming;

public enum Action {
    BACK,
    DROP,
    HOLD,
    LOOK,
    MOVE,
    PICK,
    TALK,
    USE;

    public String getTitle() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    public static Action fromString(String name) {
        if (name == null) {
            throw new NullPointerException("Name is null");
        }
        for (Action action : values()) {
            if (action.name().equalsIgnoreCase(name)) {
                return action;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Action.class.getName() + "." + name);
    }

    public static Action[] getActions() {
        return Action.values();
    }
}
