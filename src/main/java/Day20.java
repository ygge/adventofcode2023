import util.Util;

import java.util.*;

public class Day20 {

    public static void main(String[] args) {
        Util.verifySubmission();
        var input = Util.readStrings();
        //Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(List<String> input) {
        Map<String, Module> modules = new HashMap<>();
        Map<String, List<String>> conn = new HashMap<>();
        for (String row : input) {
            var s = row.split(" -> ");
            var from = s[0];
            var to = s[1].split(", ");
            var fromName = from;
            if (from.charAt(0) == '%' || from.charAt(0) == '&') {
                fromName = from.substring(1);
            }
            modules.put(fromName, new Module(from));
            conn.put(fromName, Arrays.stream(to).toList());
        }

        modules.put("rx", new Module("rx"));
        for (Map.Entry<String, List<String>> entry : conn.entrySet()) {
            for (String target : entry.getValue()) {
                if (modules.containsKey(target)) {
                    modules.get(target).lastReceived.put(entry.getKey(), false);
                }
            }
        }

        var target = modules.get("rx").lastReceived.keySet().stream().findFirst().orElseThrow();
        List<Integer> data = new ArrayList<>();
        for (String s : modules.get(target).lastReceived.keySet()) {
            data.add(findLowBit(input, s));
        }
        // 3821, 4019, 3923, 3919
        return -1;
    }

    private static int findLowBit(List<String> input, String goal) {
        Map<String, Module> modules = new HashMap<>();
        Map<String, List<String>> conn = new HashMap<>();
        for (String row : input) {
            var s = row.split(" -> ");
            var from = s[0];
            var to = s[1].split(", ");
            var fromName = from;
            if (from.charAt(0) == '%' || from.charAt(0) == '&') {
                fromName = from.substring(1);
            }
            modules.put(fromName, new Module(from));
            conn.put(fromName, Arrays.stream(to).toList());
        }

        modules.put("rx", new Module("rx"));
        for (Map.Entry<String, List<String>> entry : conn.entrySet()) {
            for (String target : entry.getValue()) {
                if (modules.containsKey(target)) {
                    modules.get(target).lastReceived.put(entry.getKey(), false);
                }
            }
        }
        for (int count = 0; ; ++count) {
            var states = new LinkedList<State>();
            states.push(new State(null, "broadcaster", false));
            while (!states.isEmpty()) {
                var state = states.pop();
                if (Objects.equals(state.name, goal) && !state.high) {
                    return count + 1;
                }
                Module module = modules.get(state.name);
                if (module == null) {
                    continue;
                }
                var type = module.type.charAt(0);
                boolean sendHigh = state.high;
                if (type == '%') {
                    if (state.high) {
                        continue;
                    }
                    module.on = !module.on;
                    sendHigh = module.on;
                }
                if (type == '&') {
                    module.lastReceived.put(state.from, state.high);
                    sendHigh = !module.lastReceived.values().stream().allMatch(v -> v);
                }
                if (conn.containsKey(state.name)) {
                    for (String s : conn.get(state.name)) {
                        states.addLast(new State(state.name, s, sendHigh));
                    }
                } else {
                    System.out.println(state.name);
                }
            }
        }
    }

    private static long part1(List<String> input) {
        Map<String, Module> modules = new HashMap<>();
        Map<String, List<String>> conn = new HashMap<>();
        for (String row : input) {
            var s = row.split(" -> ");
            var from = s[0];
            var to = s[1].split(", ");
            var fromName = from;
            if (from.charAt(0) == '%' || from.charAt(0) == '&') {
                fromName = from.substring(1);
            }
            modules.put(fromName, new Module(from));
            conn.put(fromName, Arrays.stream(to).toList());
        }

        for (Map.Entry<String, List<String>> entry : conn.entrySet()) {
            for (String target : entry.getValue()) {
                if (modules.containsKey(target)) {
                    modules.get(target).lastReceived.put(entry.getKey(), false);
                }
            }
        }

        long high = 0;
        long low = 0;
        for (int i = 0; i < 1000; ++i) {
            var states = new LinkedList<State>();
            ++low;
            states.push(new State(null, "broadcaster", false));
            while (!states.isEmpty()) {
                var state = states.pop();
                Module module = modules.get(state.name);
                if (module == null) {
                    continue;
                }
                var type = module.type.charAt(0);
                boolean sendHigh = state.high;
                if (type == '%') {
                    if (state.high) {
                        continue;
                    }
                    module.on = !module.on;
                    sendHigh = module.on;
                }
                if (type == '&') {
                    module.lastReceived.put(state.from, state.high);
                    sendHigh = !module.lastReceived.values().stream().allMatch(v -> v);
                }
                if (sendHigh) {
                    high += conn.get(state.name).size();
                } else {
                    low += conn.get(state.name).size();
                }
                for (String s : conn.get(state.name)) {
                    states.addLast(new State(state.name, s, sendHigh));
                }
            }
        }
        return high * low;
    }

    private record State(String from, String name, boolean high) {
    }

    private static class Module {
        private final String type;
        private final Map<String, Boolean> lastReceived = new HashMap<>();
        private boolean on = false;

        private Module(String type) {
            this.type = type;
        }
    }
}
