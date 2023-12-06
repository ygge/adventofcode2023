import util.Util;

import java.util.Arrays;
import java.util.List;

public class Day6 {

    public static void main(String[] args) {
        var inputs = Util.readStrings();
        Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static long part2(List<String> inputs) {
        long time = readLong(inputs.get(0));
        long dist = readLong(inputs.get(1));
        long start = 0;
        long end = time;
        for (; start < time; ++start) {
            long d = calc(time, start);
            if (d > dist) {
                break;
            }
        }
        for (; end >= start; --end) {
            long d = calc(time, end);
            if (d > dist) {
                break;
            }
        }
        return end - start + 1;
    }

    private static int part1(List<String> inputs) {
        List<Integer> time = Arrays.stream(inputs.get(0).substring("Time:".length()).split(" "))
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .toList();
        List<Integer> dist = Arrays.stream(inputs.get(1).substring("Distance:".length()).split(" "))
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .toList();
        int sum = 1;
        for (int i = 0; i < time.size(); ++i) {
            int num = 0;
            for (int t = 1; t < time.get(i); ++t) {
                int d = calc(time.get(i), t);
                System.out.println(i + " " + t + " " + d);
                if (d > dist.get(i)) {
                    ++num;
                }
            }
            sum *= num;
        }
        return sum;
    }

    private static long readLong(String input) {
        long num = 0;
        for (int i = 0; i < input.length(); ++i) {
            if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                num = 10*num + input.charAt(i)-'0';
            }
        }
        return num;
    }

    private static long calc(long time, long t) {
        var dt = time-t;
        return dt*t;
    }

    private static int calc(int time, int t) {
        var dt = time-t;
        return dt*t;
    }
}
