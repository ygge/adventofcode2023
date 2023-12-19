import util.Util;

import java.util.*;

public class Day19 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(part2(input));
    }

    private static long part2(List<String> input) {
        Map<String, List<String>> operations = new HashMap<>();
        int index = 0;
        for (; !input.get(index).isEmpty(); ++index) {
            int start = input.get(index).indexOf('{');
            var name = input.get(index).substring(0, start);
            var ops = Arrays.stream(input.get(index).substring(start+1, input.get(index).length() - 1).split(",")).toList();
            operations.put(name, ops);
        }
        var que = new LinkedList<State>();
        que.add(new State("in",
                Map.of("x", 1, "m", 1, "a", 1, "s", 1),
                Map.of("x", 4000, "m", 4000, "a", 4000, "s", 4000)
        ));
        long acc = 0;
        while (!que.isEmpty()) {
            var state = que.pop();
            if (state.flow.equals("A")) {
                acc += (state.max.get("x")-state.min.get("x")+1L)
                        *(state.max.get("m")-state.min.get("m")+1L)
                        *(state.max.get("a")-state.min.get("a")+1L)
                        *(state.max.get("s")-state.min.get("s")+1L);
                continue;
            } else if (state.flow.equals("R")) {
                continue;
            }
            var ops = operations.get(state.flow);
            for (String op : ops) {
                if (op.contains(":")) {
                    var op2 = op.split(":");
                    var cond = op2[0];
                    var v = cond.charAt(0);
                    var opp = cond.charAt(1);
                    var value2 = Integer.parseInt(cond.substring(2));
                    var vs = v + "";
                    var min = state.min.get(vs);
                    var max = state.max.get(vs);
                    var newFlow = op2[1];
                    var handled = false;
                    if (opp == '=') {
                        if (min == value2) {
                            handled = true;
                            var newState = new State(newFlow, new HashMap<>(state.min), new HashMap<>(state.max));
                            newState.min.put(vs, min);
                            newState.max.put(vs, min);
                            que.push(newState);
                            if (max > value2) {
                                var newState2 = new State(state.flow, new HashMap<>(state.min), new HashMap<>(state.max));
                                newState2.min.put(vs, min + 1);
                                newState2.max.put(vs, max);
                                que.push(newState2);
                            }
                        } else if (max == value2) {
                            handled = true;
                            var newState = new State(newFlow, new HashMap<>(state.min), new HashMap<>(state.max));
                            newState.min.put(vs, max);
                            newState.max.put(vs, max);
                            que.push(newState);
                            if (min < value2) {
                                var newState2 = new State(state.flow, new HashMap<>(state.min), new HashMap<>(state.max));
                                newState2.min.put(vs, min);
                                newState2.max.put(vs, max - 1);
                                que.push(newState2);
                            }
                        } else if (min < value2 && max > value2) {
                            handled = true;
                            var newState = new State(newFlow, new HashMap<>(state.min), new HashMap<>(state.max));
                            newState.min.put(vs, value2);
                            newState.max.put(vs, value2);
                            que.push(newState);
                            var newStateMin = new State(state.flow, new HashMap<>(state.min), new HashMap<>(state.max));
                            newStateMin.min.put(vs, min);
                            newStateMin.max.put(vs, value2 - 1);
                            que.push(newStateMin);
                            var newStateMax = new State(state.flow, new HashMap<>(state.min), new HashMap<>(state.max));
                            newStateMax.min.put(vs, value2 + 1);
                            newStateMax.max.put(vs, max);
                            que.push(newStateMax);
                        }
                    } else if (opp == '>') {
                        if (min >= value2) {
                            handled = true;
                            var newState = new State(newFlow, new HashMap<>(state.min), new HashMap<>(state.max));
                            que.push(newState);
                        } else if (max > value2) {
                            handled = true;
                            var newStateMin = new State(state.flow, new HashMap<>(state.min), new HashMap<>(state.max));
                            newStateMin.min.put(vs, min);
                            newStateMin.max.put(vs, value2);
                            que.push(newStateMin);
                            var newStateMax = new State(newFlow, new HashMap<>(state.min), new HashMap<>(state.max));
                            newStateMax.min.put(vs, value2 + 1);
                            newStateMax.max.put(vs, max);
                            que.push(newStateMax);
                        }
                    } else if (opp == '<') {
                        if (max <= value2) {
                            handled = true;
                            var newState = new State(newFlow, new HashMap<>(state.min), new HashMap<>(state.max));
                            que.push(newState);
                        } else if (min < value2) {
                            handled = true;
                            var newStateMin = new State(state.flow, new HashMap<>(state.min), new HashMap<>(state.max));
                            newStateMin.min.put(vs, value2);
                            newStateMin.max.put(vs, max);
                            que.push(newStateMin);
                            var newStateMax = new State(newFlow, new HashMap<>(state.min), new HashMap<>(state.max));
                            newStateMax.min.put(vs, min);
                            newStateMax.max.put(vs, value2 - 1);
                            que.push(newStateMax);
                        }
                    }
                    if (handled) {
                        break;
                    }
                } else {
                    que.push(new State(op, state.min, state.max));
                    break;
                }
            }
        }
        return acc;
    }

    private static long part1(List<String> input) {
        Map<String, List<String>> operations = new HashMap<>();
        int index = 0;
        for (; !input.get(index).isEmpty(); ++index) {
            int start = input.get(index).indexOf('{');
            var name = input.get(index).substring(0, start);
            var ops = Arrays.stream(input.get(index).substring(start+1, input.get(index).length() - 1).split(",")).toList();
            operations.put(name, ops);
        }

        long sum = 0;
        for (index++; index < input.size(); ++index) {
            var parts = Arrays.stream(input.get(index).substring(1, input.get(index).length() - 1).split(",")).toList();
            String flow = "in";
            while (!flow.equals("A") && !flow.equals("R")) {
                var ops = operations.get(flow);
                for (String op : ops) {
                    if (op.contains(":")) {
                        var op2 = op.split(":");
                        var cond = op2[0];
                        var v = cond.charAt(0);
                        var opp = cond.charAt(1);
                        int value = parts.stream()
                                .filter(p -> p.charAt(0) == v)
                                .map(p -> Integer.parseInt(p.substring(2)))
                                .findFirst()
                                .orElseThrow();
                        var value2 = Integer.parseInt(cond.substring(2));
                        var matches = switch (opp) {
                            case '>' -> value > value2;
                            case '<' -> value < value2;
                            case '=' -> value == value2;
                            default -> throw new IllegalArgumentException(opp + " " + op);
                        };
                        if (matches) {
                            flow = op2[1];
                            break;
                        }
                    } else {
                        flow = op;
                        break;
                    }
                }
            }
            if (flow.equals("A")) {
                sum += parts.stream()
                        .map(p -> Long.parseLong(p.split("=")[1]))
                        .reduce(0L, Long::sum);
            }
        }
        return sum;
    }

    private record State(String flow, Map<String, Integer> min, Map<String, Integer> max) {

    }
}
