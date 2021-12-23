import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClusterTest {
    @Test
    public void testRotate() {
        List<Beacon> beacons = List.of(new Beacon(0, 0, 0));
        Cluster cluster = new Cluster(beacons);
        assertEquals(1, cluster.rotate(0, 0, 0, 2, 3, 1).containsCount(cluster.beacons()));
    }
}
