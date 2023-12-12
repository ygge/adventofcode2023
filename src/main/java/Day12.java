import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day12 {

    public static void main(String[] args) {
        var inputs = Util.readStrings();
        Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static long part2(List<String> inputs) {
        long sum = 0;
        for (String input : inputs) {
            var split = input.split(" ");
            var values = Arrays.stream(split[1].split(","))
                    .map(Integer::parseInt)
                    .toList();
            List<Integer> newValues = new ArrayList<>();
            for (int i = 0; i < 5; ++i) {
                newValues.addAll(values);
            }
            var sb = new StringBuilder(split[0]);
            for (int i = 0; i < 4; ++i) {
                sb.append("?").append(split[0]);
            }
            var newString = sb.toString();
            long[][] dp = new long[newString.length()][newValues.size()];
            for (int i = 0; i < dp.length; ++i) {
                Arrays.fill(dp[i], -1);
            }
            long ans = calc(dp, newString, newValues, 0, 0);
            sum += ans;
        }
        return sum;
    }

    private static long part1(List<String> inputs) {
        long sum = 0;
        for (String input : inputs) {
            var split = input.split(" ");
            var values = Arrays.stream(split[1].split(","))
                    .map(Integer::parseInt)
                    .toList();
            long[][] dp = new long[split[0].length()][values.size()];
            for (int i = 0; i < dp.length; ++i) {
                for (int j = 0; j < dp[i].length; ++j) {
                    dp[i][j] = -1;
                }
            }
            long ans = calc(dp, split[0], values, 0, 0);
            System.out.println(input + ": " + ans);
            sum += ans;
        }
        return sum;
    }

    private static long calc(long[][] dp, String pattern, List<Integer> values, int index1, int index2) {
        if (index1 >= pattern.length() && index2 == values.size()) {
            return 1;
        }
        if (index1 >= pattern.length()) {
            return 0;
        }
        if (index2 == values.size()) {
            return pattern.substring(index1).contains("#") ? 0 : 1;
        }
        if (dp[index1][index2] == -1) {
            if (index1 + values.get(index2) > pattern.length()) {
                dp[index1][index2] = 0;
            } else {
                boolean ok = true;
                for (int i = 0; i < values.get(index2); ++i) {
                    if (pattern.charAt(index1 + i) == '.') {
                        ok = false;
                        break;
                    }
                }
                if (ok && pattern.length() > index1 + values.get(index2)) {
                    ok = pattern.charAt(index1 + values.get(index2)) != '#';
                }
                long sum = 0;
                if (ok) {
                    sum = calc(dp, pattern, values, index1 + values.get(index2) + 1, index2 + 1);
                }
                if (pattern.charAt(index1) != '#') {
                    sum += calc(dp, pattern, values, index1 + 1, index2);
                }
                dp[index1][index2] = sum;
            }
        }
        return dp[index1][index2];
    }
}
