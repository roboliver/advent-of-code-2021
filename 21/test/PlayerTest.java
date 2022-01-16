import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    @Test
    public void testRoll() {
        Player player = new Player(5);
        player = player.haveGo(2);
        assertEquals(7, player.getScore());
        player = player.haveGo(3);
        assertEquals(17, player.getScore());
        player = player.haveGo(4);
        assertEquals(21, player.getScore());
        player = player.haveGo(25);
        assertEquals(30, player.getScore());
    }

    @Test
    public void testWin() {
        Player player = new Player(10);
        for (int i = 0; i < 99; i++) {
            player = player.haveGo(10);
        }
        assertFalse(player.hasWon());
        player = player.haveGo(10);
        assertTrue(player.hasWon());
    }
}
