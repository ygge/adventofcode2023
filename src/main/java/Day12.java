import util.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day12 {

    public static void main(String[] args) {
        Util.verifySubmission();
        //var inputs = Util.readStrings();
        /*
        var inputs = Util.toStringList("???.### 1,1,3\n" +
                ".??..??...?##. 1,1,3\n" +
                "?#?#?#?#?#?#?#? 1,3,1,6\n" +
                "????.#...#... 4,1,1\n" +
                "????.######..#####. 1,6,5\n" +
                "?###???????? 3,2,1");
         */
        var inputs = Collections.singletonList("?###???????? 3,2,1");
        Util.submitPart1(part1(inputs));
    }

    private static long part1(List<String> inputs) {
        long sum = 0;
        for (String input : inputs) {
            var split = input.split(" ");
            var list = Arrays.stream(split[0].split("\\."))
                    .filter(s -> !s.isEmpty())
                    .toList();
            var values = Arrays.stream(split[1].split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            long ans = calc(list, values, 0, 0);
            System.out.println(input + ": " + ans);
            sum += ans;
        }
        return sum;
    }

    private static long calc(List<String> list, List<Integer> values, int index1, int index2) {
        if (index1 == list.size() && index2 == values.size()) {
            return 1;
        }
        if (index1 == list.size()) {
            return 0;
        }
        if (index2 == values.size()) {
            return list.subList(index1, list.size()).stream().anyMatch(s -> s.contains("#")) ? 0 : 1;
        }
        long v = calc(list, values, index1 + 1, index2);
        String pattern = list.get(index1);
        if (!pattern.contains("?")) {
            if (pattern.length() == values.get(index2)) {
                return v + calc(list, values, index1 + 1, index2 + 1);
            }
            return v;
        }
        if (pattern.length() < values.get(index2)) {
            return pattern.contains("#") ? 0 : v;
        }
        if (pattern.length() == values.get(index2)) {
            return v + calc(list, values, index1 + 1, index2 +1);
        }
        return v + calc(list, values, index1, index2, pattern);
    }

    private static long calc(List<String> list, List<Integer> values, int index1, int index2, String pattern) {
        if (pattern.isEmpty()) {
            return calc(list, values, index1 + 1, index2);
        }
        if (index2 == values.size()) {
            return pattern.contains("#") ? 0 : calc(list, values, index1, index2);
        }
        int nextLen = values.get(index2);
        if (pattern.length() < nextLen) {
            return calc(list, values, index1, index2);
        }
        if (pattern.length() == nextLen) {
            return calc(list, values, index1 + 1, index2 + 1);
        }
        if (pattern.startsWith("#")) {
            if (pattern.charAt(nextLen) != '?') {
                return 0;
            }
            return calc(list, values, index1, index2 + 1, pattern.substring(nextLen + 1));
        }

        long startNow = pattern.charAt(nextLen) == '?'
                ? calc(list, values, index1, index2 + 1, pattern.substring(nextLen + 1))
                : 0;
        long startNext = calc(list, values, index1, index2, pattern.substring(1));
        return startNow + startNext;
    }
}
