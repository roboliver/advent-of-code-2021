import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE = "target area: x=20..30, y=-10..-5";

    @Test
    public void  testHighestArc() throws IOException {
        assertEquals(45, Main.highestArc(Utils.testLineReader(SAMPLE)));
    }

    @Test
    public void testArcCount() throws IOException {
        //assertEquals(112, Main.arcCount(Utils.testLineReader(SAMPLE)));
    }
}
