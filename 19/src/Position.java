import java.util.Arrays;

/**
 * Represents a 3D point floating in the ocean. Used for both beacons and scanners.
 */
public class Position {
    private final int x;
    private final int y;
    private final int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    public Position rotate(int xOrigin, int yOrigin, int zOrigin,
                           int pitch, int roll, int yaw) {
        assertValidRotate(pitch);
        assertValidRotate(roll);
        assertValidRotate(yaw);
        int xNew = x - xOrigin;
        int yNew = y - yOrigin;
        int zNew = z - zOrigin;
        for (int i = 0; i < pitch; i++) {
            int zTemp = xNew;
            xNew = zNew * -1;
            zNew = zTemp;
        }
        for (int i = 0; i < roll; i++) {
            int yTemp = zNew;
            zNew = yNew * -1;
            yNew = yTemp;
        }
        for (int i = 0; i < yaw; i++) {
            int xTemp = yNew;
            yNew = xNew * -1;
            xNew = xTemp;
        }
        return new Position(xNew + xOrigin, yNew + yOrigin, zNew + zOrigin);
    }

    public Position translate(int x, int y, int z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    private static void assertValidRotate(int rotate) {
        if (rotate < 0 || rotate > 3) {
            throw new IllegalArgumentException("rotation value must be between 0 and 3");
        }
    }

    public Distance distanceTo(Position other) {
        int xDist = Math.abs(x - other.x);
        int yDist = Math.abs(y - other.y);
        int zDist = Math.abs(z - other.z);
        return new Distance(xDist, yDist, zDist);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Position)) {
            return false;
        } else {
            return this.x == ((Position) obj).x
                    && this.y == ((Position) obj).y
                    && this.z == ((Position) obj).z;
        }
    }

    @Override
    public int hashCode() {
        int[] vals = {x, y, z};
        return Arrays.hashCode(vals);
    }

    @Override
    public String toString() {
        return ("[" + x + ", " + y + ", " + z + "]");
    }
}
