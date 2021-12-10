import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class that stores all the basins in the cave, as calculated from the 2d array of heights, and can return the
 * necessary info on these basins.
 */
public class Heightmap {
    // list of basins, ordered by size from smallest to largest
    private final List<Basin> basins;

    public Heightmap(int[][] heights) {
        List<Basin> basins = new ArrayList<>();
        // used to track which points we have already added to their owning basin
        Basin[][] basinGrid = new Basin[heights.length][heights[0].length];
        for (int row = 0; row < heights.length; row++) {
            for (int col = 0; col < heights[row].length; col++) {
                if (isPartOfNewBasin(heights[row][col], basinGrid[row][col])) {
                    Basin basin = new Basin();
                    basins.add(basin);
                    // whenever we find a point that's part of a basin, we fully map it out before proceeding
                    basinBuild(heights, basinGrid, row, col, basin);
                }
            }
        }
        // sort the basins by size before making them immutable
        basins.sort(Comparator.comparingInt(Basin::size));
        this.basins = Collections.unmodifiableList(basins);
    }

    private void basinBuild(int[][] heights, Basin[][] basinGrid, int row, int col, Basin basin) {
        int point = heights[row][col];
        if (isPartOfNewBasin(point, basinGrid[row][col])) {
            basinGrid[row][col] = basin;
            basin.addPoint(point);
            // check all the surrounding points to see if they are part of the same basin
            if (row > 0) {
                basinBuild(heights, basinGrid, row - 1, col, basin);
            }
            if (row < heights.length - 1) {
                basinBuild(heights, basinGrid, row + 1, col, basin);
            }
            if (col > 0) {
                basinBuild(heights, basinGrid, row, col - 1, basin);
            }
            if (col < heights[row].length - 1) {
                basinBuild(heights, basinGrid, row, col + 1, basin);
            }
        }
    }

    private static boolean isPartOfNewBasin(int point, Basin basinExisting) {
        return point < 9 && basinExisting == null;
    }

    public int lowpointRiskLevel() {
        return basins.stream()
                .map(Basin::lowpointRiskLevel)
                .reduce(0, Integer::sum);
    }

    public int largestNBasins(int n) {
        int prod = 1;
        for (int i = 0; i < n; i++) {
            int basinIndex = basins.size() - 1 - i;
            prod *= basins.get(basinIndex).size();
        }
        return prod;
    }
}
