import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Volume {
    private final int xMin;
    private final int yMin;
    private final int zMin;
    private final int xMax;
    private final int yMax;
    private final int zMax;

    public static final Volume NULL = new Volume(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
            Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

    public Volume(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }

    public Volume intersection(Volume other) {
        if (!overlaps(other)) {
            return NULL;
        } else {
            int xMinIntxn = Math.max(xMin, other.xMin);
            int yMinIntxn = Math.max(yMin, other.yMin);
            int zMinIntxn = Math.max(zMin, other.zMin);
            int xMaxIntxn = Math.min(xMax, other.xMax);
            int yMaxIntxn = Math.min(yMax, other.yMax);
            int zMaxIntxn = Math.min(zMax, other.zMax);
            return new Volume(xMinIntxn, yMinIntxn, zMinIntxn, xMaxIntxn, yMaxIntxn, zMaxIntxn);
        }
    }

    public List<Volume> subtract(Volume other) {
        if (other.subsumes(this)) {
            return Collections.emptyList();
        } else if (!other.overlaps(this)) {
            return List.of(this);
        } else {
            Volume intersection = other.intersection(this);
            if (intersection.xMin > xMin) {
                List<Volume> xSlices = xSlice(intersection.xMin);
                return merge(xSlices.get(1).subtract(intersection), xSlices.get(0));
            } else if (intersection.yMin > yMin) {
                List<Volume> ySlices = ySlice(intersection.yMin);
                return merge(ySlices.get(1).subtract(intersection), ySlices.get(0));
            } else if (intersection.zMin > zMin) {
                List<Volume> zSlices = zSlice(intersection.zMin);
                return merge(zSlices.get(1).subtract(intersection), zSlices.get(0));
            } else {
                return subtractAfterTrimming(intersection);
            }
        }
    }

    private List<Volume> subtractAfterTrimming(Volume intersection) {
        List<Volume> difference = new ArrayList<>();
        Volume remaining = this;
        if (intersection.xMax < xMax) {
            List<Volume> xSlices = remaining.xSlice(intersection.xMax + 1);
            difference.add(xSlices.get(1));
            remaining = xSlices.get(0);
        }
        if (intersection.yMax < yMax) {
            List<Volume> ySlices = remaining.ySlice(intersection.yMax + 1);
            difference.add(ySlices.get(1));
            remaining = ySlices.get(0);
        }
        if (intersection.zMax < zMax) {
            List<Volume> zSlices = remaining.zSlice(intersection.zMax + 1);
            difference.add(zSlices.get(1));
            remaining = zSlices.get(0);
        }
        assert(remaining.equals(intersection));
        return Collections.unmodifiableList(difference);
    }

    public List<Volume> subdivide() {
        return subdivide(xMidpoint(), yMidpoint(), zMidpoint());
    }

    public List<Volume> subdivide(int xMid, int yMid, int zMid) {
        List<Volume> subVolumes = List.of(this);
        subVolumes = subdivideOnDimension(subVolumes, xMin, xMax, xMid, (volume -> volume::xSlice));
        subVolumes = subdivideOnDimension(subVolumes, yMin, yMax, yMid, (volume -> volume::ySlice));
        subVolumes = subdivideOnDimension(subVolumes, zMin, zMax, zMid, (volume -> volume::zSlice));
        return subVolumes;
    }

    private List<Volume> subdivideOnDimension(List<Volume> subVolumes, int min, int max, int mid,
                                              Function<Volume, Function<Integer, List<Volume>>> sliceFunc) {
        if (max - min <= 0 || mid <= min || mid > max) {
            return subVolumes;
        } else {
            List<Volume> sliced = new ArrayList<>();
            for (Volume volume : subVolumes) {
                sliced.addAll(sliceFunc.apply(volume).apply(mid));
            }
            return sliced;
        }
    }

    public int xMidpoint() {
        return midpoint(xMin, xMax);
    }

    public int yMidpoint() {
        return midpoint(yMin, yMax);
    }

    public int zMidpoint() {
        return midpoint(zMin, zMax);
    }

    private static int midpoint(int min, int max) {
        return (min + max + 1) / 2;
    }

    private List<Volume> xSlice(int x) {
        Volume first = new Volume(xMin, yMin, zMin, x - 1, yMax, zMax);
        Volume second = new Volume(x, yMin, zMin, xMax, yMax, zMax);
        return List.of(first, second);
    }

    private List<Volume> ySlice(int y) {
        Volume first = new Volume(xMin, yMin, zMin, xMax, y - 1, zMax);
        Volume second = new Volume(xMin, y, zMin, xMax, yMax, zMax);
        return List.of(first, second);
    }

    private List<Volume> zSlice(int z) {
        Volume first = new Volume(xMin, yMin, zMin, xMax, yMax, z - 1);
        Volume second = new Volume(xMin, yMin, z, xMax, yMax, zMax);
        return List.of(first, second);
    }

    private boolean overlaps(Volume other) {
        return other.xMax >= xMin && other.xMin <= xMax
                && other.yMax >= yMin && other.yMin <= yMax
                && other.zMax >= zMin && other.zMin <= zMax;
    }

    private boolean subsumes(Volume other) {
        return other.xMin >= xMin && other.xMax <= xMax
                && other.yMin >= yMin && other.yMax <= yMax
                && other.zMin >= zMin && other.zMax <= zMax;
    }

    private static List<Volume> merge(List<Volume> volumes, Volume add) {
        List<Volume> volumesMut = new ArrayList<>(volumes);
        volumesMut.add(add);
        return Collections.unmodifiableList(volumesMut);
    }

    public long size() {
        if (this == NULL) {
            return 0;
        } else {
            return (long) (xMax - xMin + 1) * (yMax - yMin + 1) * (zMax - zMin + 1);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Volume)) {
            return false;
        } else {
            Volume other = (Volume) obj;
            return xMin == other.xMin && yMin == other.yMin && zMin == other.zMin
                    && xMax == other.xMax && yMax == other.yMax && zMax == other.zMax;
        }
    }

    @Override
    public int hashCode() {
        int[] vals = {xMin, yMin, zMin, xMax, yMax, zMax};
        return Arrays.hashCode(vals);
    }

    @Override
    public String toString() {
        return "x=" + xMin + ".." + xMax + ",y=" + yMin + ".." + yMax + ",z=" + zMin + ".." + zMax;
    }
}
