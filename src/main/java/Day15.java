import util.Util;

import java.util.ArrayList;
import java.util.List;

public class Day15 {

    public static void main(String[] args) {
        var input = Util.readString();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(String input) {
        List<List<Box>> boxes = new ArrayList<>();
        for (int i = 0; i < 256; ++i) {
            boxes.add(new ArrayList<>());
        }
        for (String str : input.split(",")) {
            Integer focal = null;
            if (str.endsWith("-")) {
                str = str.substring(0, str.length()-1);
            } else {
                var s = str.split("=");
                str = s[0];
                focal = Integer.parseInt(s[1]);
            }
            int s = 0;
            for (int i = 0; i < str.length(); ++i) {
                s += str.charAt(i);
                s *= 17;
                s %= 256;
            }
            var box = boxes.get(s);
            final String label = str;
            if (focal == null) {
                box.removeIf(b -> label.equals(b.label));
            } else {
                boolean found = false;
                for (int i = 0; i < box.size(); ++i) {
                    if (box.get(i).label.equals(label)) {
                        found = true;
                        box.set(i, new Box(label, focal));
                        break;
                    }
                }
                if (!found) {
                    box.add(new Box(label, focal));
                }
            }
        }
        long sum = 0;
        for (int i = 0; i < boxes.size(); ++i) {
            for (int j = 0; j < boxes.get(i).size(); ++j) {
                sum += (i+1)*(j+1)*boxes.get(i).get(j).focal;
            }
        }
        return sum;
    }

    private static long part1(String input) {
        long sum = 0;
        for (String str : input.split(",")) {
            int s = 0;
            for (int i = 0; i < str.length(); ++i) {
                s += str.charAt(i);
                s *= 17;
                s %= 256;
            }
            System.out.println(str + " " + s);
            sum += s;
        }
        return sum;
    }

    private record Box(String label, int focal) { }
}
