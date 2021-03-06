import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a vent, with start and end coordinates. Vents must be either horizontal, vertical, or diagonal, but it
 * doesn't matter which direction they run in. Call {@code points} to get the set of points the vent spans.
 */
public class Vent {
    private final Point start;
    private final Point end;

    public Vent(Point start, Point end) {
        this.start = Objects.requireNonNull(start);
        this.end = Objects.requireNonNull(end);
        if (xLen() != 0 && yLen() != 0 && xLen() != yLen()) {
            throw new IllegalArgumentException("vent must be at a 90 or 45 degree angle");
        }
    }

    private int xLen() {
        return Math.abs(end.x() - start.x());
    }

    private int yLen() {
        return Math.abs(end.y() - start.y());
    }

    public Set<Point> points() {
        Set<Point> points = new HashSet<>();
        int xCur = start.x();
        int yCur = start.y();
        while (xCur != end.x() || yCur != end.y()) {
            points.add(new Point(xCur, yCur));
            if (xCur != end.x()) {
                xCur += start.x() < end.x() ? 1 : -1;
            }
            if (yCur != end.y()) {
                yCur += start.y() < end.y() ? 1 : -1;
            }
        }
        // add the final point
        points.add(new Point(xCur, yCur));
        return points;
    }
}
