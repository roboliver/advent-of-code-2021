import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionTest {
    @Test
    public void testRotate() {
        Position position = new Position(1, 2, 3);
        // no rotation
        assertEquals(new Position(1, 2, 3), position.rotate(0, 0, 0, 0, 0, 0));
        // pitch only
        assertEquals(new Position(-3, 2, 1), position.rotate(0, 0, 0, 1, 0, 0));
        assertEquals(new Position(-1, 2, -3), position.rotate(0, 0, 0, 2, 0, 0));
        assertEquals(new Position(3, 2, -1), position.rotate(0, 0, 0, 3, 0, 0));
        // roll only
        assertEquals(new Position(1, 3, -2), position.rotate(0, 0, 0, 0, 1, 0));
        assertEquals(new Position(1, -2, -3), position.rotate(0, 0, 0, 0, 2, 0));
        assertEquals(new Position(1, -3, 2), position.rotate(0, 0, 0, 0, 3, 0));
        // yaw only
        assertEquals(new Position(2, -1, 3), position.rotate(0, 0, 0, 0, 0, 1));
        assertEquals(new Position(-1, -2, 3), position.rotate(0, 0, 0, 0, 0, 2));
        assertEquals(new Position(-2, 1, 3), position.rotate(0, 0, 0, 0, 0, 3));
    }

    @Test
    public void testTranslate() {
        Position position = new Position(1, 2, 3);
        assertEquals(new Position(2, 2, 3), position.translate(1, 0, 0));
    }

    @Test
    public void testDistanceTo() {
        Position position1 = new Position(0, 0, 0);
        Position position2 = new Position(4, 4, 4);
        Position position2Neg = new Position(-4, -4, -4);
        Position position3 = new Position(500, 500, 500);
        Position position4 = new Position(504, 504, 504);
        assertEquals(new Distance(4, 4, 4), position1.distanceTo(position2));
        assertEquals(new Distance(4, 4, 4), position3.distanceTo(position4));
        assertEquals(new Distance(4, 4, 4), position1.distanceTo(position2Neg));
    }
}