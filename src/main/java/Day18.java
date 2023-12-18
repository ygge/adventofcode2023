import util.Direction;
import util.Pos;
import util.Util;

import java.util.*;

public class Day18 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(List<String> input) {
        var pos = new Pos(0, 0);
        var list = new ArrayList<Pos>();
        list.add(pos);
        long count = 0;
        for (String row : input) {
            var s = row.split(" ")[2];
            int steps = Integer.parseInt(s.substring(2, 7), 16);
            var dir = switch (s.charAt(7)) {
                case '3' -> Direction.UP;
                case '1' -> Direction.DOWN;
                case '2' -> Direction.LEFT;
                case '0' -> Direction.RIGHT;
                default -> throw new IllegalArgumentException(row);
            };
            for (int i = 0; i < steps; ++i) {
                pos = pos.move(dir);
            }
            count += steps;
            list.add(pos);
        }
        list.add(new Pos(0, 0));
        long area = 0;
        for (int i = 0; i < list.size() - 1; ++i) {
            var px = (long)list.get(i).x;
            var py = (long)list.get(i).y;
            area += px * list.get(i + 1).y - list.get(i + 1).x * py;
        }
        return area/2 + 1 + count / 2;
    }

    private static long part1(List<String> input) {
        var pos = new Pos(0, 0);
        int minX = 0;
        int maxX = 0;
        int minY = 0;
        int maxY = 0;
        var seen = new HashSet<Pos>();
        seen.add(pos);
        for (String row : input) {
            var s = row.split(" ");
            char d = s[0].charAt(0);
            int steps = Integer.parseInt(s[1]);
            var dir = switch (d) {
                case 'U' -> Direction.UP;
                case 'D' -> Direction.DOWN;
                case 'L' -> Direction.LEFT;
                case 'R' -> Direction.RIGHT;
                default -> throw new IllegalArgumentException(row);
            };
            for (int i = 0; i < steps; ++i) {
                pos = pos.move(dir);
                seen.add(pos);
                minX = Math.min(minX, pos.x);
                maxX = Math.max(maxX, pos.x);
                minY = Math.min(minY, pos.y);
                maxY = Math.max(maxY, pos.y);
            }
        }
        long count = 0;
        Set<Pos> inside = inside(new Pos(1, 1), seen);
        for (int y = minY; y <= maxY; ++y) {
            for (int x = minX; x <= maxX; ++x) {
                var p = new Pos(x, y);
                if (seen.contains(p) || inside.contains(p)) {
                    ++count;
                }
            }
        }
        return count;
    }

    private static Set<Pos> inside(Pos pos, Set<Pos> seen) {
        var s = new HashSet<Pos>();
        var que = new LinkedList<Pos>();
        que.add(pos);
        while (!que.isEmpty()) {
            var p = que.pop();
            if (!s.add(p)) {
                continue;
            }
            if (seen.contains(p)) {
                continue;
            }
            for (Direction dir : Direction.values()) {
                que.add(p.move(dir));
            }
        }
        return s;
    }
}
