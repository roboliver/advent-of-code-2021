import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BeaconTest {
    @Test
    public void rotateTest() {
        Beacon beacon = new Beacon(1, 2, 3);
        // no rotation
        assertEquals(new Beacon(1, 2, 3), beacon.rotate(0, 0, 0));
        // pitch only
        assertEquals(new Beacon(-3, 2, 1), beacon.rotate(1, 0, 0));
        assertEquals(new Beacon(-1, 2, -3), beacon.rotate(2, 0, 0));
        assertEquals(new Beacon(3, 2, -1), beacon.rotate(3, 0, 0));
        // roll only
        assertEquals(new Beacon(1, 3, -2), beacon.rotate(0, 1, 0));
        assertEquals(new Beacon(1, -2, -3), beacon.rotate(0, 2, 0));
        assertEquals(new Beacon(1, -3, 2), beacon.rotate(0, 3, 0));
        // yaw only
        assertEquals(new Beacon(2, -1, 3), beacon.rotate(0, 0, 1));
        assertEquals(new Beacon(-1, -2, 3), beacon.rotate(0, 0, 2));
        assertEquals(new Beacon(-2, 1, 3), beacon.rotate(0, 0, 3));
    }
}