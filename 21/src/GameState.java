import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GameState {
    public enum WhichPlayer {
        ONE(GameState::getPlayer1,
                (gameState, rollSum) -> new GameState(gameState.getPlayer1().haveGo(rollSum), gameState.getPlayer2())),
        TWO(GameState::getPlayer2,
                (gameState, rollSum) -> new GameState(gameState.getPlayer1(), gameState.getPlayer2().haveGo(rollSum)));

        private final Function<GameState, Player> getPlayer;
        private final BiFunction<GameState, Integer, GameState> haveGo;

        WhichPlayer(Function<GameState, Player> getPlayer, BiFunction<GameState, Integer, GameState> haveGo) {
            this.getPlayer = getPlayer;
            this.haveGo = haveGo;
        }

        public Player getPlayer(GameState gameState) {
            return getPlayer.apply(gameState);
        }

        private GameState haveGo(GameState gameState, Integer rollSum) {
            return haveGo.apply(gameState, rollSum);
        }

        public WhichPlayer other() {
            return this == WhichPlayer.ONE ? WhichPlayer.TWO : WhichPlayer.ONE;
        }
    }

    private final Player player1;
    private final Player player2;

    public GameState(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Map<GameState, Long> haveGo(WhichPlayer whichPlayer, Die die) {
        Map<GameState, Long> gameStatesNew = new HashMap<>();
        for (int rollSum : rollSums(die)) {
            gameStatesNew.merge(whichPlayer.haveGo(this, rollSum), 1L, Long::sum);
        }
        return gameStatesNew;
    }

    private List<Integer> rollSums(Die die) {
        List<Integer> rollSums = new ArrayList<>();
        rollSums.add(0);
        for (int i = 0; i < 3; i++) {
            List<Integer> rolls = die.roll();
            List<Integer> rollSumsNew = new ArrayList<>();
            for (int rollSum : rollSums) {
                for (int roll : rolls) {
                    rollSumsNew.add(rollSum + roll);
                }
            }
            rollSums = rollSumsNew;
        }
        return rollSums;
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
