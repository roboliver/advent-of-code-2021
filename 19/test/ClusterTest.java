import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClusterTest {
    @Test
    public void testRotate() {
        Cluster cluster = new Cluster();
        cluster.addBeacon(new Beacon(0, 0, 0));
        assertEquals(1, cluster.rotate(0, 0, 0, 2, 3, 1).containsCount(cluster.beacons()));
    }
}
