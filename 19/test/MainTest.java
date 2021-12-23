import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MainTest {
    @Test
    public void testBeaconCount() throws IOException {
        assertEquals(79, Main.beaconCount(Utils.inputLineReader("sample.txt")));
    }

    @Test
    public void testMaxManhattanDistance() throws IOException {
        assertEquals(3621, Main.maxManhattanDistance(Utils.inputLineReader("sample.txt")));
    }

    //@Test
    public void testPrintClusters() throws IOException {
        List<Cluster> clusters = Main.clusters(Utils.inputLineReader("sample.txt"));
        for (Cluster cluster : clusters) {
            System.out.println(cluster);
            System.out.println();
        }
    }
}
