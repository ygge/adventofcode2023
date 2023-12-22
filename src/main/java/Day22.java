import util.MultiDimPos;
import util.Util;

import java.util.*;

public class Day22 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static int part2(List<String> input) {
        List<List<MultiDimPos>> bricks = getBricks(input);
        bricks.sort(Comparator.comparingInt(b -> b.stream().map(p -> p.dim[2]).min(Comparator.comparingInt(a -> a)).orElseThrow()));
        int sum = 0;
        for (int i = 0; i < bricks.size(); ++i) {
            var removed = new HashSet<List<MultiDimPos>>();
            removed.add(bricks.get(i));
            for (int j = 0; j < bricks.size(); ++j) {
                var brick = bricks.get(j);
                if (brick.stream().anyMatch(b -> b.dim[2] == 1) || removed.contains(brick)) {
                    continue;
                }
                if (bricks.stream().filter(b -> !removed.contains(b)).filter(b -> !b.equals(brick)).noneMatch(b -> below(brick, b))) {
                    removed.add(brick);
                }
            }
            sum += removed.size() - 1;
        }
        return sum;
    }

    private static int part1(List<String> input) {
        List<List<MultiDimPos>> bricks = getBricks(input);
        var safe = new HashSet<>(bricks);
        for (int i = 0; i < bricks.size(); ++i) {
            List<MultiDimPos> singleBrick = null;
            for (int j = 0; j < bricks.size(); ++j) {
                if (i != j && below(bricks.get(i), bricks.get(j))) {
                    if (singleBrick == null) {
                        singleBrick = bricks.get(j);
                    } else {
                        singleBrick = null;
                        break;
                    }
                }
            }
            if (singleBrick != null) {
                safe.remove(singleBrick);
            }
        }
        return safe.size();
    }

    private static List<List<MultiDimPos>> getBricks(List<String> input) {
        List<List<MultiDimPos>> bricks = new ArrayList<>();
        for (String brick : input) {
            var p = brick.split("~");
            var as = p[0].split(",");
            var bs = p[1].split(",");
            var a = new MultiDimPos(Integer.parseInt(as[0]), Integer.parseInt(as[1]), Integer.parseInt(as[2]));
            var b = new MultiDimPos(Integer.parseInt(bs[0]), Integer.parseInt(bs[1]), Integer.parseInt(bs[2]));
            if (a.equals(b)) {
                bricks.add(Collections.singletonList(a));
            } else {
                var dx = Integer.compare(b.dim[0], a.dim[0]);
                var dy = Integer.compare(b.dim[1], a.dim[1]);
                var dz = Integer.compare(b.dim[2], a.dim[2]);
                var loop = a;
                var s = new ArrayList<MultiDimPos>();
                while (true) {
                    s.add(loop);
                    if (loop.equals(b)) {
                        break;
                    }
                    loop = new MultiDimPos(loop.dim[0] + dx, loop.dim[1] + dy, loop.dim[2] + dz);
                }
                bricks.add(s);
            }
        }
        while (true) {
            boolean anyMoved = false;
            for (int i = 0; i < bricks.size(); ++i) {
                var brick = bricks.get(i);
                if (brick.stream().anyMatch(b -> b.dim[2] == 1)) {
                    continue;
                }
                var canFall = true;
                for (int j = 0; j < bricks.size(); ++j) {
                    if (j != i && below(brick, bricks.get(j))) {
                        canFall = false;
                        break;
                    }
                }
                if (canFall) {
                    for (MultiDimPos pos : brick) {
                        --pos.dim[2];
                    }
                    anyMoved = true;
                }
            }
            if (!anyMoved) {
                break;
            }
        }
        return bricks;
    }

    private static boolean below(List<MultiDimPos> brick, List<MultiDimPos> otherBrick) {
        for (MultiDimPos pos : brick) {
            var newPos = new MultiDimPos(pos.dim);
            --newPos.dim[2];
            if (otherBrick.stream().anyMatch(p -> p.equals(newPos))) {
                return true;
            }
        }
        return false;
    }
}
