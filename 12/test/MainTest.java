import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE_SMALL =
            "start-A\n" +
            "start-b\n" +
            "A-c\n" +
            "A-b\n" +
            "b-d\n" +
            "A-end\n" +
            "b-end";
    private static final String SAMPLE_MEDIUM =
            "dc-end\n" +
            "HN-start\n" +
            "start-kj\n" +
            "dc-start\n" +
            "dc-HN\n" +
            "LN-dc\n" +
            "HN-end\n" +
            "kj-sa\n" +
            "kj-HN\n" +
            "kj-dc";
    private static final String SAMPLE_LARGE =
            "fs-end\n" +
            "he-DX\n" +
            "fs-he\n" +
            "start-DX\n" +
            "pj-DX\n" +
            "end-zg\n" +
            "zg-sl\n" +
            "zg-pj\n" +
            "pj-he\n" +
            "RW-he\n" +
            "fs-DX\n" +
            "pj-RW\n" +
            "zg-RW\n" +
            "start-pj\n" +
            "he-WI\n" +
            "zg-he\n" +
            "pj-fs\n" +
            "start-RW";

    @Test
    public void testCountPaths() throws IOException {
        assertEquals(10, Main.countPaths(Utils.testLineReader(SAMPLE_SMALL), false));
        assertEquals(19, Main.countPaths(Utils.testLineReader(SAMPLE_MEDIUM), false));
        assertEquals(226, Main.countPaths(Utils.testLineReader(SAMPLE_LARGE), false));
    }

    @Test
    public void testCountPathsWithRevisiting() throws IOException {
        assertEquals(36, Main.countPaths(Utils.testLineReader(SAMPLE_SMALL), true));
        assertEquals(103, Main.countPaths(Utils.testLineReader(SAMPLE_MEDIUM), true));
        assertEquals(3509, Main.countPaths(Utils.testLineReader(SAMPLE_LARGE), true));
    }
}
