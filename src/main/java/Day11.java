import util.Pos;
import util.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day11 {

    public static void main(String[] args) {
        var board = Util.readBoard();
        Util.submitPart1(solve(board, 1));
        Util.submitPart2(solve(board, 999999));
    }

    private static long solve(char[][] board, int extra) {
        Set<Integer> rows = new HashSet<>();
        List<Pos> galaxies = new ArrayList<>();
        for (int y = 0; y < board.length; ++y) {
            boolean empty = true;
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] != '.') {
                    galaxies.add(new Pos(x, y));
                    empty = false;
                }
            }
            if (empty) {
                rows.add(y);
            }
        }
        Set<Integer> cols = new HashSet<>();
        for (int x = 0; x < board[0].length; ++x) {
            boolean empty = true;
            for (char[] chars : board) {
                if (chars[x] != '.') {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                cols.add(x);
            }
        }
        int sum = 0;
        for (int i = 0; i < galaxies.size(); ++i) {
            for (int j = i+1; j < galaxies.size(); ++j) {
                sum += dist(galaxies.get(i), galaxies.get(j), rows, cols, extra);
            }
        }
        return sum;
    }

    private static int dist(Pos from, Pos to, Set<Integer> rows, Set<Integer> cols, int extra) {
        int dist = from.dist(to);
        for (int y = Math.min(from.y, to.y); y < Math.max(from.y, to.y); ++y) {
            if (rows.contains(y)) {
                dist += extra;
            }
        }
        for (int x = Math.min(from.x, to.x); x < Math.max(from.x, to.x); ++x) {
            if (cols.contains(x)) {
                dist += extra;
            }
        }
        return dist;
    }
}
