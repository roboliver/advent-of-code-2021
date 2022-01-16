import java.util.function.BiFunction;
import java.util.function.Function;

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

    public GameState haveGo(GameState gameState, Integer rollSum) {
        return haveGo.apply(gameState, rollSum);
    }

    public WhichPlayer other() {
        return this == WhichPlayer.ONE ? WhichPlayer.TWO : WhichPlayer.ONE;
    }
}
