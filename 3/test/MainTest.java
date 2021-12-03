import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "00100\n" +
            "11110\n" +
            "10110\n" +
            "10111\n" +
            "10101\n" +
            "01111\n" +
            "00111\n" +
            "11100\n" +
            "10000\n" +
            "11001\n" +
            "00010\n" +
            "01010";

    private static BufferedReader sampleLineReader() {
        return new BufferedReader(new StringReader(SAMPLE));
    }

    @Test
    public void testPowerConsumption() throws IOException {
        assertEquals(198, Main.powerConsumption(sampleLineReader()));
    }

    @Test
    public void testLifeSupportRating() throws IOException {
        assertEquals(230, Main.lifeSupportRating(sampleLineReader()));
    }
}
