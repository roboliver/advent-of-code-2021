import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DistanceTest {
    @Test
    public void testDimensionSorting() {
        Distance distance1 = new Distance(1, 2, 3);
        Distance distance2 = new Distance(2, 3, 1);
        Distance distance3 = new Distance(3, 1, 2);
        assertEquals(distance1, distance2);
        assertEquals(distance1, distance3);
        assertEquals(distance2, distance3);
    }
}
