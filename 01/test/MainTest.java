import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

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

    private static BufferedReader sampleLineReader() {
        return new BufferedReader(new StringReader(SAMPLE));
    }

    @Test
    public void testIncreasesCount() throws IOException {
        assertEquals(7, Main.increasesCount(sampleLineReader()));
    }

    @Test
    public void testIncreasesCountSliding() throws IOException {
        assertEquals(5, Main.increasesCountSliding(sampleLineReader()));
    }
}
