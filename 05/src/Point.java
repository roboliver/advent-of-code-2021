import java.util.Arrays;

/**
 * Represents a single point with an x and y coordinate.
 */
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Point)) {
            return false;
        } else {
            return this.x == ((Point) obj).x && this.y == ((Point) obj).y;
        }
    }

    @Override
    public int hashCode() {
        int[] vals = {x, y};
        return Arrays.hashCode(vals);
    }
}
