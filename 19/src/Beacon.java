import java.util.Arrays;

public class Beacon {
    private final int x;
    private final int y;
    private final int z;

    public Beacon(int x, int y, int z) {
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

    public Beacon rotate(int pitch, int roll, int yaw) {
        int xNew = requireValidRotate(pitch);
        int yNew = requireValidRotate(roll);
        int zNew = requireValidRotate(yaw);
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
        return new Beacon(xNew, yNew, zNew);
    }

    private static int requireValidRotate(int rotate) {
        if (rotate < 0 || rotate > 3) {
            throw new IllegalArgumentException("rotation value must be between 0 and 3");
        }
        return rotate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Beacon)) {
            return false;
        } else {
            return this.x == ((Beacon) obj).x
                    && this.y == ((Beacon) obj).y
                    && this.z == ((Beacon) obj).z;
        }
    }

    @Override
    public int hashCode() {
        int[] vals = {x, y, z};
        return Arrays.hashCode(vals);
    }

    @Override
    public String toString() {
        return ("[" + x + "," + y + "," + z + "]");
    }
}
