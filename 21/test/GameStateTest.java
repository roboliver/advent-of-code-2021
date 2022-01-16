import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameStateTest {
    @Test
    public void testHaveGo() {
        Player player1 = new Player(2);
        Player player2 = new Player(5);
        GameState gameState = new GameState(player1, player2);
        DeterministicDie die = new DeterministicDie(100);
        gameState = haveGoDeterministic(gameState, die, GameState.WhichPlayer.ONE);
        assertEquals(16, gameState.getPlayer1().getScore());
        assertEquals(0, gameState.getPlayer2().getScore());
        assertEquals(3, die.rollCount());
        gameState = haveGoDeterministic(gameState, die, GameState.WhichPlayer.TWO);
        assertEquals(16, gameState.getPlayer1().getScore());
        assertEquals(23, gameState.getPlayer2().getScore());
        assertEquals(6, die.rollCount());
    }

    private static GameState haveGoDeterministic(GameState gameState, Die die, GameState.WhichPlayer whichPlayer) {
        return gameState.haveGo(whichPlayer, die)
                .entrySet()
                .stream()
                .findFirst()
                .get()
                .getKey();
    }
}
