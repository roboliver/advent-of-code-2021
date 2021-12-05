import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class VentTest {

    @Test
    public void testConstructor() {
        // horizontal, L->R and R->L
        new Vent(new Point(1, 4), new Point(56, 4));
        new Vent(new Point(355, 94), new Point(-35, 94));
        // vertical, T->B and B->T
        new Vent(new Point(6, 15), new Point(6, 90902));
        new Vent(new Point(35, 55555), new Point(35, 553));
        // diagonal, TL->BR and TR->BL
        new Vent(new Point(0, 0), new Point(6, 6));
        new Vent(new Point(8989, 108989), new Point(0, 100000));
        // diagonal, TL->BR and TR->BL
        new Vent(new Point(0, 23), new Point(23, 0));
        new Vent(new Point(8, 19), new Point(20, 7));
        // invalid points
        assertThrows(Exception.class, () -> new Vent(null, new Point(3, 5)));
        assertThrows(Exception.class, () -> new Vent(new Point(594, 23), null));
        assertThrows(Exception.class, () -> new Vent(new Point(0, 0), new Point(15, 30)));
    }

    @Test
    public void testPoints() {
        Set<Point> pointSingle = Set.of(new Point(523, 23));
        Vent ventSingle = new Vent(new Point(523, 23), new Point(523, 23));
        Set<Point> pointsHorizontal = Set.of(new Point(1, 1), new Point(1, 2), new Point(1, 3));
        Vent ventLtoR = new Vent(new Point(1, 1), new Point(1, 3));
        Vent ventRtoL = new Vent(new Point(1, 3), new Point(1, 1));
        Set<Point> pointsVertical = Set.of(new Point(3, 5), new Point(3, 6), new Point(3, 7));
        Vent ventBtoT = new Vent(new Point(3, 5), new Point(3, 7));
        Vent ventTtoB = new Vent(new Point(3, 7), new Point(3, 5));
        Set<Point> pointsDiagPos = Set.of(new Point(0, 0), new Point(1, 1), new Point(2, 2));
        Vent ventBLtoTR = new Vent(new Point(0, 0), new Point(2, 2));
        Vent ventTRtoBL = new Vent(new Point(2, 2), new Point(0, 0));
        Set<Point> pointsDiagNeg = Set.of(new Point(1, 10), new Point(2, 9), new Point(3, 8));
        Vent ventTLtoBR = new Vent(new Point(1, 10), new Point(3, 8));
        Vent ventBRtoTL = new Vent(new Point(3, 8), new Point(1, 10));
        testPointsAllMatch(pointSingle, ventSingle);
        testPointsAllMatch(pointsHorizontal, ventLtoR);
        testPointsAllMatch(pointsHorizontal, ventRtoL);
        testPointsAllMatch(pointsVertical, ventBtoT);
        testPointsAllMatch(pointsVertical, ventTtoB);
        testPointsAllMatch(pointsDiagPos, ventBLtoTR);
        testPointsAllMatch(pointsDiagPos, ventTRtoBL);
        testPointsAllMatch(pointsDiagNeg, ventTLtoBR);
        testPointsAllMatch(pointsDiagNeg, ventBRtoTL);
    }

    private static void testPointsAllMatch(Set<Point> points, Vent vent) {
        Set<Point> ventPoints = vent.points();
        assertEquals(points.size(), ventPoints.size());
        assertTrue(points.containsAll(ventPoints));
        assertTrue(ventPoints.containsAll(points));
    }
}
