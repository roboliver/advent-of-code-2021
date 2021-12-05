import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SeafloorTest {

    @Test
    public void testConstructor() {
        new Seafloor(0, 0, 0, 0);
        new Seafloor(0, 1, 0, 1);
        new Seafloor(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertThrows(Exception.class, () -> new Seafloor(1, 0, 0, 0));
        assertThrows(Exception.class, () -> new Seafloor(0, 0, 1, 0));
    }

    @Test
    public void testAddVent() {
        Seafloor seafloor = new Seafloor(0, 10, 0, 10);
        // valid inputs
        seafloor.addVent(new Vent(new Point(0, 0), new Point(0, 10)));
        seafloor.addVent(new Vent(new Point(0, 0), new Point(10, 0)));
        seafloor.addVent(new Vent(new Point(0, 0), new Point(10, 10)));
        seafloor.addVent(new Vent(new Point(2, 2), new Point(6, 6)));
        // invalid inputs
        assertThrows(Exception.class, () -> seafloor.addVent(null));
        assertThrows(Exception.class, () -> seafloor.addVent(new Vent(new Point(-1, 0), new Point(0, 0))));
        assertThrows(Exception.class, () -> seafloor.addVent(new Vent(new Point(0, -1), new Point(0, 0))));
        assertThrows(Exception.class, () -> seafloor.addVent(new Vent(new Point(5, 5), new Point(11, 5))));
        assertThrows(Exception.class, () -> seafloor.addVent(new Vent(new Point(2, 2), new Point(2, 11))));
    }

    @Test
    public void testVentOverlaps() {
        Seafloor seafloor = new Seafloor(0, 10, 0, 10);
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
