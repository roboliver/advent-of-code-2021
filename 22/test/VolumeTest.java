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
    public void testIntersectionCorners() {
        Volume v = new Volume(-10, -10, -10, 10, 10, 10);
        Volume cornerLLL = new Volume(-15, -15, -15, 5, 5, 5);
        Volume cornerLLH = new Volume(-20, -30, 5, 6, 6, 20);
        Volume cornerLHL = new Volume(-40, 7, -80, 2, 500, 9);
        Volume cornerLHH = new Volume(-11, 0, 1, -4, 12, 1000);
        Volume cornerHLL = new Volume(8, -900, -79, 10, 8, 3);
        Volume cornerHLH = new Volume(5, -10, 5, 14, 1, 90);
        Volume cornerHHL = new Volume(7, 4, -11, 11, 1100, -8);
        Volume cornerHHH = new Volume(5, 6, 7, 11, 12, 13);
        assertEquals(new Volume(-10, -10, -10, 5, 5, 5), v.intersection(cornerLLL));
        assertEquals(new Volume(-10, -10, -10, 5, 5, 5), cornerLLL.intersection(v));
        assertEquals(new Volume(-10, -10, 5, 6, 6, 10), v.intersection(cornerLLH));
        assertEquals(new Volume(-10, 7, -10, 2, 10, 9), v.intersection(cornerLHL));
        assertEquals(new Volume(-10, 0, 1, -4, 10, 10), v.intersection(cornerLHH));
        assertEquals(new Volume(8, -10, -10, 10, 8, 3), v.intersection(cornerHLL));
        assertEquals(new Volume(5, -10, 5, 10, 1, 10), v.intersection(cornerHLH));
        assertEquals(new Volume(7, 4, -10, 10, 10, -8), v.intersection(cornerHHL));
        assertEquals(new Volume(5, 6, 7, 10, 10, 10), v.intersection(cornerHHH));
    }

    @Test
    public void testIntersectionEdges() {
        Volume v = new Volume(20, 20, 20, 50, 50, 50);
        Volume edgeXLL = new Volume(25, 10, 10, 45, 40, 40);
        Volume edgeXLH = new Volume(30, -5, 25, 35, 40, 110);
        Volume edgeXHL = new Volume(24, 24, -3, 24, 240, 24);
        Volume edgeXHH = new Volume(21, 33, 32, 49, 330, 320);
        Volume edgeLYL = new Volume(1, 23, 10, 31, 45, 46);
        Volume edgeLYH = new Volume(7, 27, 33, 29, 32, 51);
        Volume edgeHYL = new Volume(43, 41, -3, 444, 43, 22);
        Volume edgeHYH = new Volume(25, 25, 25, 55, 45, 55);
        Volume edgeLLZ = new Volume(1, 1, 24, 25, 25, 28);
        Volume edgeLHZ = new Volume(5, 36, 35, 22, 350, 38);
        Volume edgeHLZ = new Volume(39, -123, 24, 3555, 26, 39);
        Volume edgeHHZ = new Volume(43, 43, 21, 56, 50, 43);
        assertEquals(new Volume(25, 20, 20, 45, 40, 40), v.intersection(edgeXLL));
        assertEquals(new Volume(30, 20, 25, 35, 40, 50), v.intersection(edgeXLH));
        assertEquals(new Volume(24, 24, 20, 24, 50, 24), v.intersection(edgeXHL));
        assertEquals(new Volume(21, 33, 32, 49, 50, 50), v.intersection(edgeXHH));
        assertEquals(new Volume(20, 23, 20, 31, 45, 46), v.intersection(edgeLYL));
        assertEquals(new Volume(20, 27, 33, 29, 32, 50), v.intersection(edgeLYH));
        assertEquals(new Volume(43, 41, 20, 50, 43, 22), v.intersection(edgeHYL));
        assertEquals(new Volume(25, 25, 25, 50, 45, 50), v.intersection(edgeHYH));
        assertEquals(new Volume(20, 20, 24, 25, 25, 28), v.intersection(edgeLLZ));
        assertEquals(new Volume(20, 36, 35, 22, 50, 38), v.intersection(edgeLHZ));
        assertEquals(new Volume(39, 20, 24, 50, 26, 39), v.intersection(edgeHLZ));
        assertEquals(new Volume(43, 43, 21, 50, 50, 43), v.intersection(edgeHHZ));
    }

    @Test
    public void testIntersectionContained() {
        Volume v = new Volume(0, 0, 0, 100, 100, 100);
        Volume contained = new Volume(30, 40, 50, 60, 70, 80);
        assertEquals(new Volume(30, 40, 50, 60, 70, 80), v.intersection(contained));
    }

    @Test
    public void testIntersectionContaining() {
        Volume v = new Volume(20, 20, 20, 40, 40, 40);
        Volume containing = new Volume(15, 15, 15, 45, 45, 45);
        assertEquals(new Volume(20, 20, 20, 40, 40, 40), v.intersection(containing));
    }

    @Test
    public void testIntersectionNone() {
        // total misses
        Volume v = new Volume(10, 10, 10, 20, 20, 20);
        Volume noOverlapLLL = new Volume(-1, -1, -1, 4, 4, 5);
        Volume noOverlapLLH = new Volume(-1, -5, 200, -3, -6, 203);
        Volume noOverlapLHL = new Volume(-143, 6000, -2, -2, 55555, 1);
        Volume noOverlapHHH = new Volume(45, 50, 55, 400, 405, 410);
        assertEquals(Volume.NULL, v.intersection(noOverlapLLL));
        assertEquals(Volume.NULL, v.intersection(noOverlapLLH));
        assertEquals(Volume.NULL, v.intersection(noOverlapLHL));
        assertEquals(Volume.NULL, v.intersection(noOverlapHHH));
        // overlaps on two dimensions but not the third
        Volume noOverlapXL = new Volume(-1, 12, 12, -3, 14, 14);
        Volume noOverlapXH = new Volume(35, 11, 13, 355, 15, 16);
        Volume noOverlapYL = new Volume(-5, -5, -5, 200, -6, 300);
        Volume noOverlapYH = new Volume(11, 1500, 12, 1501, 1501, 1501);
        Volume noOverlapZL = new Volume(4, 4, 4, 40, 40, 5);
        Volume noOverlapZH = new Volume(12, 12, 120, 16, 16, 150);
        assertEquals(Volume.NULL, v.intersection(noOverlapXL));
        assertEquals(Volume.NULL, v.intersection(noOverlapXH));
        assertEquals(Volume.NULL, v.intersection(noOverlapYL));
        assertEquals(Volume.NULL, v.intersection(noOverlapYH));
        assertEquals(Volume.NULL, v.intersection(noOverlapZL));
        assertEquals(Volume.NULL, v.intersection(noOverlapZH));
    }
}
