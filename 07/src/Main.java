import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Fuel required: " + fuelRequired(lineReader));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Fuel required (actual): " + fuelRequiredActual(lineReader));
        }
    }

    public static int fuelRequired(BufferedReader lineReader) throws IOException {
        List<Integer> locs = crabLocs(lineReader);
        // for part 1, the optimal location for the crabs to converge at is the median of their starting locations
        Collections.sort(locs);
        int median = locs.get(locs.size() / 2);
        // sum the steps each crab takes to reach the median location
        return locs.stream()
                .reduce(0, (fuelSubtotal, loc) -> fuelSubtotal + Math.abs(loc - median));
    }

    public static int fuelRequiredActual(BufferedReader lineReader) throws IOException {
        List<Integer> locs = crabLocs(lineReader);
        // for part 2, we will do a binary search to find the best location to converge at - we have found it when
        // adjusting it either left or right by one would increase the resultant fuel usage
        int low = locs.stream().reduce(locs.get(0), Integer::min);
        int high = locs.stream().reduce(locs.get(0), Integer::max);
        int cur;
        while (true) {
            cur = (low + high) / 2;
            int fuelCur = fuelUsedActual(locs, cur);
            int fuelPrev = cur == 0 ? Integer.MAX_VALUE : fuelUsedActual(locs, cur - 1);
            int fuelNext = cur == locs.size() - 1 ? Integer.MAX_VALUE : fuelUsedActual(locs, cur + 1);
            if (fuelCur <= fuelPrev && fuelCur <= fuelNext) {
                // found the optimal location
                return fuelCur;
            } else if (fuelPrev < fuelCur) {
                // narrow the search window downwards
                high = cur;
            } else {
                // narrow the search window upwards
                low = cur;
            }
        }
    }

    private static int fuelUsedActual(List<Integer> locs, int converge) {
        int fuelUsed = 0;
        for (int loc : locs) {
            int dist = Math.abs(loc - converge);
            // use the triangle number formula to calculate the fuel usage per crab
            fuelUsed += (dist * (dist + 1)) / 2;
        }
        return fuelUsed;
    }

    private static List<Integer> crabLocs(BufferedReader lineReader) throws IOException {
        String[] locStrs = lineReader.readLine().split(",");
        lineReader.close();
        List<Integer> locs = new ArrayList<>();
        for (String locStr : locStrs) {
            locs.add(Integer.parseInt(locStr));
        }
        return locs;
    }
}