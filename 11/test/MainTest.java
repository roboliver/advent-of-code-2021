import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "5483143223\n" +
            "2745854711\n" +
            "5264556173\n" +
            "6141336146\n" +
            "6357385478\n" +
            "4167524645\n" +
            "2176841721\n" +
            "6882881134\n" +
            "4846848554\n" +
            "5283751526";

    @Test
    public void testFlashesAfter100Steps() throws IOException {
        assertEquals(1656, Main.flashesAfterNSteps(Utils.testLineReader(SAMPLE), 100));
    }

    @Test
    public void testFirstStepSynchronisedFlashes() throws IOException {
        assertEquals(195, Main.firstStepSynchronisedFlashes(Utils.testLineReader(SAMPLE)));
    }
}
