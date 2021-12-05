import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Increasing measurements: " + increasesCount(lineReader));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("With sliding window: " + increasesCountSliding(lineReader));
        }
    }

    public static int increasesCount(BufferedReader lineReader) throws IOException {
        int count = 0;
        int prevMeasurement = Integer.MAX_VALUE;
        String line;
        while ((line = lineReader.readLine()) != null) {
            int measurement = Integer.parseInt(line);
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
            third = Integer.parseInt(line);
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
