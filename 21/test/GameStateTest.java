import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameStateTest {
    @Test
    public void testHaveGoDeterministic() {
        Player player1 = new Player(2);
        Player player2 = new Player(5);
        GameState gameState = new GameState(player1, player2);
        DeterministicDie die = new DeterministicDie(100);
        gameState = gameState.haveGoDeterministic(gameState, die, WhichPlayer.ONE);
        assertEquals(8, gameState.getPlayer1().getScore());
        assertEquals(0, gameState.getPlayer2().getScore());
        assertEquals(3, die.rollCount());
        gameState = gameState.haveGoDeterministic(gameState, die, WhichPlayer.TWO);
        assertEquals(8, gameState.getPlayer1().getScore());
        assertEquals(10, gameState.getPlayer2().getScore());
        assertEquals(6, die.rollCount());
    }
}
