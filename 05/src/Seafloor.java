import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents the seafloor. Add vents to it with {@code addVent} and then check the overlaps.
 */
public class Seafloor {
    private final Map<Point, Integer> ventPoints = new HashMap<>();
    private int ventOverlaps = 0;

    public void addVent(Vent vent) {
        for (Point point : vent.points()) {
            Integer count = ventPoints.get(point);
            if (count == null) {
                count = 0;
            } else if (count.intValue() == 1) {
                // found the first overlap at this point
                ventOverlaps++;
            }
            ventPoints.put(point, count + 1);
        }
    }

    public int ventOverlaps() {
        return ventOverlaps;
    }
}
