import util.Util;

import java.util.*;
import java.util.function.Function;

public class Day8 {

    public static void main(String[] args) {
        var inputs = Util.readStrings();
        Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static long part2(List<String> inputs) {
        String ins = inputs.get(0);
        Map<String, String> left = new HashMap<>();
        Map<String, String> right = new HashMap<>();
        for (int i = 2; i < inputs.size(); ++i) {
            var s = inputs.get(i).split(" = ");
            var from = s[0];
            var to = s[1].substring(1, s[1].length()-1).split(", ");
            left.put(from, to[0]);
            right.put(from, to[1]);
        }
        List<List<Result>> curr = left.keySet().stream()
                .filter(k -> k.endsWith("A"))
                .map(k -> {
                    var list = new ArrayList<Result>();
                    list.add(new Result(k, 0));
                    return (List<Result>)list;
                })
                .toList();
        for (List<Result> result : curr) {
            var c = result.get(0);
            for (int i = 0; i < 3; ++i) {
                c = determineSteps(c.curr, s -> s.endsWith("Z"), ins, left, right);
                result.add(c);
            }
        }
        List<Long> values = new ArrayList<>();
        for (List<Result> results : curr) {
            values.add((long)results.get(1).steps);
        }
        long s = values.get(0);
        for (int i = 1; i < values.size(); ++i) {
            var a = values.get(i);
            long ans = s;
            while (ans%a != 0) {
                ans += s;
            }
            s = ans;
        }
        return s;
    }

    private static long part1(List<String> inputs) {
        String ins = inputs.get(0);
        Map<String, String> left = new HashMap<>();
        Map<String, String> right = new HashMap<>();
        for (int i = 2; i < inputs.size(); ++i) {
            var s = inputs.get(i).split(" = ");
            var from = s[0];
            var to = s[1].substring(1, s[1].length()-1).split(", ");
            left.put(from, to[0]);
            right.put(from, to[1]);
        }
        String curr = "AAA";
        return determineSteps(curr, s -> s.equals("ZZZ"), ins, left, right).steps;
    }

    private static Result determineSteps(String curr, Function<String, Boolean> goal, String ins, Map<String, String> left, Map<String, String> right) {
        int steps = 0;
        while (true) {
            if (goal.apply(curr) && steps > 0) {
                return new Result(curr, steps);
            }
            if (ins.charAt(steps % ins.length()) == 'L') {
                curr = left.get(curr);
            } else {
                curr = right.get(curr);
            }
            ++steps;
        }
    }

    private record Result(String curr, int steps) { }
}
