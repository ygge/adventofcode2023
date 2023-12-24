import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day24 {

    public static void main(String[] args) {
        Util.verifySubmission();
        var input = Util.readStrings();
        //Util.submitPart1(part1(input));
        part2(input);
    }

    private static void part2(List<String> input) {
        List<List<Long>> pos = new ArrayList<>();
        List<List<Long>> vel = new ArrayList<>();
        for (String row : input) {
            var s = row.split("@");
            var p = s[0].split(", ");
            var v = s[1].split(", ");
            pos.add(Arrays.asList(Long.parseLong(p[0].trim()), Long.parseLong(p[1].trim()), Long.parseLong(p[2].trim())));
            vel.add(Arrays.asList(Long.parseLong(v[0].trim()), Long.parseLong(v[1].trim()), Long.parseLong(v[2].trim())));
        }
        var equations = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            String t = "t" + i;
            equations.append(t).append(" >= 0, ").append(pos.get(i).get(0)).append(" + ").append(vel.get(i).get(0)).append(t).append(" == x + vx ").append(t).append(", ");
            equations.append(pos.get(i).get(1)).append(" + ").append(vel.get(i).get(1)).append(t).append(" == y + vy ").append(t).append(", ");
            equations.append(pos.get(i).get(2)).append(" + ").append(vel.get(i).get(2)).append(t).append(" == z + vz ").append(t).append(", ");
        }
        System.out.println("Solve[{" + equations.substring(0, equations.length() - 2) + "}, {x,y,z,vx,vy,vz,t0,t1,t2}]");
        // {{x419848807765291,y391746659362922,z213424530058607,vx-227,vy-221,vz111,t0178381243424,t1564132261724,t2585444749559}}
        var x = 419848807765291L;
        var y = 391746659362922L;
        var z = 213424530058607L;
        System.out.println(x + y + z);
    }

    private static long part1(List<String> input) {
        List<List<Long>> pos = new ArrayList<>();
        List<List<Long>> vel = new ArrayList<>();
        for (String row : input) {
            var s = row.split("@");
            var p = s[0].split(", ");
            var v = s[1].split(", ");
            pos.add(Arrays.asList(Long.parseLong(p[0].trim()), Long.parseLong(p[1].trim())));
            vel.add(Arrays.asList(Long.parseLong(v[0].trim()), Long.parseLong(v[1].trim())));
        }
        long count = 0;
        for (int i = 0; i < pos.size() - 1; ++i) {
            for (int j = i + 1; j < pos.size(); ++j) {
                var a = crossing(pos.get(i), vel.get(i), pos.get(j), vel.get(j));
                if (a) {
                    ++count;
                }
                var b = crossing2(pos.get(i), vel.get(i), pos.get(j), vel.get(j));
                if (a != b) {
                    crossing(pos.get(i), vel.get(i), pos.get(j), vel.get(j));
                    crossing2(pos.get(i), vel.get(i), pos.get(j), vel.get(j));
                }
            }
        }
        return count;
    }

    private static boolean crossing(List<Long> pos1, List<Long> vel1, List<Long> pos2, List<Long> vel2) {
        var x1 = pos1.get(0);
        var y1 = pos1.get(1);
        var x2 = pos1.get(0) + vel1.get(0);
        var y2 = pos1.get(1) + vel1.get(1);
        var x3 = pos2.get(0);
        var y3 = pos2.get(1);
        var x4 = pos2.get(0) + vel2.get(0);
        var y4 = pos2.get(1) + vel2.get(1);

        var denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0) {
            return false;
        }
        var u = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        var x = x1 + u * (x2 - x1);
        var y = y1 + u * (y2 - y1);
        if (x > x1 == x2-x1 > 0 && y > y1 == y2-y1 > 0 && x > x3 == x4-x3 > 0 && y > y3 == y4-y3 > 0
                && x >= 200000000000000L && x <= 400000000000000L && y >= 200000000000000L && y <= 400000000000000L) {
            return true;
        }
        return false;
    }

    private static boolean crossing2(List<Long> pos1, List<Long> vel1, List<Long> pos2, List<Long> vel2) {
        var x1 = pos1.get(0);
        var y1 = pos1.get(1);
        var x2 = pos1.get(0) + vel1.get(0);
        var y2 = pos1.get(1) + vel1.get(1);
        var x3 = pos2.get(0);
        var y3 = pos2.get(1);
        var x4 = pos2.get(0) + vel2.get(0);
        var y4 = pos2.get(1) + vel2.get(1);
        double n = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
        if (n == 0) {
            return false;
        }
        double xt = (x1*y2 - y1*x2) * (x3 - x4) - (x1 - x2) * (x3*y4 - y3*x4);
        double yt = (x1*y2 - y1*x2) * (y3 - y4) - (y1 - y2) * (x3*y4 - y3*x4);
        var x = xt/n;
        var y = yt/n;
        if (x >= 200000000000000L && x <= 400000000000000L
                && y >= 200000000000000L && y <= 400000000000000L) {
            var t1 = (x-x1) / vel1.get(0);
            var t2 = (x-x3) / vel2.get(0);
            if (t1 > 0 && t2 > 0) {
                // 11473
                // 11477
                // 17244
                return true;
            }
        }
        return false;
    }
}
