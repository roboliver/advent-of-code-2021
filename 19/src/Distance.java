import java.util.Arrays;

public class Distance {
    private int[] distances;

    public Distance(int x, int y, int z) {
        int[] distances = new int[]{x, y, z};
        Arrays.sort(distances);
        this.distances = distances;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Distance)) {
            return false;
        } else {
            return Arrays.equals(distances, ((Distance) obj).distances);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(distances);
    }

    @Override
    public String toString() {
        return Arrays.toString(distances);
    }
}
