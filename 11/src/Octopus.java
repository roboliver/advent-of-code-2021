import java.util.ArrayList;
import java.util.List;

public class Octopus {
    private static final int MIN_ENERGY = 0;
    private static final int MAX_ENERGY = 9;
    private int energy;
    private final List<Octopus> neighbours = new ArrayList<>();
    private final Counter flashCounter;
    private boolean flashed = false;

    public Octopus(int energy, Counter flashCounter) {
        if (energy < MIN_ENERGY || energy > MAX_ENERGY) {
            throw new IllegalArgumentException(String.format("energy must be between %d and %d", MIN_ENERGY, MAX_ENERGY));
        }
        this.energy = energy;
        this.flashCounter = flashCounter;
    }

    public void addNeighbour(Octopus neighbour) {
        neighbours.add(neighbour);
    }

    public void increaseEnergy() {
        energy++;
        if (energy > MAX_ENERGY && !flashed) {
            flashed = true;
            flashCounter.increment();
            for (Octopus neighbour : neighbours) {
                neighbour.increaseEnergy();
            }
        }
    }

    public void resetFlash() {
        if (flashed) {
            energy = 0;
            flashed = false;
        }
    }

    public boolean justFlashed() {
        return flashed;
    }
}
