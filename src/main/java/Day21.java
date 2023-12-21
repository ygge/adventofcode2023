import util.Direction;
import util.Pos;
import util.Util;

import java.util.*;

public class Day21 {

    public static void main(String[] args) {
        Util.verifySubmission();
        var board = Util.readBoard();
        //Util.submitPart1(part1(board));
        Util.submitPart2(part2(board));
    }

    private static long part2(char[][] board) {
        Pos start = findStart(board);
        Set<Node> seen = new HashSet<>();
        var que = new LinkedList<Node>();
        que.add(new Node(start, 0));
        int last = 0;
        int steps = 0;
        while (!que.isEmpty()) {
            var node = que.pop();
            if (node.steps != steps) {
                var count = (que.size() + 1);
                System.out.println(node.steps + ": " + count + " " + (count-last));
                steps = node.steps;
                last = count;
            }
            for (Direction dir : Direction.values()) {
                var newPos = node.pos.move(dir);
                var newNode = new Node(newPos, node.steps + 1);
                if (seen.add(newNode) && openTerrain(board, newPos)) {
                    que.add(newNode);
                }
            }
        }
        return -1;
    }

    private static long part1(char[][] board) {
        Pos start = findStart(board);
        Set<Node> seen = new HashSet<>();
        Set<Pos> goals = new HashSet<>();
        var que = new LinkedList<Node>();
        que.add(new Node(start, 0));
        while (!que.isEmpty()) {
            var node = que.pop();
            if (!seen.add(node)) {
                continue;
            }
            for (Direction dir : Direction.values()) {
                var newPos = node.pos.move(dir);
                if (newPos.y < board.length && newPos.y >= 0
                    && newPos.x < board[newPos.y].length && newPos.x >= 0
                    && board[newPos.x][newPos.y] == '.') {
                    if (node.steps + 1 == 10) {
                        goals.add(newPos);
                    } else {
                        que.add(new Node(newPos, node.steps + 1));
                    }
                }
            }
        }
        return goals.size();
    }

    private static boolean openTerrain(char[][] board, Pos pos) {
        Pos loop = pos;
        while (loop.y < 0) {
            loop = new Pos(loop.x, loop.y + board.length);
        }
        while (loop.x < 0) {
            loop = new Pos(loop.x + board[0].length, loop.y);
        }
        while (loop.y >= board.length) {
            loop = new Pos(loop.x, loop.y - board.length);
        }
        while (loop.x >= board[0].length) {
            loop = new Pos(loop.x - board[0].length, loop.y);
        }
        return board[loop.x][loop.y] == '.';
    }

    private static Pos findStart(char[][] board) {
        Pos start = null;
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] == 'S') {
                    start = new Pos(x, y);
                    board[y][x] = '.';
                }
            }
        }
        if (start == null) {
            throw new IllegalStateException();
        }
        return start;
    }

    private record Node(Pos pos, int steps) {
    }
}
