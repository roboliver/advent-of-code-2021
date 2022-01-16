import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class GameState {
    public enum WhichPlayer {
        ONE((gameState, roll) -> new GameState(gameState.getPlayer1().roll(roll), gameState.getPlayer2())),
        TWO((gameState, roll) -> new GameState(gameState.getPlayer1(), gameState.getPlayer2().roll(roll)));

        private final BiFunction<GameState, Integer, GameState> playerFunc;

        private WhichPlayer(BiFunction<GameState, Integer, GameState> playerFunc) {
            this.playerFunc = playerFunc;
        }

        private GameState rollPlayer(GameState gameState, Integer roll) {
            return playerFunc.apply(gameState, roll);
        }
    };

    private final Player player1;
    private final Player player2;

    public GameState(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Map<GameState, Long> haveGo(WhichPlayer whichPlayer, Die die) {
        Map<GameState, Long> gameStates = new HashMap<>();
        gameStates.put(this, 1L);
        for (int i = 0; i < 3; i++) {
            Map<GameState, Long> gameStatesNew = new HashMap<>();
            for (Map.Entry<GameState, Long> gameState : gameStates.entrySet()) {
                List<GameState> gameStatesAfterRoll = gameState.getKey().roll(whichPlayer, die);
                for (GameState gameStateAfterRoll : gameStatesAfterRoll) {
                    gameStatesNew.merge(gameStateAfterRoll, gameState.getValue(), Long::sum);
                }
            }
            gameStates = gameStatesNew;
        }
        return gameStates;
    }

    private List<GameState> roll(WhichPlayer whichPlayer, Die die) {
        List<GameState> gameStatesNew = new ArrayList<>();
        for (int roll : die.roll()) {
            gameStatesNew.add(whichPlayer.rollPlayer(this, roll));
        }
        return gameStatesNew;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
