import java.util.HashMap;
import java.util.Map;

public class Player {
    private static final int TRACK_SIZE = 10;
    private static final int SCORE_TO_WIN = 1000;

    private final int pawnPos;
    private final int score;

    public Player(int startingPos) {
        this.pawnPos = startingPos;
        this.score = 0;
    }

    private Player(int pawnPos, int score) {
        this.pawnPos = pawnPos;
        this.score = score;
    }

    public Player roll(int roll) {
        int pawnPosNew = (((this.pawnPos + roll) - 1) % TRACK_SIZE) + 1;
        int scoreNew = this.score + pawnPosNew;
        return new Player(pawnPosNew, scoreNew);
    }

    public boolean hasWon() {
        return score >= SCORE_TO_WIN;
    }

    public int getScore() {
        return score;
    }
}
