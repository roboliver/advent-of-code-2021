import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BeaconTest {
    @Test
    public void testRotate() {
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

    @Test
    public void testTranslate() {
        Beacon beacon = new Beacon(1, 2, 3);
        assertEquals(new Beacon(2, 2, 3), beacon.translate(1, 0, 0));
    }

    @Test
    public void testDistanceTo() {
        Beacon beacon1 = new Beacon(0, 0, 0);
        Beacon beacon2 = new Beacon(4, 4, 4);
        Beacon beacon2Neg = new Beacon(-4, -4, -4);
        Beacon beacon3 = new Beacon(500, 500, 500);
        Beacon beacon4 = new Beacon(504, 504, 504);
        System.out.println(beacon1.distanceTo(beacon2));
        System.out.println(beacon3.distanceTo(beacon4));
        System.out.println(beacon1.distanceTo(beacon2Neg));
    }
}