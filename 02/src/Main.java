import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Distance: " + distance(lineReader));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Distance (actual): " + distanceActual(lineReader));
        }
    }

    public static int distance(BufferedReader lineReader) throws IOException {
        int horizontal = 0;
        int depth = 0;
        String line;
        while ((line = lineReader.readLine()) != null) {
            String[] lineParts = line.split(" ");
            String dir = lineParts[0];
            int dist = Integer.parseInt(lineParts[1]);
            switch (dir) {
                case "forward":
                    horizontal += dist;
                    break;
                case "down":
                    depth += dist;
                    break;
                case "up":
                    depth -= dist;
                    break;
            }
        }
        return horizontal * depth;
    }

    public static int distanceActual(BufferedReader lineReader) throws IOException {
        int horizontal = 0;
        int depth = 0;
        int aim = 0;
        String line;
        while ((line = lineReader.readLine()) != null) {
            String[] lineParts = line.split(" ");
            String dir = lineParts[0];
            int dist = Integer.parseInt(lineParts[1]);
            switch (dir) {
                case "forward":
                    horizontal += dist;
                    depth += aim * dist;
                    break;
                case "down":
                    aim += dist;
                    break;
                case "up":
                    aim -= dist;
                    break;
            }
        }
        return horizontal * depth;
    }
}
