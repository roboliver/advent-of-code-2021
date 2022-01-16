import java.util.*;

public class GameState {

    private final Player player1;
    private final Player player2;

    public GameState(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Map<GameState, Long> haveGo(WhichPlayer whichPlayer, Die die) {
        Map<GameState, Long> gameStatesNew = new HashMap<>();
        for (int rollSum : die.rollNTimes(3)) {
            gameStatesNew.merge(whichPlayer.haveGo(this, rollSum), 1L, Long::sum);
        }
        return gameStatesNew;
    }

    public GameState haveGoDeterministic(GameState gameState, DeterministicDie die, WhichPlayer whichPlayer) {
        return haveGo(whichPlayer, die)
                .entrySet()
                .stream()
                .findFirst()
                .orElseThrow()
                .getKey();
    }

    public Player getPlayer(WhichPlayer whichPlayer) {
        return whichPlayer.getPlayer(this);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof GameState)) {
            return false;
        } else {
            return this.player1.equals(((GameState) obj).player1)
                    && this.player2.equals(((GameState) obj).player2);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(player1, player2);
    }
}
