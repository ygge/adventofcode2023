import util.Util;

import java.util.List;

public class Day13 {

    public static void main(String[] args) {
        var inputs = Util.readStrings();
        Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static long part2(List<String> inputs) {
        int start = 0;
        int row = 0;
        long sum = 0;
        while (row < inputs.size()) {
            while (row < inputs.size() && !inputs.get(row).isEmpty()) {
                ++row;
            }
            sum += calc2(inputs.subList(start, row));
            start = row + 1;
            row = start;
        }
        return sum;
    }

    private static long part1(List<String> inputs) {
        int start = 0;
        int row = 0;
        long sum = 0;
        while (row < inputs.size()) {
            while (row < inputs.size() && !inputs.get(row).isEmpty()) {
                ++row;
            }
            sum += calc(inputs.subList(start, row), -1);
            start = row + 1;
            row = start;
        }
        return sum;
    }

    private static long calc2(List<String> strings) {
        long vv = calc(strings, -1);
        for (int y = 0; y < strings.size(); ++y) {
            var str = strings.get(y);
            var array = str.toCharArray();
            for (int x = 0; x < array.length; ++x) {
                char c = array[x];
                char oc = c == '.' ? '#' : '.';
                array[x] = oc;
                strings.set(y, new String(array));
                long v = calc(strings, vv);
                if (v != 0 && v != vv) {
                    return v;
                }
                array[x] = c;
                strings.set(y, new String(array));
            }
        }
        throw new IllegalStateException();
    }

    private static long calc(List<String> inputs, long notValid) {
        for (int x = 1; x < inputs.get(0).length(); ++x) {
            boolean same = true;
            for (int dx = 1; ; ++dx) {
                if (!same || x - dx < 0 || x + dx > inputs.get(0).length()) {
                    break;
                }
                for (int y = 0; y < inputs.size(); ++y) {
                    if (inputs.get(y).charAt(x + dx - 1) != inputs.get(y).charAt(x - dx)) {
                        same = false;
                        break;
                    }
                }
            }
            if (same && x != notValid) {
                return x;
            }
        }
        for (int y = 1; y < inputs.size(); ++y) {
            boolean same = true;
            for (int dy = 1; ; ++dy) {
                if (!same || y - dy < 0 || y + dy > inputs.size()) {
                    break;
                }
                for (int x = 0; x < inputs.get(y).length(); ++x) {
                    if (inputs.get(y + dy - 1).charAt(x) != inputs.get(y - dy).charAt(x)) {
                        same = false;
                        break;
                    }
                }
            }
            if (same && 100*y != notValid) {
                return 100*y;
            }
        }
        return 0;
    }
}
