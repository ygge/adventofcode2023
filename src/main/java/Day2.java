import util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 {

    public static void main(String[] args) {
        var inputs = Util.readStrings();
        Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static long part2(List<String> inputs) {
        long sum = 0;
        for (String input : inputs) {
            var split = input.split(": ");
            sum += power(split[1]);
        }
        return sum;
    }

    private static int part1(List<String> inputs) {
        int sum = 0;
        for (String input : inputs) {
            var split = input.split(": ");
            var id = Integer.parseInt(split[0].substring("Game ".length()));
            if (possible(split[1])) {
                sum += id;
            }
        }
        return sum;
    }

    private static int power(String games) {
        Map<String, Integer> max = new HashMap<>();
        for (String game : games.split("; ")) {
            var split = game.split(", ");
            for (String color : split) {
                var s = color.split(" ");
                int count = Integer.parseInt(s[0]);
                int v = max.getOrDefault(s[1], 0);
                max.put(s[1], Math.max(v, count));
            }
        }
        return max.values().stream()
                .reduce(1, (a, b) -> a * b);
    }

    private static boolean possible(String games) {
        for (String game : games.split("; ")) {
            var split = game.split(", ");
            for (String color : split) {
                if (!possibleColor(color)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean possibleColor(String color) {
        var s = color.split(" ");
        int count = Integer.parseInt(s[0]);
        return switch (s[1]) {
            case "red" -> count <= 12;
            case "green" -> count <= 13;
            case "blue" -> count <= 14;
            default -> throw new IllegalStateException(color);
        };
    }
}





