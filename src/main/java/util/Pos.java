package util;

import java.util.Objects;

public class Pos {
    public final int x, y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pos move(Direction dir) {
        return new Pos(x+dir.dx, y+dir.dy);
    }

    public int dist(Pos other) {
        return Math.abs(x-other.x) + Math.abs(y - other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return x == pos.x &&
                y == pos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Pos{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
