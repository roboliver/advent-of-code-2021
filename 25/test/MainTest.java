import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "v...>>.vv>\n"
            + ".vv>>.vv..\n"
            + ">>.>v>...v\n"
            + ">>v>>.>.v.\n"
            + "v>v.vv.v..\n"
            + ">.>>..v...\n"
            + ".vv..>.>v.\n"
            + "v.v..>>v.v\n"
            + "....v..v.>";

    @Test
    public void testStepWhenStopped() throws IOException {
        assertEquals(58, Main.stepWhenStopped(Utils.testLineReader(SAMPLE)));
    }
}
