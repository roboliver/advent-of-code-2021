import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE = "3,4,3,1,2";

    @Test
    public void testLanternfishAfter80Days() throws IOException {
        assertEquals(5934, Main.lanternfishAfterNDays(Utils.testLineReader(SAMPLE), 80));
    }

    @Test
    public void testLanternfishAfter256Days() throws IOException {
        assertEquals(26984457539L, Main.lanternfishAfterNDays(Utils.testLineReader(SAMPLE), 256));
    }
}
