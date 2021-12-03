import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static final String INPUT = "input.txt";

    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = lineReader()) {
            System.out.println("Increasing measurements: " + increasesCount(lineReader));
        }
        try (BufferedReader lineReader = lineReader()) {
            System.out.println("With sliding window: " + increasesCountSliding(lineReader));
        }
    }

    private static BufferedReader lineReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(INPUT));
    }

    public static int increasesCount(BufferedReader lineReader) throws IOException {
        int count = 0;
        int prevMeasurement = Integer.MAX_VALUE;
        String line;
        while ((line = lineReader.readLine()) != null) {
            int measurement = Integer.valueOf(line);
            if (measurement > prevMeasurement) {
                count++;
            }
            prevMeasurement = measurement;
        }
        return count;
    }

    public static int increasesCountSliding(BufferedReader lineReader) throws IOException {
        int count = 0;
        boolean isFullWindow = false;
        int first = -1;
        int second = -1;
        int third = -1;
        String line;
        while ((line = lineReader.readLine()) != null) {
            int prevWindow = isFullWindow ? first + second + third : -1;
            first = second;
            second = third;
            third = Integer.valueOf(line);
            isFullWindow = first != -1 && second != -1 && third != -1;
            if (isFullWindow && prevWindow != -1) {
                int curWindow = first + second + third;
                if (curWindow > prevWindow) {
                    count++;
                }
            }
        }
        return count;
    }
}
