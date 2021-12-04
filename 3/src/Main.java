import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String INPUT = "input.txt";

    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = inputLineReader()) {
            System.out.println("Power consumption: " + powerConsumption(lineReader));
        }
        try (BufferedReader lineReader = inputLineReader()) {
            System.out.println("Life support rating: " + lifeSupportRating(lineReader));
        }
    }

    private static BufferedReader inputLineReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(INPUT));
    }

    public static int powerConsumption(BufferedReader lineReader) throws IOException {
        // build up an array where each position represents a digit, and the value at each position represents the
        // number of 1s at that position minus the number of 0s -- so a positive value means more 1s than 0s, a negative
        // value means more 0s than 1s
        String line = lineReader.readLine();
        int[] digitOnesMinusZeros = new int[line.length()];
        while (line != null) {
            for (int i = 0; i < line.length(); i++) {
                char bit = line.charAt(i);
                switch (bit) {
                    case '0':
                        digitOnesMinusZeros[i]--;
                        break;
                    case '1':
                        digitOnesMinusZeros[i]++;
                        break;
                }
            }
            line = lineReader.readLine();
        }
        int gamma = calcPowerComponentRate(digitOnesMinusZeros, true);
        int epsilon = calcPowerComponentRate(digitOnesMinusZeros, false);
        return gamma * epsilon;
    }

    private static int calcPowerComponentRate(int[] digitOnesMinusZeros, boolean gammaNotEpsilon) {
        int rate = 0;
        for (int i = 0; i < digitOnesMinusZeros.length; i++) {
            boolean moreOnesThanZeros = digitOnesMinusZeros[i] > 0;
            boolean addOne = moreOnesThanZeros && gammaNotEpsilon || !moreOnesThanZeros && !gammaNotEpsilon;
            rate *= 2;
            rate += addOne ? 1 : 0;
        }
        return rate;
    }

    public static int lifeSupportRating(BufferedReader lineReader) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            lines.add(line);
        }
        int oxygenGenRating = calcGasMachineRating(lines, 0, true);
        int co2ScrubberRating = calcGasMachineRating(lines, 0, false);
        return oxygenGenRating * co2ScrubberRating;
    }

    private static int calcGasMachineRating(List<String> lines, int digit, boolean oxygenNotCo2) throws IOException {
        while (lines.size() > 1) {
            // separate lines based on their value of the applicable digit (indexed from the left, starting at 0)
            List<String> zeroLines = new ArrayList<>();
            List<String> oneLines = new ArrayList<>();
            for (String line : lines) {
                char bit = line.charAt(digit);
                switch (bit) {
                    case '0':
                        zeroLines.add(line);
                        break;
                    case '1':
                        oneLines.add(line);
                        break;
                }
            }
            // update the lines to evaluate to the correct subset to use in the next iteration
            if (oxygenNotCo2) {
                lines = oneLines.size() >= zeroLines.size() ? oneLines : zeroLines;
            } else {
                lines = oneLines.size() >= zeroLines.size() ? zeroLines : oneLines;
            }
            digit++;
        }
        return Integer.valueOf(lines.get(0), 2);
    }
}
