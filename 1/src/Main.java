import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static final String INPUT = "input.txt";

    public static void main(String[] args) throws IOException {
        try (BufferedReader lines = lines()) {
            System.out.println("Increasing measurements: " + increasesCount(lines));
        }
        try (BufferedReader lines = lines()) {
            System.out.println("With sliding window: " + increasesCountSliding(lines));
        }
    }

    private static BufferedReader lines() throws FileNotFoundException {
        return new BufferedReader(new FileReader(INPUT));
    }

    private static int increasesCount(BufferedReader lines) throws IOException {
        int count = 0;
        int prevMeasurement = Integer.MAX_VALUE;
        String line;
        while ((line = lines.readLine()) != null) {
            int measurement = Integer.valueOf(line);
            if (measurement > prevMeasurement) {
                count++;
            }
            prevMeasurement = measurement;
        }
        return count;
    }

    private static int increasesCountSliding(BufferedReader lines) throws IOException {
        int count = 0;
        boolean isFullWindow = false;
        int first = -1;
        int second = -1;
        int third = -1;
        String line;
        while ((line = lines.readLine()) != null) {
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
