import org.junit.Test;

import static org.junit.Assert.*;

public class PointTest {

    @Test
    public void testEquals() {
        Point point1 = new Point(3, 5);
        Point point2 = new Point(3, 5);
        assertEquals(point1, point2);
        assertEquals(point2, point1);
        Point point3 = new Point(5, 3);
        assertNotEquals(point1, point3);
        assertNotEquals(point3, point2);
    }

    @Test
    public void testHashCode() {
        Point point1 = new Point(11, 3);
        Point point2 = new Point(11, 3);
        assertEquals(point1.hashCode(), point2.hashCode());
        Point point3 = new Point(3, 11);
        assertNotEquals(point1.hashCode(), point3.hashCode());
    }
}
