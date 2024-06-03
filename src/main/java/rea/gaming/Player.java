package rea.gaming;

import rea.components.Character;

import java.util.Objects;

public class Player {

    Character character;

    public Player(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(character, player.character);
    }

    @Override
    public int hashCode() {
        return Objects.hash(character);
    }
}
