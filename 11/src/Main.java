import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Flashes after 100 steps: " + flashesAfterNSteps(lineReader, 100));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("First step with synchronised flashes: " + firstStepSynchronisedFlashes(lineReader));
        }
    }

    public static int flashesAfterNSteps(BufferedReader lineReader, int steps) throws IOException {
        Counter flashCounter = new Counter();
        Collection<Octopus> octopuses = octopuses(lineReader, flashCounter);
        for (int i = 0; i < steps; i++) {
            for (Octopus octopus : octopuses) {
                octopus.increaseEnergy();
            }
            for (Octopus octopus : octopuses) {
                octopus.resetFlash();
            }
        }
        return flashCounter.getCount();
    }

    public static int firstStepSynchronisedFlashes(BufferedReader lineReader) throws IOException {
        Collection<Octopus> octopuses = octopuses(lineReader, new Counter());
        int steps = 0;
        while (true) {
            for (Octopus octopus : octopuses) {
                octopus.increaseEnergy();
            }
            steps++;
            long flashes = octopuses.stream()
                    .filter(Octopus::justFlashed)
                    .count();
            if (flashes == octopuses.size()) {
                return steps;
            }
            for (Octopus octopus : octopuses) {
                octopus.resetFlash();
            }
        }
    }

    private static Collection<Octopus> octopuses(BufferedReader lineReader, Counter counter) throws IOException {
        int[][] valsArray = Utils.readIntArray(lineReader);
        Octopus[][] octopusesArray = new Octopus[valsArray.length][valsArray[0].length];
        for (int row = 0; row < valsArray.length; row++) {
            for (int col = 0; col < valsArray[row].length; col++) {
                octopusesArray[row][col] = new Octopus(valsArray[row][col], counter);
            }
        }
        List<Octopus> octopuses = new ArrayList<>();
        for (int row = 0; row < octopusesArray.length; row++) {
            for (int col = 0; col < octopusesArray[row].length; col++) {
                Octopus octopus = octopusesArray[row][col];
                octopuses.add(octopus);
                if (row > 0) {
                    octopus.addNeighbour(octopusesArray[row - 1][col]);
                    if (col > 0) {
                        octopus.addNeighbour(octopusesArray[row - 1][col - 1]);
                    }
                    if (col < octopusesArray[row].length - 1) {
                        octopus.addNeighbour(octopusesArray[row - 1][col + 1]);
                    }
                }
                if (col > 0) {
                    octopus.addNeighbour(octopusesArray[row][col - 1]);
                }
                if (col < octopusesArray[row].length - 1) {
                    octopus.addNeighbour(octopusesArray[row][col + 1]);
                }
                if (row < octopusesArray.length - 1) {
                    octopus.addNeighbour(octopusesArray[row + 1][col]);
                    if (col > 0) {
                        octopus.addNeighbour(octopusesArray[row + 1][col - 1]);
                    }
                    if (col < octopusesArray[row].length - 1) {
                        octopus.addNeighbour(octopusesArray[row + 1][col + 1]);
                    }
                }
            }
        }
        return octopuses;
    }
}
