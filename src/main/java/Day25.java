import util.Util;

import java.util.*;

public class Day25 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        Util.submitPart1(part1(input));
        Util.submitPart2(0);
    }

    private static long part1(List<String> input) {
        var graph = new HashMap<String, List<String>>();
        var list = new ArrayList<Node>();
        for (String row : input) {
            var s = row.split(": ");
            var to = s[1].split(" ");
            for (String str : to) {
                addConnection(graph, list, s[0], str);
            }
        }
        var source = graph.keySet().stream().findAny().orElseThrow();
        var goal = findGoal(graph, source);
        var used = new HashSet<Node>();
        var a = findPath(graph, source, goal, used);
        used.addAll(a);
        var b = findPath(graph, source, goal, used);
        used.addAll(b);
        var c = findPath(graph, source, goal, used);
        for (Node aa : a) {
            for (Node bb : b) {
                for (Node cc : c) {
                    var size = calc(graph, Arrays.asList(aa, bb, cc));
                    if (size != -1) {
                        return size;
                    }
                }
            }
        }
        return -1;
    }

    private static List<Node> findPath(Map<String, List<String>> graph, String source, String goal, HashSet<Node> used) {
        var seen = new HashSet<String>();
        var que = new LinkedList<Path>();
        que.add(new Path(source, Collections.emptyList()));
        while (!que.isEmpty()) {
            var path = que.pop();
            if (path.node.equals(goal)) {
                return path.nodes;
            }
            if (!seen.add(path.node)) {
                continue;
            }
            for (String neigh : graph.get(path.node)) {
                if (!used.contains(new Node(path.node, neigh)) && !used.contains(new Node(neigh, path.node))) {
                    var list = new ArrayList<>(path.nodes);
                    list.add(new Node(path.node, neigh));
                    que.add(new Path(neigh, list));
                }
            }
        }
        throw new IllegalStateException("No path found");
    }

    private static String findGoal(Map<String, List<String>> graph, String source) {
        var seen = new HashSet<String>();
        var que = new LinkedList<String>();
        que.add(source);
        String last = null;
        while (!que.isEmpty()) {
            var node = que.pop();
            if (!seen.add(node)) {
                continue;
            }
            last = node;
            que.addAll(graph.get(node));
        }
        return last;
    }

    private static Long calc(Map<String, List<String>> graph, List<Node> nodes) {
        var seen = new HashSet<String>();
        var que = new LinkedList<String>();
        que.add(graph.keySet().stream().findAny().orElseThrow());
        while (!que.isEmpty()) {
            var node = que.pop();
            if (!seen.add(node)) {
                continue;
            }
            for (String str : graph.get(node)) {
                if (!nodes.contains(new Node(node, str)) && !nodes.contains(new Node(str, node))) {
                    que.add(str);
                }
            }
        }
        if (seen.size() == graph.size()) {
            return -1L;
        }
        return (long) seen.size() * (graph.size() - seen.size());
    }

    private static void addConnection(Map<String, List<String>> graph, List<Node> list, String a, String b) {
        graph.putIfAbsent(a, new ArrayList<>());
        graph.get(a).add(b);
        graph.putIfAbsent(b, new ArrayList<>());
        graph.get(b).add(a);
        list.add(new Node(a, b));
    }

    private record Node(String from, String to) {
    }

    private record Path(String node, List<Node> nodes) {
    }
}
