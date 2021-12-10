import java.util.*;

public class Heightmap {
    private final List<Basin> basins;

    public Heightmap(int[][] heights) {
        List<Basin> basins = new ArrayList<>();
        Basin[][] basinGrid = new Basin[heights.length][heights[0].length];
        for (int i = 0; i < heights.length; i++) {
            for (int j = 0; j < heights[i].length; j++) {
                if (partOfNewBasin(heights[i][j], basinGrid[i][j])) {
                    Basin basin = new Basin();
                    basins.add(basin);
                    basinBuild(heights, basinGrid, i, j, basin);
                }
            }
        }
        this.basins = basins;
    }

    private void basinBuild(int[][] heights, Basin[][] basinGrid, int i, int j, Basin basin) {
        int point = heights[i][j];
        if (partOfNewBasin(point, basinGrid[i][j])) {
            basinGrid[i][j] = basin;
            basin.addPoint(point);
            if (i > 0) {
                basinBuild(heights, basinGrid, i - 1, j, basin);
            }
            if (i < heights.length - 1) {
                basinBuild(heights, basinGrid, i + 1, j, basin);
            }
            if (j > 0) {
                basinBuild(heights, basinGrid, i, j - 1, basin);
            }
            if (j < heights[i].length - 1) {
                basinBuild(heights, basinGrid, i, j + 1, basin);
            }
        }
    }

    private static boolean partOfNewBasin(int point, Basin basinExisting) {
        return point < 9 && basinExisting == null;
    }

    public int lowpointRiskLevel() {
        return basins.stream()
                .map(Basin::lowpointRiskLevel)
                .reduce(0, Integer::sum);
    }

    public int largestNBasins(int n) {
        this.basins.sort(Comparator.comparingInt(Basin::size));
        int prod = 1;
        for (int i = 0; i < n; i++) {
            int basinIndex = basins.size() - 1 - i;
            prod *= basins.get(basinIndex).size();
        }
        return prod;
    }
}
