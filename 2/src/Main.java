import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static final String INPUT = "input.txt";

    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = lineReader()) {
            System.out.println("Distance: " + distance(lineReader));
        }
        try (BufferedReader lineReader = lineReader()) {
            System.out.println("Distance (actual): " + distanceActual(lineReader));
        }
    }

    private static BufferedReader lineReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(INPUT));
    }

    public static int distance(BufferedReader lineReader) throws IOException {
        int horizontal = 0;
        int depth = 0;
        String line;
        while ((line = lineReader.readLine()) != null) {
            String[] lineParts = line.split(" ");
            String dir = lineParts[0];
            int dist = Integer.valueOf(lineParts[1]);
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
            int dist = Integer.valueOf(lineParts[1]);
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
