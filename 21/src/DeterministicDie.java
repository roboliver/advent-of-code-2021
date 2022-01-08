public class DeterministicDie {
    private int nextRoll = 1;
    private final int sides;
    private int rollCount = 0;

    public DeterministicDie(int sides) {
        if (sides < 1) {
            throw new IllegalArgumentException("die must have at least 1 side");
        }
        this.sides = sides;
    }

    public int roll(int times) {
        int roll = 0;
        for (int i = 0; i < times; i++) {
            roll += roll();
        }
        return roll;
    }

    public int roll() {
        int roll = nextRoll;
        nextRoll = nextRoll == sides ? 1 : nextRoll + 1;
        rollCount++;
        return roll;
    }

    public int rollCount() {
        return rollCount;
    }
}
