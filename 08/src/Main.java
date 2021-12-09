import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Number of 1, 4, 7, 8 digits: " + countDigits(lineReader, 1, 4, 7, 8));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Sum of encoded numbers: " + sumNumbers(lineReader));
        }
    }

    public static int countDigits(BufferedReader lineReader, int... digitsIncludeArray) throws IOException {
        Set<Integer> digitsInclude = new HashSet<>();
        for (int digit : digitsIncludeArray) {
            if (digit < 0 || digit / 10 != 0) {
                throw new IllegalArgumentException("digits must be between 0-9");
            }
            digitsInclude.add(digit);
        }
        int count = 0;
        String line;
        while ((line = lineReader.readLine()) != null) {
            String[] signalPatterns = signalPatterns(line);
            SevenSegmentDecoder ssd = new SevenSegmentDecoder(signalPatterns);
            for (String encodedDigit : encodedDigits(line)) {
                int digit = ssd.decode(encodedDigit);
                if (digitsInclude.contains(digit)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int sumNumbers(BufferedReader lineReader) throws IOException {
        int sum = 0;
        String line;
        while ((line = lineReader.readLine()) != null) {
            String[] signalPatterns = signalPatterns(line);
            SevenSegmentDecoder ssd = new SevenSegmentDecoder(signalPatterns);
            int number = 0;
            for (String encodedDigit : encodedDigits(line)) {
                number *= 10;
                number += ssd.decode(encodedDigit);
            }
            sum += number;
        }
        return sum;
    }

    private static String[] signalPatterns(String line) {
        String signalPatterns = line.substring(0, line.indexOf(" |"));
        return signalPatterns.split(" ");
    }

    private static String[] encodedDigits(String line) {
        String encodedDigits = line.substring(line.indexOf("| ") + 2);
        return encodedDigits.split(" ");
    }
}
