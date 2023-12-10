import util.Direction;
import util.Pos;
import util.Util;

import java.util.*;

public class Day10 {

    public static void main(String[] args) {
        var board = Util.readBoard();
        Util.submitPart1(part1(board));
        Util.submitPart2(part2(board));
    }

    private static int part2(char[][] board) {
        Pos start = findStart(board);
        for (Direction dir : Direction.values()) {
            var newPos = start.move(dir);
            if (newPos.y >= 0 && newPos.y < board.length
                    && newPos.x >= 0 && newPos.x < board[0].length
                    && connecting(board, dir, newPos)) {
                List<Pos> seen = pipes(board, start, newPos, dir);
                if (seen != null) {
                    return calc2(start, board, seen);
                }
            }
        }
        throw new IllegalStateException();
    }

    private static int part1(char[][] board) {
        Pos start = findStart(board);
        for (Direction dir : Direction.values()) {
            var newPos = start.move(dir);
            if (newPos.y >= 0 && newPos.y < board.length
                    && newPos.x >= 0 && newPos.x < board[0].length
                    && connecting(board, dir, newPos)) {
                Integer calc = calc(board, start, newPos, dir);
                if (calc != null) {
                    return calc / 2;
                }
            }
        }
        throw new IllegalStateException();
    }

    private static int calc2(Pos start, char[][] board, List<Pos> seen) {
        var startSet = new HashSet<>();
        startSet.add(seen.get(0));
        startSet.add(seen.get(seen.size()-1));
        if (startSet.contains(start.move(Direction.UP)) && startSet.contains(start.move(Direction.DOWN))) {
            board[start.y][start.x] = '|';
        } else if (startSet.contains(start.move(Direction.UP)) && startSet.contains(start.move(Direction.LEFT))) {
            board[start.y][start.x] = 'J';
        } else if (startSet.contains(start.move(Direction.UP)) && startSet.contains(start.move(Direction.RIGHT))) {
            board[start.y][start.x] = 'L';
        } else if (startSet.contains(start.move(Direction.DOWN)) && startSet.contains(start.move(Direction.LEFT))) {
            board[start.y][start.x] = '7';
        } else if (startSet.contains(start.move(Direction.DOWN)) && startSet.contains(start.move(Direction.RIGHT))) {
            board[start.y][start.x] = 'F';
        } else if (startSet.contains(start.move(Direction.LEFT)) && startSet.contains(start.move(Direction.RIGHT))) {
            board[start.y][start.x] = '-';
        }
        char[][] bigBoard = new char[board.length*2][board[0].length*2];
        for (char[] chars : bigBoard) {
            Arrays.fill(chars, ' ');
        }
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                bigBoard[y*2][x*2] = board[y][x];
            }
        }
        var lastPos = start;
        for (Pos pos : seen) {
            if (pos.y == lastPos.y) {
                if (pos.x < lastPos.x) {
                    bigBoard[lastPos.y*2][lastPos.x*2 - 1] = '-';
                } else {
                    bigBoard[lastPos.y*2][lastPos.x*2 + 1] = '-';
                }
            } else {
                if (pos.y < lastPos.y) {
                    bigBoard[lastPos.y*2 - 1][lastPos.x*2] = '|';
                } else {
                    bigBoard[lastPos.y*2 + 1][lastPos.x*2] = '|';
                }
            }
            lastPos = pos;
        }
        int count = 0;
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                var pos = new Pos(x, y);
                if (seen.contains(pos)) {
                    continue;
                }
                if (calc2(bigBoard, pos)) {
                    ++count;
                }
            }
        }
        return count;
    }

    private static boolean calc2(char[][] board, Pos pos) {
        Set<Pos> curr = new HashSet<>();
        var queue = new LinkedList<Pos>();
        queue.add(new Pos(pos.x * 2, pos.y * 2));
        while (!queue.isEmpty()) {
            var p = queue.pop();
            if (!curr.add(p)) {
                continue;
            }
            for (Direction dir : Direction.values()) {
                var newPos = p.move(dir);
                if (newPos.y < 0 || newPos.y == board.length || newPos.x < 0 || newPos.x == board[0].length) {
                    return false;
                }
                char c = board[newPos.y][newPos.x];
                if (c == '.' || c == ' ') {
                    queue.add(newPos);
                }
            }
        }
        return true;
    }

    private static List<Pos> pipes(char[][] board, Pos start, Pos newPos, Direction dir) {
        var list = new ArrayList<Pos>();
        list.add(start);
        list.add(newPos);
        while (!newPos.equals(start)) {
            dir = newDirection(board, dir, newPos);
            if (dir == null) {
                return null;
            }
            newPos = newPos.move(dir);
            list.add(newPos);
        }
        return list;
    }

    private static Integer calc(char[][] board, Pos start, Pos newPos, Direction dir) {
        int steps = 1;
        while (!newPos.equals(start)) {
            dir = newDirection(board, dir, newPos);
            if (dir == null) {
                return null;
            }
            newPos = newPos.move(dir);
            ++steps;
        }
        return steps;
    }

    private static Direction newDirection(char[][] board, Direction dir, Pos pos) {
        char c = board[pos.y][pos.x];
        if (dir == Direction.UP) {
            if (c == '|') {
                return Direction.UP;
            } else if (c == 'F') {
                return Direction.RIGHT;
            } else if (c == '7') {
                return Direction.LEFT;
            }
        } else if (dir == Direction.LEFT) {
            if (c == '-') {
                return Direction.LEFT;
            } else if (c == 'F') {
                return Direction.DOWN;
            } else if (c == 'L') {
                return Direction.UP;
            }
        } else if (dir == Direction.DOWN) {
            if (c == '|') {
                return Direction.DOWN;
            } else if (c == 'J') {
                return Direction.LEFT;
            } else if (c == 'L') {
                return Direction.RIGHT;
            }
        } else if (dir == Direction.RIGHT) {
            if (c == '-') {
                return Direction.RIGHT;
            } else if (c == '7') {
                return Direction.DOWN;
            } else if (c == 'J') {
                return Direction.UP;
            }
        }
        return null;
    }

    private static boolean connecting(char[][] board, Direction dir, Pos pos) {
        if (board[pos.y][pos.x] == '.') {
            return false;
        }
        char c = board[pos.y][pos.x];
        return switch (dir) {
            case UP -> c == '|' || c == '7' || c == 'F';
            case LEFT -> c == '-' || c == 'J' || c == '7';
            case DOWN -> c == '|' || c == 'L' || c == 'J';
            case RIGHT -> c == '-' || c == 'L' || c == 'F';
        };
    }

    private static Pos findStart(char[][] board) {
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] == 'S') {
                    return new Pos(x, y);
                }
            }
        }
        throw new IllegalStateException("Could not find start");
    }

    private record Result(Set<Pos> handled, boolean found) {
    }
}
