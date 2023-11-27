package util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {

    private static final String YEAR = "2023";
    private static final String COOKIE_FILE = "cookie.txt";

    private static boolean verifySubmission = false;

    private Util() {}

    public static String readString() {
        final List<String> strings = readStrings();
        if (strings.size() != 1) {
            throw new IllegalStateException(String.format("File does not contain exactly one row, got %d", strings.size()));
        }
        return strings.get(0);
    }

    public static int readInt() {
        return Integer.parseInt(readString());
    }

    public static List<String> readStrings() {
        return readFile(Function.identity());
    }

    public static List<Integer> readInts() {
        return readFile(Integer::parseInt);
    }

    public static List<Long> readLongs() {
        return readFile(Long::parseLong);
    }

    public static <T> List<T> read(Function<String, T> parser) {
        return readFile(parser);
    }

    public static List<List<Integer>> readIntLists() {
        return readFile(str -> Arrays.stream(str.split("\\D+")).map(Integer::parseInt).collect(Collectors.toList()));
    }

    public static List<List<Pos>> readPosLists() {
        return readIntLists().stream()
                .map(Util::posList)
                .collect(Collectors.toList());
    }

    public static List<List<Pos>> readLines() {
        return readPosLists().stream()
                .map(Util::toLine)
                .collect(Collectors.toList());
    }

    public static char[][] readBoard() {
        var content = readFile(String::toCharArray);
        char[][] board = new char[content.size()][];
        for (int i = 0; i < content.size(); ++i) {
            board[i] = content.get(i);
        }
        return board;
    }

    public static int[][] readIntBoard() {
        var content = readFile(String::toCharArray);
        int[][] board = new int[content.size()][];
        for (int i = 0; i < content.size(); ++i) {
            var row = content.get(i);
            board[i] = new int[row.length];
            for (int j = 0; j < row.length; ++j) {
                board[i][j] = row[j]-'0';
            }
        }
        return board;
    }

    public static boolean[][] readBoard(char trueChar, char falseChar) {
        var content = readFile(String::toCharArray);
        boolean[][] board = new boolean[content.size()][];
        for (int i = 0; i < content.size(); ++i) {
            board[i] = new boolean[content.get(i).length];
            for (int j = 0; j < content.get(i).length; ++j) {
                char c = content.get(i)[j];
                if (c != trueChar && c != falseChar) {
                    throw new RuntimeException(String.format("Char %c is neither true nor false", c));
                }
                board[i][j] = c == trueChar;
            }
        }
        return board;
    }

    public static void verifySubmission() {
        verifySubmission = true;
    }

    public static void submitPart1(int answer) {
        submitPart1(Integer.toString(answer));
    }

    public static void submitPart1(long answer) {
        submitPart1(Long.toString(answer));
    }

    public static void submitPart1(String answer) {
        submit(1, answer);
    }

    public static void submitPart2(int answer) {
        submitPart2(Integer.toString(answer));
    }
    public static void submitPart2(long answer) {
        submitPart2(Long.toString(answer));
    }

    public static void submitPart2(String answer) {
        submit(2, answer);
    }

    public static List<String> toStringList(String str) {
        return Arrays.asList(str.split("\n"));
    }

    private static List<Pos> toLine(List<Pos> posList) {
        if (posList.size() != 2) {
            throw new IllegalArgumentException(String.format("Got %d positions, need 2 for a line", posList.size()));
        }
        int x1 = posList.get(0).x;
        int y1 = posList.get(0).y;
        int x2 = posList.get(1).x;
        int y2 = posList.get(1).y;
        if (x1 == x2) {
            List<Pos> pos = new ArrayList<>();
            for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); ++i) {
                pos.add(new Pos(x1, i));
            }
            return pos;
        }
        if (y1 == y2) {
            List<Pos> pos = new ArrayList<>();
            for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); ++i) {
                pos.add(new Pos(i, y1));
            }
            return pos;
        }
        List<Pos> pos = new ArrayList<>();
        for (int i = 0; i <= Math.max(x1, x2)-Math.min(x1, x2); ++i) {
            pos.add(new Pos(x1+(x1 < x2 ? 1 : -1)*i, y1+(y1 < y2 ? 1 : -1)*i));
        }
        return pos;
    }

    private static List<Pos> posList(List<Integer> intList) {
        if (intList.size()%2 == 1) {
            throw new IllegalArgumentException(String.format("Cannot convert odd list of numbers to positions, got %d numbers", intList.size()));
        }
        List<Pos> ret = new ArrayList<>();
        for (int i = 0; i < intList.size(); i += 2) {
            ret.add(new Pos(intList.get(i), intList.get(i+1)));
        }
        return ret;
    }

    private static void submit(int part, String answer) {
        System.out.printf("Submitting part %d: %s\n", part, answer);
        if (verifySubmission) {
            System.out.println("Press y to proceed, anything else to skip");
            try {
                var in = new BufferedReader(new InputStreamReader(System.in));
                var input = in.readLine();
                if (!input.equalsIgnoreCase("y")) {
                    return;
                }
            } catch (IOException e) {
                return;
            }
        }
        final String day = getDay();
        final var data = String.format("level=%d&answer=%s", part, URLEncoder.encode(answer, StandardCharsets.UTF_8));
        try {
            final var url = new URL(baseUrl(day) + "answer");
            final var urlConnection = createUrlConnection(url);
            urlConnection.setDoOutput(true);

            final var out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(data);
            out.flush();

            final var in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            boolean print = false;
            boolean success = false;
            while ((line = in.readLine()) != null) {
                if (line.equals("</main>")) {
                    print = false;
                }
                if (print) {
                    System.out.println(line);
                    if (line.contains("You gave an answer too")) {
                        var p = line.indexOf("s left to wait");
                        if (p != -1) {
                            var p2 = line.indexOf(' ', p-5);
                            int s = Integer.parseInt(line.substring(p2+1, p));
                            System.out.printf("Submitting again in %ds\n", s);
                            try {
                                Thread.sleep(s*1000L);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            submit(part, answer);
                            return;
                        }
                    }
                    if (line.contains("star")) {
                        success = true;
                    }
                }
                if (line.equals("<main>")) {
                    print = true;
                }
            }
            out.close();
            in.close();
            if (!success) {
                throw new IllegalStateException("No star");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> readFile(Function<String, T> converter) {
        final String day = getDay();
        final var fileName = String.format("/%s.in", day);
        final var filePath = filePath(fileName);
        if (!Files.exists(Path.of(filePath))) {
            var data = fetchData(day);
            writeData(data, fileName);
        }
        try {
            return readData(Files.newInputStream(Path.of(filePath))).stream()
                    .map(converter)
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getDay() {
        final var className = getCallerClassName();
        return className.substring(3);
    }

    private static List<String> readData(InputStream ins) {
        final var in = new BufferedReader(new InputStreamReader(ins));
        final var input = new ArrayList<String>();
        String row;
        try {
            while ((row = in.readLine()) != null) {
                input.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return input;
    }

    private static void writeData(String data, String fileName) {
        String path = filePath(fileName);
        try {
            var out = new BufferedWriter(new FileWriter(path));
            out.write(data);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String filePath(String fileName) {
        return System.getProperty("user.dir") + File.separator
                + "src" + File.separator
                + "main" + File.separator
                + "resources" + File.separator
                + fileName;
    }

    private static String getCallerClassName() {
        for (StackTraceElement stackTraceElement : new RuntimeException().getStackTrace()) {
            if (!stackTraceElement.getClassName().equals("util.Util")) {
                return stackTraceElement.getClassName();
            }
        }
        return "";
    }

    private static String fetchData(String day) {
        try {
            var url = new URL(baseUrl(day) + "input");
            URLConnection urlConnection = createUrlConnection(url);
            return Util.readData(urlConnection.getInputStream()).stream()
                    .reduce(null, (a, b) -> a == null ? b : a + "\n" + b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String baseUrl(String day) {
        return "https://adventofcode.com/" + YEAR + "/day/" + day + "/";
    }

    private static URLConnection createUrlConnection(URL url) throws IOException {
        var urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Cookie", "session=" + Files.readString(Path.of(COOKIE_FILE)));
        return urlConnection;
    }
}
