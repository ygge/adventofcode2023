import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 {

    public static void main(String[] args) {
        var inputs = Util.readStrings();
        Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static int part2(List<String> inputs) {
        int sum = 0;
        for (String input : inputs) {
            var values = Arrays.stream(input.split(" "))
                    .map(Integer::parseInt)
                    .toList();
            sum += prev(values);
        }
        return sum;
    }

    private static long part1(List<String> inputs) {
        int sum = 0;
        for (String input : inputs) {
            var values = Arrays.stream(input.split(" "))
                    .map(Integer::parseInt)
                    .toList();
            sum += next(values);
        }
        return sum;
    }

    private static int prev(List<Integer> values) {
        List<Integer> diff = getDiff(values);
        if (diff.stream().allMatch(v -> v == 0)) {
            return values.get(0);
        }
        var prev = prev(diff);
        return values.get(0) - prev;
    }

    private static int next(List<Integer> values) {
        List<Integer> diff = getDiff(values);
        if (diff.stream().allMatch(v -> v == 0)) {
            return values.get(0);
        }
        var next = next(diff);
        return values.get(values.size() - 1) + next;
    }

    private static List<Integer> getDiff(List<Integer> values) {
        List<Integer> diff = new ArrayList<>();
        int last = values.get(0);
        for (int i = 1; i < values.size(); ++i) {
            diff.add(values.get(i) - last);
            last = values.get(i);
        }
        return diff;
    }
}
