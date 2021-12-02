import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static final String INPUT = "input.txt";

    public static void main(String[] args) throws IOException {
        try (BufferedReader lines = lines()) {
            System.out.println("Distance: " + distance(lines));
        }
        try (BufferedReader lines = lines()) {
            System.out.println("Distance (actual): " + distanceActual(lines));
        }
    }

    private static BufferedReader lines() throws FileNotFoundException {
        return new BufferedReader(new FileReader(INPUT));
    }

    private static int distance(BufferedReader lines) throws IOException {
        int horizontal = 0;
        int depth = 0;
        String line;
        while ((line = lines.readLine()) != null) {
            String[] split = line.split(" ");
            String lineDir = split[0];
            int lineDist = Integer.valueOf(split[1]);
            switch (lineDir) {
                case "forward":
                    horizontal += lineDist;
                    break;
                case "down":
                    depth += lineDist;
                    break;
                case "up":
                    depth -= lineDist;
                    break;
            }
        }
        return horizontal * depth;
    }

    private static int distanceActual(BufferedReader lines) throws IOException {
        int horizontal = 0;
        int depth = 0;
        int aim = 0;
        String line;
        while ((line = lines.readLine()) != null) {
            String[] split = line.split(" ");
            String lineDir = split[0];
            int lineDist = Integer.valueOf(split[1]);
            switch (lineDir) {
                case "forward":
                    horizontal += lineDist;
                    depth += aim * lineDist;
                    break;
                case "down":
                    aim += lineDist;
                    break;
                case "up":
                    aim -= lineDist;
                    break;
            }
        }
        return horizontal * depth;
    }
}
