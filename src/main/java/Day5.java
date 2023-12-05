import util.Util;

import java.util.*;

public class Day5 {

    public static void main(String[] args) {
        var inputs = Util.readStrings();
        Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static long part2(List<String> inputs) {
        var seeds = Arrays.asList(inputs.get(0).substring("seeds: ".length()).split(" "));
        var converters = parseConverters(inputs);
        long min = Long.MAX_VALUE;
        for (int i = 0; i < seeds.size(); i += 2) {
            var start = Long.parseLong(seeds.get(i));
            var length = Long.parseLong(seeds.get(i+1));
            for (int j = 0; j < length; ++j) {
                var ans = toLocation(converters, start + j);
                min = Math.min(min, ans);
            }
        }
        return min;
    }

    private static long part1(List<String> inputs) {
        var seeds = Arrays.asList(inputs.get(0).substring("seeds: ".length()).split(" "));
        var converters = parseConverters(inputs);
        return seeds.stream()
                .map(s -> toLocation(converters, Long.parseLong(s)))
                .min(Long::compareTo)
                .orElseThrow();
    }

    private static List<Converter> parseConverters(List<String> inputs) {
        var converters = new ArrayList<Converter>();
        for (int i = 2; i < inputs.size(); ++i) {
            String row = inputs.get(i);
            if (row.isBlank()) {
                continue;
            }
            if (!row.contains("map")) {
                throw new RuntimeException(row);
            }
            var split = row.substring(0, row.length()-5).split("-to-");
            var from = split[0];
            var to = split[1];
            var c = new Converter(from, to, new ArrayList<>());
            ++i;
            while (i < inputs.size() && !inputs.get(i).isBlank()) {
                var v = inputs.get(i).split(" ");
                ++i;
                c.list.add(new Conv(Long.parseLong(v[1]), Long.parseLong(v[0]), Long.parseLong(v[2])));
            }
            converters.add(c);
        }
        return converters;
    }

    private static long toLocation(List<Converter> converters, long seed) {
        String name = "seed";
        long v = seed;
        while (!name.equals("location")) {
            for (Converter converter : converters) {
                if (converter.from.equals(name)) {
                    for (Conv conv : converter.list) {
                        if (conv.from <= v && conv.from+conv.delta > v) {
                            v += conv.to - conv.from;
                            break;
                        }
                    }
                    name = converter.to;
                    break;
                }
            }
        }
        return v;
    }

    private record Converter(String from, String to, List<Conv> list) {
    }

    private record Conv(long from, long to, long delta) {
    }
}
