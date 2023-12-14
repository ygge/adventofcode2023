import util.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day14 {

    public static void main(String[] args) {
        var board = Util.readBoard();
        Util.submitPart1(part1(board));
        Util.submitPart2(part2(board));
    }

    private static long part2(char[][] board) {
        Map<Board, Integer> seen = new HashMap<>();
        seen.put(new Board(board), 0);
        int goal = 1000000000;
        for (int t = 1; t <= goal; ++t) {
            char[][] board2 = tilt(board);
            var b = new Board(board2);
            board = board2;
            var prev = seen.get(b);
            if (prev == null) {
                seen.put(b, t);
            } else {
                int diff = t - prev;
                while (t + diff < goal) {
                    t += diff;
                }
            }
        }
        return score(board);
    }

    private static int score(char[][] board) {
        int sum = 0;
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] == 'O') {
                    sum += board.length - y;
                }
            }
        }
        return sum;
    }

    private static char[][] tilt(char[][] board) {
        var newBoard = tiltUp(board);
        newBoard = tiltLeft(newBoard);
        newBoard = tiltDown(newBoard);
        newBoard = tiltRight(newBoard);
        return newBoard;
    }

    private static char[][] tiltRight(char[][] board) {
        var newBoard = new char[board.length][board[0].length];
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                newBoard[y][x] = board[y][x] == 'O' ? '.' : board[y][x];
            }
        }
        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < board.length; ++i) {
            pos.put(i, board.length - 1);
        }
        for (int y = 0; y < board.length; ++y) {
            for (int x = board[y].length - 1; x >= 0; --x) {
                char c = board[y][x];
                if (c == '#') {
                    pos.put(y, x - 1);
                } else if (c == 'O') {
                    newBoard[y][pos.get(y)] = 'O';
                    pos.put(y, pos.get(y) - 1);
                }
            }
        }
        return newBoard;
    }

    private static char[][] tiltDown(char[][] board) {
        var newBoard = new char[board.length][board[0].length];
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                newBoard[y][x] = board[y][x] == 'O' ? '.' : board[y][x];
            }
        }
        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < board[0].length; ++i) {
            pos.put(i, board[0].length - 1);
        }
        for (int y = board.length - 1; y >= 0; --y) {
            for (int x = 0; x < board[y].length; ++x) {
                char c = board[y][x];
                if (c == '#') {
                    pos.put(x, y - 1);
                } else if (c == 'O') {
                    newBoard[pos.get(x)][x] = 'O';
                    pos.put(x, pos.get(x) - 1);
                }
            }
        }
        return newBoard;
    }

    private static char[][] tiltLeft(char[][] board) {
        var newBoard = new char[board.length][board[0].length];
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                newBoard[y][x] = board[y][x] == 'O' ? '.' : board[y][x];
            }
        }
        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < board.length; ++i) {
            pos.put(i, 0);
        }
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                char c = board[y][x];
                if (c == '#') {
                    pos.put(y, x + 1);
                } else if (c == 'O') {
                    newBoard[y][pos.get(y)] = 'O';
                    pos.put(y, pos.get(y) + 1);
                }
            }
        }
        return newBoard;
    }

    private static char[][] tiltUp(char[][] board) {
        var newBoard = new char[board.length][board[0].length];
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                newBoard[y][x] = board[y][x] == 'O' ? '.' : board[y][x];
            }
        }
        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < board[0].length; ++i) {
            pos.put(i, 0);
        }
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                char c = board[y][x];
                if (c == '#') {
                    pos.put(x, y + 1);
                } else if (c == 'O') {
                    newBoard[pos.get(x)][x] = 'O';
                    pos.put(x, pos.get(x) + 1);
                }
            }
        }
        return newBoard;
    }

    private static long part1(char[][] board) {
        long sum = 0;
        Map<Integer, Integer> loads = new HashMap<>();
        for (int i = 0; i < board[0].length; ++i) {
            loads.put(i, board.length);
        }
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                char c = board[y][x];
                if (c == '#') {
                    loads.put(x, board.length - y - 1);
                } else if (c == 'O') {
                    var v = loads.get(x);
                    sum += v;
                    loads.put(x, v-1);
                }
            }
        }
        return sum;
    }

    private static class Board {
        private final char[][] board;

        private Board(char[][] board) {
            this.board = board;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Board board1 = (Board) o;
            return Arrays.deepEquals(board, board1.board);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(board);
        }
    }
}
