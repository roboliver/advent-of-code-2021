import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "199\n" +
            "200\n" +
            "208\n" +
            "210\n" +
            "200\n" +
            "207\n" +
            "240\n" +
            "269\n" +
            "260\n" +
            "263";

    @Test
    public void testIncreasesCount() throws IOException {
        assertEquals(7, Main.increasesCount(Utils.testLineReader(SAMPLE)));
    }

    @Test
    public void testIncreasesCountSliding() throws IOException {
        assertEquals(5, Main.increasesCountSliding(Utils.testLineReader(SAMPLE)));
    }
}
