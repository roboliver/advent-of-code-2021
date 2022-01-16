import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class VolumeTest {
    @Test
    public void testEquals() {
        Volume v1 = new Volume(124, 30, 3, 23590, 4239, 7896);
        Volume v2 = new Volume(124, 30, 3, 23590, 4239, 7896);
        Volume v3 = new Volume(8, -57, 0, 767, -55, 78787);
        assertEquals(v1, v2);
        assertEquals(v2, v1);
        assertNotEquals(v1, v3);
        assertNotEquals(v2, v3);
        assertNotEquals(v3, v1);
        assertNotEquals(v3, v2);
        assertEquals(Volume.NULL, Volume.NULL);
    }

    @Test
    public void testHashCode() {
        Volume v1 = new Volume(2, 409, 23, 904, 3253, 90776);
        Volume v2 = new Volume(2, 409, 23, 904, 3253, 90776);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testIntersection() {
        // overlaps over corners
        //Volume v1 = new Volume()
    }
}
