import util.Direction;
import util.Pos;
import util.Util;

import java.util.*;

public class Day17 {

    public static void main(String[] args) {
        var board = Util.readIntBoard();
        Util.submitPart1(part1(board));
        Util.submitPart2(part2(board));
    }

    private static long part2(int[][] board) {
        long best = Long.MAX_VALUE;
        var seen = new HashMap<Node, Long>();
        var que = new LinkedList<Node>();
        que.add(new Node(new Pos(0, 0), null, 0, 0));
        while (!que.isEmpty()) {
            var node = que.pop();
            var prev = seen.get(node);
            if (prev != null && prev <= node.loss) {
                continue;
            }
            seen.put(node, node.loss);
            if (node.pos.y == board.length - 1 && node.pos.x == board[0].length - 1) {
                best = Math.min(best, node.loss);
            }
            for (Direction dir : Direction.values()) {
                if (node.dir != null) {
                    if (dir == node.dir.reverse()) {
                        continue;
                    }
                    if (node.same < 4 && node.dir != dir) {
                        continue;
                    }
                    if (node.same == 10 && node.dir == dir) {
                        continue;
                    }
                }
                var newPos = node.pos.move(dir);
                if (newPos.x < 0 || newPos.y < 0 || newPos.y == board.length || newPos.x == board[newPos.y].length) {
                    continue;
                }
                que.add(new Node(newPos, dir, dir == node.dir ? node.same + 1 : 1, node.loss + board[newPos.y][newPos.x]));
            }
        }
        return best;
    }

    private static long part1(int[][] board) {
        long best = Long.MAX_VALUE;
        var seen = new HashMap<Node, Long>();
        var que = new LinkedList<Node>();
        que.add(new Node(new Pos(0, 0), null, 0, 0));
        while (!que.isEmpty()) {
            var node = que.pop();
            var prev = seen.get(node);
            if (prev != null && prev <= node.loss) {
                continue;
            }
            seen.put(node, node.loss);
            if (node.pos.y == board.length - 1 && node.pos.x == board[0].length - 1) {
                best = Math.min(best, node.loss);
            }
            for (Direction dir : Direction.values()) {
                if (node.dir != null && (dir == node.dir.reverse() || (dir == node.dir && node.same == 3))) {
                    continue;
                }
                var newPos = node.pos.move(dir);
                if (newPos.x < 0 || newPos.y < 0 || newPos.y == board.length || newPos.x == board[newPos.y].length) {
                    continue;
                }
                que.add(new Node(newPos, dir, dir == node.dir ? node.same + 1 : 1, node.loss + board[newPos.y][newPos.x]));
            }
        }
        return best;
    }

    private record Node(Pos pos, Direction dir, int same, long loss) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return same == node.same && Objects.equals(pos, node.pos) && dir == node.dir;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, dir, same);
        }
    }
}
