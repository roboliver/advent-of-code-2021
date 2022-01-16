import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    @Test
    public void testRoll() {
        Player player = new Player(5);
        player = player.roll(2);
        assertEquals(7, player.getScore());
        player = player.roll(3);
        assertEquals(17, player.getScore());
        player = player.roll(4);
        assertEquals(21, player.getScore());
        player = player.roll(25);
        assertEquals(30, player.getScore());
    }

    @Test
    public void testWin() {
        Player player = new Player(10);
        for (int i = 0; i < 99; i++) {
            player = player.roll(10);
        }
        assertFalse(player.hasWon());
        player = player.roll(10);
        assertTrue(player.hasWon());
    }
}
