import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "NNCB\n" +
            "\n" +
            "CH -> B\n" +
            "HH -> N\n" +
            "CB -> H\n" +
            "NH -> C\n" +
            "HB -> C\n" +
            "HC -> B\n" +
            "HN -> C\n" +
            "NN -> C\n" +
            "BH -> H\n" +
            "NC -> B\n" +
            "NB -> B\n" +
            "BN -> B\n" +
            "BB -> N\n" +
            "BC -> B\n" +
            "CC -> N\n" +
            "CN -> C";

    @Test
    public void testRangeAfter10Steps() throws IOException {
        assertEquals(1588L, Main.rangeAfterNSteps(Utils.testLineReader(SAMPLE), 10));
    }

    @Test
    public void testRangeAfter40Steps() throws IOException {
        assertEquals(2188189693529L, Main.rangeAfterNSteps(Utils.testLineReader(SAMPLE), 40));
    }
}
