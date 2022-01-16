import java.util.List;

public class DeterministicDie implements Die {
    private int nextRoll = 1;
    private final int sides;
    private int rollCount = 0;

    public DeterministicDie(int sides) {
        if (sides < 1) {
            throw new IllegalArgumentException("die must have at least 1 side");
        }
        this.sides = sides;
    }

    @Override
    public List<Integer> roll() {
        int roll = nextRoll;
        nextRoll = nextRoll == sides ? 1 : nextRoll + 1;
        rollCount++;
        return List.of(roll);
    }

    public int rollCount() {
        return rollCount;
    }
}
