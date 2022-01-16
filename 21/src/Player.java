import java.util.Arrays;

public class Player {
    private static final int TRACK_SIZE = 10;

    private final int pawnPos;
    private final int score;

    public Player(int startingPos) {
        if (startingPos < 1 || startingPos > TRACK_SIZE) {
            throw new IllegalArgumentException("starting position must be a valid track position: between 1 and "
                    + TRACK_SIZE);
        }
        this.pawnPos = startingPos;
        this.score = 0;
    }

    private Player(int pawnPos, int score) {
        this.pawnPos = pawnPos;
        this.score = score;
    }

    public Player haveGo(int roll) {
        int pawnPosNew = (((this.pawnPos + roll) - 1) % TRACK_SIZE) + 1;
        int scoreNew = this.score + pawnPosNew;
        return new Player(pawnPosNew, scoreNew);
    }

    public boolean hasWon(int scoreToWin) {
        return score >= scoreToWin;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Player)) {
            return false;
        } else {
            return this.pawnPos == ((Player) obj).pawnPos
                    && this.score == ((Player) obj).score;
        }
    }

    @Override
    public int hashCode() {
        int[] vals = {pawnPos, score};
        return Arrays.hashCode(vals);
    }
}
