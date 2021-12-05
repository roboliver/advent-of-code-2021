import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "forward 5\n" +
            "down 5\n" +
            "forward 8\n" +
            "up 3\n" +
            "down 8\n" +
            "forward 2";

    @Test
    public void testDistance() throws IOException {
        assertEquals(150, Main.distance(Utils.testLineReader(SAMPLE)));
    }

    @Test
    public void testDistanceActual() throws IOException {
        assertEquals(900, Main.distanceActual(Utils.testLineReader(SAMPLE)));
    }
}
