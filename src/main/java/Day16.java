import util.Direction;
import util.Pos;
import util.Util;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Day16 {

    public static void main(String[] args) {
        var board = Util.readBoard();
        Util.submitPart1(part1(board));
        Util.submitPart2(part2(board));
    }

    private static long part2(char[][] board) {
        long max = 0;
        for (int x = 0; x < board[0].length; ++x) {
            max = Math.max(max, shootBeam(board, new Beam(new Pos(x, 0), Direction.DOWN)));
            max = Math.max(max, shootBeam(board, new Beam(new Pos(x, board.length-1), Direction.UP)));
        }
        for (int y = 0; y < board.length; ++y) {
            max = Math.max(max, shootBeam(board, new Beam(new Pos(0, y), Direction.RIGHT)));
            max = Math.max(max, shootBeam(board, new Beam(new Pos(board[y].length - 1, y), Direction.LEFT)));
        }
        return max;
    }

    private static long part1(char[][] board) {
        Beam start = new Beam(new Pos(0, 0), Direction.RIGHT);
        return shootBeam(board, start);
    }

    private static long shootBeam(char[][] board, Beam start) {
        Set<Beam> seen = new HashSet<>();
        Deque<Beam> current = new LinkedList<>();
        current.add(start);
        while (!current.isEmpty()) {
            var beam = current.pop();
            if (beam.pos.y < 0 || beam.pos.x < 0 || beam.pos.y == board.length || beam.pos.x == board[beam.pos.y].length) {
                continue;
            }
            if (!seen.add(beam)) {
                continue;
            }
            char c = board[beam.pos.y][beam.pos.x];
            if (c == '.'
                    || (c == '-' && (beam.dir == Direction.LEFT || beam.dir == Direction.RIGHT))
                    || (c == '|' && (beam.dir == Direction.UP || beam.dir == Direction.DOWN))) {
                current.push(new Beam(beam.pos.move(beam.dir), beam.dir));
            } else if (c == '\\') {
                var dir = switch (beam.dir) {
                    case UP -> Direction.LEFT;
                    case LEFT -> Direction.UP;
                    case DOWN -> Direction.RIGHT;
                    case RIGHT -> Direction.DOWN;
                };
                current.push(new Beam(beam.pos.move(dir), dir));
            } else if (c == '/') {
                var dir = switch (beam.dir) {
                    case UP -> Direction.RIGHT;
                    case LEFT -> Direction.DOWN;
                    case DOWN -> Direction.LEFT;
                    case RIGHT -> Direction.UP;
                };
                current.push(new Beam(beam.pos.move(dir), dir));
            } else if (c == '|') {
                current.push(new Beam(beam.pos.move(Direction.UP), Direction.UP));
                current.push(new Beam(beam.pos.move(Direction.DOWN), Direction.DOWN));
            } else if (c == '-') {
                current.push(new Beam(beam.pos.move(Direction.LEFT), Direction.LEFT));
                current.push(new Beam(beam.pos.move(Direction.RIGHT), Direction.RIGHT));
            }
        }
        return seen.stream()
                .map(b -> b.pos)
                .distinct()
                .count();
    }

    private record Beam(Pos pos, Direction dir) {
    }
}
