public class Player {
    private static final int TRACK_SIZE = 10;
    private static final int SCORE_TO_WIN = 1000;

    private int pawnPos;
    private int score = 0;

    public Player(int startingPos) {
        this.pawnPos = startingPos;
    }

    public void haveGo(DeterministicDie die) {
        int roll = die.roll(3);
        pawnPos = (((pawnPos + roll) - 1) % TRACK_SIZE) + 1;
        score += pawnPos;
    }

    public boolean hasWon() {
        return score >= SCORE_TO_WIN;
    }

    public int getScore() {
        return score;
    }
}
