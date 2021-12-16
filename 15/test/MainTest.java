import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "1163751742\n" +
            "1381373672\n" +
            "2136511328\n" +
            "3694931569\n" +
            "7463417111\n" +
            "1319128137\n" +
            "1359912421\n" +
            "3125421639\n" +
            "1293138521\n" +
            "2311944581";

    @Test
    public void testBestPathRisk() throws IOException {
        assertEquals(40, Main.bestPathRisk(Utils.testLineReader(SAMPLE), 1));
    }

    @Test
    public void testBestPathRiskQuintupledCave() throws IOException {
        assertEquals(315, Main.bestPathRisk(Utils.testLineReader(SAMPLE), 5));
    }

    private static String printCaveTraversed(CavePosition[][] caveTraversed) {
        StringBuilder buf = new StringBuilder();
        for (int row = 0; row < caveTraversed.length; row++) {
            if (row > 0) {
                buf.append('\n');
            }
            for (int col = 0; col < caveTraversed[row].length; col++) {
                buf.append('|');
                buf.append(String.format("%3d", caveTraversed[row][col].bestPathTo()));
            }
            buf.append('|');
        }
        return buf.toString();
    }
}
