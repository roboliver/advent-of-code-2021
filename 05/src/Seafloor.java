/**
 * Represents the seafloor. Add vents to it with {@code addVent} and then check the overlaps.
 */
public class Seafloor {

    private final int[][] grid;
    // offsets we will use to compensate for the fact that the grid is indexed from 0 starting from the lowest y and x
    // coordinates it will contain, but the vents are provided with their actual coordinates.
    private final int xOffset;
    private final int yOffset;
    private int ventOverlaps = 0;

    public Seafloor(int xMin, int xMax, int yMin, int yMax) {
        if (xMin > xMax) {
            throw new IllegalArgumentException("x min cannot be higher than x max");
        } else if (yMin > yMax) {
            throw new IllegalArgumentException("y min cannot be higher than y max");
        }
        int height = (yMax - yMin) + 1;
        int width = (xMax - xMin) + 1;
        grid = new int[height][width];
        xOffset = xMin;
        yOffset = yMin;
    }

    public void addVent(Vent vent) {
        Point[] ventPoints = vent.points();
        for (Point point : ventPoints) {
            int xForGrid = point.x() - xOffset;
            int yForGrid = point.y() - yOffset;
            if (grid[yForGrid][xForGrid] == 1) {
                ventOverlaps++;
            }
            grid[yForGrid][xForGrid]++;
        }
    }

    public int ventOverlaps() {
        return ventOverlaps;
    }
}
