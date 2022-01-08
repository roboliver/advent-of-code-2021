import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "Player 1 starting position: 4\n"
            + "Player 2 starting position: 8";

    @Test
    public void testLosingScoreTimesDieRolls() throws IOException {
        assertEquals(739785, Main.losingScoreTimesDieRolls(Utils.testLineReader(SAMPLE)));
    }
}
