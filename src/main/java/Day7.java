import util.Util;

import java.util.*;

public class Day7 {

    public static void main(String[] args) {
        var inputs = Util.readStrings();
        Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static long part2(List<String> inputs) {
        var hands = inputs.stream()
                .map(r -> r.split(" "))
                .map(r -> new Hand2(r[0], Integer.parseInt(r[1])))
                .sorted()
                .toList();
        long sum = 0;
        long index = 0;
        for (Hand2 hand : hands) {
            sum += (++index)*hand.bid;
        }
        return sum;
    }

    private static long part1(List<String> inputs) {
        var hands = inputs.stream()
                .map(r -> r.split(" "))
                .map(r -> new Hand(r[0], Integer.parseInt(r[1])))
                .sorted()
                .toList();
        long sum = 0;
        long index = 0;
        for (Hand hand : hands) {
            sum += (++index)*hand.bid;
        }
        return sum;
    }

    private record Hand2(String hand, int bid) implements Comparable<Hand2> {

        @Override
        public int compareTo(Hand2 other) {
            var kind = kind();
            var oKind = other.kind();
            if (kind != oKind) {
                return oKind.ordinal() - kind.ordinal();
            }
            for (int i = 0; i < hand.length(); ++i) {
                if (hand.charAt(i) != other.hand.charAt(i)) {
                    return value(hand.charAt(i)) - value(other.hand.charAt(i));
                }
            }
            return 0;
        }

        private int value(char card) {
            if (card >= '2' && card <= '9') {
                return card-'0';
            }
            return switch (card) {
                case 'T' -> 10;
                case 'J' -> 1;
                case 'Q' -> 12;
                case 'K' -> 13;
                case 'A' -> 14;
                default -> throw new IllegalStateException(card + "");
            };
        }

        private Kind kind() {
            if (hand.contains("J")) {
                Set<Character> seen = new HashSet<>();
                for (int i = 0; i < hand.length(); ++i) {
                    seen.add(hand.charAt(i));
                }
                if (seen.size() == 1) {
                    return Kind.FIVE;
                }
                var best = Kind.HIGH;
                for (Character c : seen) {
                    var h = hand.replace('J', c);
                    Map<Integer, Integer> cardValues = new HashMap<>();
                    for (int i = 0; i < h.length(); ++i) {
                        int v = value(h.charAt(i));
                        cardValues.put(v, cardValues.getOrDefault(v, 0) + 1);
                    }
                    var kind = kind(cardValues);
                    if (kind.ordinal() < best.ordinal()) {
                        best = kind;
                    }
                }
                return best;
            } else {
                Map<Integer, Integer> cardValues = new HashMap<>();
                for (int i = 0; i < hand.length(); ++i) {
                    int v = value(hand.charAt(i));
                    cardValues.put(v, cardValues.getOrDefault(v, 0) + 1);
                }
                return kind(cardValues);
            }
        }

        private static Kind kind(Map<Integer, Integer> cardValues) {
            var set = cardValues.keySet();
            if (set.size() == 1) {
                return Kind.FIVE;
            }
            if (set.size() == 2) {
                if (cardValues.containsValue(4)) {
                    return Kind.FOUR;
                }
                return Kind.FULL;
            }
            if (cardValues.containsValue(3)) {
                return Kind.THREE;
            }
            if (set.size() == 3) {
                return Kind.TWO;
            }
            if (set.size() == 4) {
                return Kind.ONE;
            }
            return Kind.HIGH;
        }
    }

    private record Hand(String hand, int bid) implements Comparable<Hand> {

        @Override
        public int compareTo(Hand other) {
            var kind = kind();
            var oKind = other.kind();
            if (kind != oKind) {
                return oKind.ordinal() - kind.ordinal();
            }
            for (int i = 0; i < hand.length(); ++i) {
                if (hand.charAt(i) != other.hand.charAt(i)) {
                    return value(hand.charAt(i)) - value(other.hand.charAt(i));
                }
            }
            return 0;
        }

        private int value(char card) {
            if (card >= '2' && card <= '9') {
                return card-'0';
            }
            return switch (card) {
                case 'T' -> 10;
                case 'J' -> 11;
                case 'Q' -> 12;
                case 'K' -> 13;
                case 'A' -> 14;
                default -> throw new IllegalStateException(card + "");
            };
        }

        private Kind kind() {
            Map<Integer, Integer> cardValues = new HashMap<>();
            for (int i = 0; i < hand.length(); ++i) {
                int v = value(hand.charAt(i));
                cardValues.put(v, cardValues.getOrDefault(v, 0) + 1);
            }
            var set = cardValues.keySet();
            if (set.size() == 1) {
                return Kind.FIVE;
            }
            if (set.size() == 2) {
                if (cardValues.containsValue(4)) {
                    return Kind.FOUR;
                }
                return Kind.FULL;
            }
            if (cardValues.containsValue(3)) {
                return Kind.THREE;
            }
            if (set.size() == 3) {
                return Kind.TWO;
            }
            if (set.size() == 4) {
                return Kind.ONE;
            }
            return Kind.HIGH;
        }
    }

    private enum Kind {
        FIVE,
        FOUR,
        FULL,
        THREE,
        TWO,
        ONE,
        HIGH
    }
}
