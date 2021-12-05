import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SeafloorTest {

    @Test
    public void testConstructor() {
        new Seafloor(0, 0, 0, 0);
        new Seafloor(0, 0, 1, 1);
        new Seafloor(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertThrows(Exception.class, () -> new Seafloor(1, 0, 0, 0));
        assertThrows(Exception.class, () -> new Seafloor(0, 1, 0, 0));
    }

    @Test
    public void testAddVent() {
        Seafloor seafloor = new Seafloor(0, 0, 10, 10);
        // valid inputs
        seafloor.addVent(new Vent(0, 0, 0, 10));
        seafloor.addVent(new Vent(0, 0, 10, 0));
        seafloor.addVent(new Vent(0, 0, 10, 10));
        seafloor.addVent(new Vent(2, 2, 6, 6));
        // invalid inputs
        assertThrows(Exception.class, () -> seafloor.addVent(null));
        assertThrows(Exception.class, () -> seafloor.addVent(new Vent(-1, 0, 0, 0)));
        assertThrows(Exception.class, () -> seafloor.addVent(new Vent(0, -1, 0, 0)));
        assertThrows(Exception.class, () -> seafloor.addVent(new Vent(5, 5, 11, 5)));
        assertThrows(Exception.class, () -> seafloor.addVent(new Vent(2, 2, 2, 11)));
    }

    @Test
    public void testVentOverlaps() {
        Seafloor seafloor = new Seafloor(0, 10, 0, 10);
        assertEquals(0, seafloor.ventOverlaps());
        seafloor.addVent(new Vent(1, 1, 5, 1));
        assertEquals(0, seafloor.ventOverlaps());
        seafloor.addVent(new Vent(3, 3, 3, 8));
        assertEquals(0, seafloor.ventOverlaps());
        seafloor.addVent(new Vent(3, 1, 3, 7));
        assertEquals(6, seafloor.ventOverlaps());
        seafloor.addVent(new Vent(2, 1, 4, 1));
        assertEquals(8, seafloor.ventOverlaps());
    }
}
