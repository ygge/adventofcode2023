import util.Util;

import java.util.*;

public class Day4 {

    public static void main(String[] args) {
        var inputs = Util.readStrings();
        //Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static int part2(List<String> inputs) {
        int sum = 0;
        Map<Integer, Integer> count = new HashMap<>();
        int index = 0;
        for (String input : inputs) {
            int num = count.getOrDefault(index, 0) + 1;
            System.out.println(index + " " + num);
            sum += num;
            var split = input.substring(input.indexOf(':') + 2).split("\\|");
            var winning = new HashSet<>(Arrays.stream(split[0].split(" ")).filter(s -> !s.trim().isEmpty()).toList());
            var mine = new HashSet<>(Arrays.stream(split[1].split(" ")).filter(s -> !s.trim().isEmpty()).toList());
            mine.retainAll(winning);
            if (!mine.isEmpty()) {
                int n = mine.size();
                for (int i = 1; i <= n; ++i) {
                    count.put(i + index, count.getOrDefault(i + index, 0) + num);
                }
            }
            ++index;
        }
        return sum;
    }

    private static int part1(List<String> inputs) {
        int sum = 0;
        for (String input : inputs) {
            var split = input.substring(input.indexOf(':') + 2).split("\\|");
            var winning = new HashSet<>(Arrays.stream(split[0].split(" ")).filter(s -> !s.trim().isEmpty()).toList());
            var mine = new HashSet<>(Arrays.stream(split[1].split(" ")).filter(s -> !s.trim().isEmpty()).toList());
            mine.retainAll(winning);
            if (!mine.isEmpty()) {
                sum += Math.pow(2, mine.size() - 1);
            }
        }
        return sum;
    }
}
