import util.Util;

import java.util.*;

public class Day1 {

    public static void main(String[] args) {
        var inputs = Util.readStrings();
        Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static int part2(List<String> inputs) {
        int sum = 0;
        for (String input : inputs) {
            String a = "";
            var pos = new ArrayList<>(Arrays.asList(
                    findFirst(input, "one", 1),
                    findFirst(input, "two", 2),
                    findFirst(input, "three", 3),
                    findFirst(input, "four", 4),
                    findFirst(input, "five", 5),
                    findFirst(input, "six", 6),
                    findFirst(input, "seven", 7),
                    findFirst(input, "eight", 8),
                    findFirst(input, "nine", 9),
                    findLast(input, "one", 1),
                    findLast(input, "two", 2),
                    findLast(input, "three", 3),
                    findLast(input, "four", 4),
                    findLast(input, "five", 5),
                    findLast(input, "six", 6),
                    findLast(input, "seven", 7),
                    findLast(input, "eight", 8),
                    findLast(input, "nine", 9)
            ));
            for (int i = 0; i < input.length(); ++i) {
                if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                    pos.add(new Data(i, input.charAt(i) - '0'));
                    break;
                }
            }
            for (int i = 0; i < input.length(); ++i) {
                if (input.charAt(input.length() - i - 1) >= '0' && input.charAt(input.length() - i - 1) <= '9') {
                    pos.add(new Data(input.length() - i - 1, input.charAt(input.length() - i - 1) - '0'));
                    break;
                }
            }
            var f = pos.stream()
                    .filter(d -> d.pos != -1)
                    .min(Comparator.comparing(d -> d.pos))
                    .orElseThrow();
            a += f.value;
            var s = pos.stream()
                    .filter(d -> d.pos != -1)
                    .max(Comparator.comparing(d -> d.pos))
                    .orElseThrow();
            a += s.value;
            System.out.println(a);
            sum += Integer.parseInt(a);
        }
        return sum;
    }

    private static Data findFirst(String input, String text, int v) {
        int vv = input.indexOf(text);
        return new Data(vv, v);
    }

    private static Data findLast(String input, String text, int v) {
        int vv = input.lastIndexOf(text);
        return new Data(vv, v);
    }

    private static int part1(List<String> inputs) {
        int sum = 0;
        for (String input : inputs) {
            String a = "";
            for (int i = 0; i < input.length(); ++i) {
                if (input.charAt(i) >= '0' && input.charAt(i) <= '9') {
                    a += input.charAt(i);
                    break;
                }
            }
            for (int i = 0; i < input.length(); ++i) {
                if (input.charAt(input.length() - i - 1) >= '0' && input.charAt(input.length() - i - 1) <= '9') {
                    a += input.charAt(input.length() - i - 1);
                    break;
                }
            }
            sum += Integer.parseInt(a);
        }
        return sum;
    }

    private record Data(int pos, int value) { }
}
