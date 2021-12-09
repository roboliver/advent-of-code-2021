import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "2199943210\n" +
            "3987894921\n" +
            "9856789892\n" +
            "8767896789\n" +
            "9899965678";

    @Test
    public void testLowpointRiskLevel() throws IOException {
        assertEquals(15, Main.lowpointRiskLevel(Utils.testLineReader(SAMPLE)));
    }
}
