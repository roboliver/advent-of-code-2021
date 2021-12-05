import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents the seafloor. Add vents to it with {@code addVent} and then check the overlaps.
 */
public class Seafloor {
    private final Map<Point, Integer> points = new HashMap<Point, Integer>();
    private int ventOverlaps = 0;

    public Seafloor() {
    }

    public void addVent(Vent vent) {
        Set<Point> ventPoints = vent.points();
        for (Point point : ventPoints) {
            Integer count = points.get(point);
            if (count == null) {
                count = 0;
            } else if (count == 1) {
                // first vent overlap at this point
                ventOverlaps++;
            }
            points.put(point, count + 1);
        }
    }

    public int ventOverlaps() {
        return ventOverlaps;
    }
}
