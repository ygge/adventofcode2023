package util;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ParseUtil {

    public static <T> long sumLong(Collection<T> collection, Function<T, Long> func) {
        return collection.stream()
                .map(func)
                .reduce(Long::sum)
                .orElse(0L);
    }

    public static <T> int sum(Collection<T> collection, Function<T, Integer> func) {
        return collection.stream()
                .map(func)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public static <T> int sum(List<T> list, BiFunction<T, Integer, Integer> func) {
        int sum = 0;
        for (int i = 0; i < list.size(); ++i) {
            sum += func.apply(list.get(i), i);
        }
        return sum;
    }

    public static void cells(char[][] board, BiFunction<Integer, Integer, Boolean> func) {
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                if (!func.apply(x, y)) {
                    return;
                }
            }
        }
    }

    public static void neighbours(char[][] board, int x, int y, BiFunction<Integer, Integer, Boolean> func) {
        for (int dy = -1; dy <= 1; ++dy) {
            for (int dx = -1; dx <= 1; ++dx) {
                if ((dy != 0 || dx != 0) && y >= 0 && y < board.length && x >= 0 && x < board[y].length) {
                    if (func.apply(y+dy, x+dy)) {
                        return;
                    }
                }
            }
        }
    }
}
