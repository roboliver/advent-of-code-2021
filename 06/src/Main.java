import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.LongStream;

public class Main {
    private static final int SPAWN_FREQ = 7; // frequency with which a lanternfish spawns new lanternfish
    private static final int SPINUP = 2; // extra days a newly spawned lanternfish takes to enter the spawning cycle
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Lanternfish after 80 days: " + lanternfishAfterNDays(lineReader, 80));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Lanternfish after 256 days: " + lanternfishAfterNDays(lineReader, 256));
        }
    }

    public static long lanternfishAfterNDays(BufferedReader lineReader, int days) throws IOException {
        if (days < 0) {
            throw new IllegalArgumentException("days must not be negative");
        }
        // store fish as an array, with the index representing a timer position and the value as the number of fish at
        // that timer position
        long[] fishCounts = lanternfishCounts(lineReader);
        for (int day = 0; day < days; day++) {
            long[] fishCountsNew = new long[SPAWN_FREQ + SPINUP];
            for (int i = 0; i < fishCounts.length; i++) {
                if (i == 0) {
                    fishCountsNew[SPAWN_FREQ - 1] += fishCounts[i];
                    fishCountsNew[(SPAWN_FREQ - 1) + SPINUP] += fishCounts[i];
                } else {
                    fishCountsNew[i - 1] += fishCounts[i];
                }
            }
            fishCounts = fishCountsNew;
        }
        return LongStream.of(fishCounts).sum();
    }

    private static long[] lanternfishCounts(BufferedReader lineReader) throws IOException {
        String[] fishStrs = lineReader.readLine().split(",");
        long[] fishCounts = new long[SPAWN_FREQ + SPINUP];
        for (String fishStr : fishStrs) {
            fishCounts[Integer.parseInt(fishStr)]++;
        }
        return fishCounts;
    }
}
