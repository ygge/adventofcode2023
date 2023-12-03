import util.Util;

import java.util.ArrayList;
import java.util.List;

public class Day3 {

    public static void main(String[] args) {
        var inputs = Util.readBoard();
        Util.submitPart1(part1(inputs));
        Util.submitPart2(part2(inputs));
    }

    private static long part2(char[][] board) {
        long sum = 0;
        for (int y = 0; y < board.length; ++y) {
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] == '*') {
                    sum += calc(board, y, x);
                }
            }
        }
        return sum;
    }

    private static int part1(char[][] board) {
        int sum = 0;
        for (int y = 0; y < board.length; ++y) {
            int num = -1;
            int startX = -1;
            for (int x = 0; x < board[y].length; ++x) {
                if (board[y][x] >= '0' && board[y][x] <= '9') {
                    if (num == -1) {
                        num = board[y][x] - '0';
                        startX = x;
                    } else {
                        num = 10 * num + board[y][x] - '0';
                    }
                } else if (num != -1) {
                    if (isAdjacent(board, y, x-1, startX)) {
                        sum += num;
                    }
                    num = -1;
                }
            }
            if (num != -1) {
                if (isAdjacent(board, y, board.length-1, startX)) {
                    sum += num;
                }
            }
        }
        return sum;
    }

    private static int calc(char[][] board, int y, int x) {
        List<Integer> num = new ArrayList<>();
        if (y > 0) {
            if (x > 0 && board[y-1][x-1] >= '0' && board[y-1][x-1] <= '9') {
                int dx = x-1;
                while (dx >= 0 && board[y-1][dx] >= '0' && board[y-1][dx] <= '9') {
                    --dx;
                }
                num.add(getNum(board, y-1, dx+1));
                if (board[y-1][x] == '.' && x < board[y].length - 1 && board[y-1][x+1] >= '0' && board[y-1][x+1] <= '9') {
                    num.add(getNum(board, y-1, x+1));
                }
            } else if (board[y-1][x] >= '0' && board[y-1][x] <= '9') {
                num.add(getNum(board, y-1, x));
            } else if (x < board[y].length - 1 && board[y-1][x+1] >= '0' && board[y-1][x+1] <= '9') {
                num.add(getNum(board, y-1, x+1));
            }
        }
        if (x > 0 && board[y][x-1] >= '0' && board[y][x-1] <= '9') {
            int dx = x-1;
            while (dx >= 0 && board[y][dx] >= '0' && board[y][dx] <= '9') {
                --dx;
            }
            num.add(getNum(board, y, dx + 1));
        }
        if (x < board[y].length - 1 && board[y][x+1] >= '0' && board[y][x+1] <= '9') {
            num.add(getNum(board, y, x+1));
        }
        if (y < board.length - 1) {
            if (x > 0 && board[y+1][x-1] >= '0' && board[y+1][x-1] <= '9') {
                int dx = x-1;
                while (board[y+1][dx] >= '0' && board[y+1][x-1] <= '9') {
                    --dx;
                }
                num.add(getNum(board, y+1, dx+1));
                if (board[y+1][x] == '.' && x < board[y].length - 1 && board[y+1][x+1] >= '0' && board[y+1][x+1] <= '9') {
                    num.add(getNum(board, y+1, x+1));
                }
            } else if (board[y+1][x] >= '0' && board[y+1][x] <= '9') {
                num.add(getNum(board, y+1, x));
            } else if (x < board[y].length - 1 && board[y+1][x+1] >= '0' && board[y+1][x+1] <= '9') {
                num.add(getNum(board, y+1, x+1));
            }
        }
        if (num.size() == 2) {
            return num.get(0) * num.get(1);
        }
        return 0;
    }

    private static int getNum(char[][] board, int y, int x) {
        int num = 0;
        while (x < board[y].length) {
            if (board[y][x] >= '0' && board[y][x] <= '9') {
                num = 10 * num + board[y][x] - '0';
            } else {
                break;
            }
            ++x;
        }
        return num;
    }

    private static boolean isAdjacent(char[][] board, int y, int x, int startX) {
        if (y > 0) {
            for (int dx = Math.max(0, startX - 1); dx <= Math.min(x + 1, board[y].length - 1); ++dx) {
                if (board[y-1][dx] != '.' && (board[y-1][dx] < '0' || board[y-1][dx] > '9')) {
                    return true;
                }
            }
        }
        if (y < board.length - 1) {
            for (int dx = Math.max(0, startX - 1); dx <= Math.min(x + 1, board[y].length - 1); ++dx) {
                if (board[y+1][dx] != '.' && (board[y+1][dx] < '0' || board[y+1][dx] > '9')) {
                    return true;
                }
            }
        }
        if (startX > 0) {
            if (board[y][startX - 1] != '.' && (board[y][startX - 1] < '0' || board[y][startX - 1] > '9')) {
                return true;
            }
        }
        if (x < board[y].length - 1) {
            if (board[y][x + 1] != '.' && (board[y][x + 1] < '0' || board[y][x + 1] > '9')) {
                return true;
            }
        }
        return false;
    }
}
