package rea.components;

import java.util.Objects;

public class Visual {
    private final String pathname;
    private final int width, height;

    public Visual(String pathname, int width, int height) {
        this.pathname = pathname;
        this.width = width;
        this.height = height;
    }

    public String getPathname() {
        return pathname;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visual visual = (Visual) o;
        return width == visual.width && height == visual.height && Objects.equals(pathname, visual.pathname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathname, width, height);
    }
}
