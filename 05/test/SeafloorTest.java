import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SeafloorTest {
    @Test
    public void testAddVent() {
        Seafloor seafloor = new Seafloor();
        // valid inputs
        seafloor.addVent(new Vent(new Point(0, 0), new Point(0, 10)));
        seafloor.addVent(new Vent(new Point(0, 0), new Point(10, 0)));
        seafloor.addVent(new Vent(new Point(0, 0), new Point(10, 10)));
        seafloor.addVent(new Vent(new Point(2, 2), new Point(6, 6)));
        // invalid inputs
        assertThrows(Exception.class, () -> seafloor.addVent(null));
    }

    @Test
    public void testVentOverlaps() {
        Seafloor seafloor = new Seafloor();
        assertEquals(0, seafloor.ventOverlaps());
        seafloor.addVent(new Vent(new Point(1, 1), new Point(5, 1)));
        assertEquals(0, seafloor.ventOverlaps());
        seafloor.addVent(new Vent(new Point(3, 3), new Point(3, 8)));
        assertEquals(0, seafloor.ventOverlaps());
        seafloor.addVent(new Vent(new Point(3, 1), new Point(3, 7)));
        assertEquals(6, seafloor.ventOverlaps());
        seafloor.addVent(new Vent(new Point(2, 1), new Point(4, 1)));
        assertEquals(8, seafloor.ventOverlaps());
    }
}
