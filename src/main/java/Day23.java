import util.Direction;
import util.Pos;
import util.Util;

import java.util.*;

public class Day23 {

    public static void main(String[] args) {
        var board = Util.readBoard();
        Util.submitPart1(part1(board));
        Util.submitPart2(part2(board));
    }

    private static int part2(char[][] board) {
        Pos start = null;
        Pos goal = null;
        for (int x = 0; x < board[0].length; ++x) {
            if (board[0][x] == '.') {
                start = new Pos(x, 0);
            }
            if (board[board.length - 1][x] == '.') {
                goal = new Pos(x, board.length - 1);
            }
        }
        List<Pos> nodes = new ArrayList<>();
        nodes.add(start);
        nodes.add(goal);
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] != '#') {
                    var pos = new Pos(x, y);
                    int neigh = (int)Arrays.stream(Direction.values())
                            .map(pos::move)
                            .filter(newPos -> !(newPos.y < 0 || newPos.y == board.length
                                    || newPos.x < 0 || newPos.x == board[newPos.y].length
                                    || board[newPos.y][newPos.x] == '#'))
                            .count();
                    if (neigh > 2) {
                        nodes.add(pos);
                    }
                }
            }
        }
        Map<Pos, List<Edge>> graph = new HashMap<>();
        for (Pos pos : nodes) {
            var neigh = Arrays.stream(Direction.values())
                    .map(pos::move)
                    .filter(newPos -> !(newPos.y < 0 || newPos.y == board.length
                            || newPos.x < 0 || newPos.x == board[newPos.y].length
                            || board[newPos.y][newPos.x] == '#'))
                    .toList();
            for (Pos newPos : neigh) {
                var edge = calc(pos, newPos, nodes, board);
                if (edge != null) {
                    graph.putIfAbsent(edge.from, new ArrayList<>());
                    graph.get(edge.from).add(edge);
                }
            }
        }
        var que = new LinkedList<Node2>();
        que.add(new Node2(start, 0, new HashSet<>()));
        int max = 0;
        while (!que.isEmpty()) {
            var node = que.pop();
            if (Objects.equals(goal, node.pos)) {
                max = Math.max(max, node.steps);
            } else {
                var seen = new HashSet<>(node.seen);
                seen.add(node.pos);
                for (Edge edge : graph.get(node.pos)) {
                    if (!node.seen.contains(edge.to)) {
                        que.add(new Node2(edge.to, node.steps + edge.steps, seen));
                    }
                }
            }
        }
        return max;
    }

    private static int part1(char[][] board) {
        Pos start = null;
        Pos goal = null;
        for (int x = 0; x < board[0].length; ++x) {
            if (board[0][x] == '.') {
                start = new Pos(x, 0);
            }
            if (board[board.length-1][x] == '.') {
                goal = new Pos(x, board.length-1);
            }
        }
        var que = new LinkedList<Node>();
        que.add(new Node(start, 0, new HashSet<>()));
        int max = 0;
        while (!que.isEmpty()) {
            var node = que.pop();
            if (node.pos.equals(goal)) {
                max = Math.max(max, node.steps);
            } else if (board[node.pos.y][node.pos.x] == '^') {
                var newPos = node.pos.move(Direction.UP);
                if (newPos.y < 0 || newPos.y == board.length
                        || newPos.x < 0 || newPos.x == board[newPos.y].length
                        || board[newPos.y][newPos.x] == '#'
                        || node.seen.contains(newPos)) {
                    continue;
                }
                var seen = new HashSet<>(node.seen);
                seen.add(newPos);
                que.add(new Node(newPos, node.steps + 1, seen));
            } else if (board[node.pos.y][node.pos.x] == '<') {
                var newPos = node.pos.move(Direction.LEFT);
                if (newPos.y < 0 || newPos.y == board.length
                        || newPos.x < 0 || newPos.x == board[newPos.y].length
                        || board[newPos.y][newPos.x] == '#'
                        || node.seen.contains(newPos)) {
                    continue;
                }
                var seen = new HashSet<>(node.seen);
                seen.add(newPos);
                que.add(new Node(newPos, node.steps + 1, seen));
            } else if (board[node.pos.y][node.pos.x] == 'v') {
                var newPos = node.pos.move(Direction.DOWN);
                if (newPos.y < 0 || newPos.y == board.length
                        || newPos.x < 0 || newPos.x == board[newPos.y].length
                        || board[newPos.y][newPos.x] == '#'
                        || node.seen.contains(newPos)) {
                    continue;
                }
                var seen = new HashSet<>(node.seen);
                seen.add(newPos);
                que.add(new Node(newPos, node.steps + 1, seen));
            } else if (board[node.pos.y][node.pos.x] == '>') {
                var newPos = node.pos.move(Direction.RIGHT);
                if (newPos.y < 0 || newPos.y == board.length
                        || newPos.x < 0 || newPos.x == board[newPos.y].length
                        || board[newPos.y][newPos.x] == '#'
                        || node.seen.contains(newPos)) {
                    continue;
                }
                var seen = new HashSet<>(node.seen);
                seen.add(newPos);
                que.add(new Node(newPos, node.steps + 1, seen));
            } else {
                for (Direction dir : Direction.values()) {
                    var newPos = node.pos.move(dir);
                    if (newPos.y < 0 || newPos.y == board.length
                            || newPos.x < 0 || newPos.x == board[newPos.y].length
                            || board[newPos.y][newPos.x] == '#'
                            || node.seen.contains(newPos)) {
                        continue;
                    }
                    var seen = new HashSet<>(node.seen);
                    seen.add(newPos);
                    que.add(new Node(newPos, node.steps + 1, seen));
                }
            }
        }
        return max;
    }

    private static Edge calc(Pos start, Pos nextPos, List<Pos> nodes, char[][] board) {
        int steps = 1;
        Pos loop = nextPos;
        Pos last = start;
        while (!nodes.contains(loop)) {
            var finalLast = last;
            var next = Arrays.stream(Direction.values())
                    .map(loop::move)
                    .filter(newPos -> !newPos.equals(finalLast))
                    .filter(newPos -> !(newPos.y < 0 || newPos.y == board.length
                            || newPos.x < 0 || newPos.x == board[newPos.y].length
                            || board[newPos.y][newPos.x] == '#'))
                    .findFirst();
            if (next.isEmpty()) {
                return null;
            }
            last = loop;
            loop = next.get();
            ++steps;
        }
        return new Edge(start, loop, steps);
    }

    private record Node(Pos pos, int steps, Set<Pos> seen) {
    }

    private record Edge(Pos from, Pos to, int steps) {
    }

    private record Node2(Pos pos, int steps, Set<Pos> seen) {
    }
}
