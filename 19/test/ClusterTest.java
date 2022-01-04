import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClusterTest {
    @Test
    public void testRotate() {
        List<Position> beacons = List.of(new Position(0, 0, 0));
        Cluster cluster = new Cluster(beacons);
        assertEquals(1, cluster.rotate(0, 0, 0, 2, 3, 1).containsCount(cluster.beacons()));
    }
}
