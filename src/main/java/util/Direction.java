package util;

public enum Direction {
    UP(-1, 0),
    LEFT(0, -1),
    DOWN(1, 0),
    RIGHT(0, 1);

    public final int dy, dx;

    Direction(int dy, int dx) {
        this.dy = dy;
        this.dx = dx;
    }

    public Direction turnLeft() {
        return switch (this) {
            case UP -> LEFT;
            case LEFT -> DOWN;
            case DOWN -> RIGHT;
            case RIGHT -> UP;
        };
    }

    public Direction turnRight() {
        return switch (this) {
            case UP -> RIGHT;
            case LEFT -> UP;
            case DOWN -> LEFT;
            case RIGHT -> DOWN;
        };
    }

    public Direction reverse() {
        return switch (this) {
            case UP -> Direction.DOWN;
            case LEFT -> Direction.RIGHT;
            case DOWN -> Direction.UP;
            case RIGHT -> Direction.LEFT;
        };
    }
}
