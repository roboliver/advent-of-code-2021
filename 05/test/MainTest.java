import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "0,9 -> 5,9\n" +
            "8,0 -> 0,8\n" +
            "9,4 -> 3,4\n" +
            "2,2 -> 2,1\n" +
            "7,0 -> 7,4\n" +
            "6,4 -> 2,0\n" +
            "0,9 -> 2,9\n" +
            "3,4 -> 1,4\n" +
            "0,0 -> 8,8\n" +
            "5,5 -> 8,2";

    private static BufferedReader sampleLineReader() {
        return new BufferedReader(new StringReader(SAMPLE));
    }

    @Test
    public void testVentOverlapsWithoutDiagonals() throws IOException {
        assertEquals(5, Main.ventOverlaps(sampleLineReader(), false));
    }

    @Test
    public void testVentOverlapsWithDiagonals() throws IOException {
        assertEquals(12, Main.ventOverlaps(sampleLineReader(), true));
    }
}
