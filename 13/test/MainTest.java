import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "6,10\n" +
            "0,14\n" +
            "9,10\n" +
            "0,3\n" +
            "10,4\n" +
            "4,11\n" +
            "6,0\n" +
            "6,12\n" +
            "4,1\n" +
            "0,13\n" +
            "10,12\n" +
            "3,4\n" +
            "3,0\n" +
            "8,4\n" +
            "1,10\n" +
            "2,14\n" +
            "8,10\n" +
            "9,0\n" +
            "\n" +
            "fold along y=7\n" +
            "fold along x=5";

    private static final String SAMPLE_FOLDED =
            "#####\n" +
            "#...#\n" +
            "#...#\n" +
            "#...#\n" +
            "#####\n" +
            ".....\n" +
            ".....";

    @Test
    public void testDotsAfterOneFold() throws IOException {
        assertEquals(17, Main.dotsAfterNFolds(Utils.testLineReader(SAMPLE), 1));
    }

    @Test
    public void testFullyFold() throws IOException {
        assertEquals(SAMPLE_FOLDED, Main.fullyFold(Utils.testLineReader(SAMPLE)));
    }
}
