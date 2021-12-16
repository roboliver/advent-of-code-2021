import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Risk of best path: " + bestPathRisk(lineReader, 1));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Risk of best path in quintupled cave: " + bestPathRisk(lineReader, 5));
        }
    }

    public static int bestPathRisk(BufferedReader lineReader, int scale) throws IOException {
        long timeBefore = System.currentTimeMillis();
        int[][] cave = scaleCave(Utils.readIntArray(lineReader), scale);
        CavePosition[][] traversedCave = traverseCave(cave);
        long timeAfter = System.currentTimeMillis();
        System.out.println("calculated in " + (timeAfter - timeBefore) + "ms");
        return caveExit(traversedCave).bestPathTo();
    }

    private static CavePosition[][] traverseCave(int[][] cave) {
        CavePosition[][] traversedCave = new CavePosition[cave.length][cave[0].length];
        for (int row = 0; row < traversedCave.length; row++) {
            for (int col = 0; col < traversedCave[row].length; col++) {
                traversedCave[row][col] = new CavePosition(cave[row][col], row == 0 && col == 0);
                if (row > 0) {
                    traversedCave[row][col].addNeighbour(traversedCave[row - 1][col]);
                }
                if (col > 0) {
                    traversedCave[row][col].addNeighbour(traversedCave[row][col - 1]);
                }
            }
        }
        return traversedCave;
    }

    private static CavePosition caveExit(CavePosition[][] cave) {
        return cave[cave.length - 1][cave[0].length - 1];
    }

    private static int[][] scaleCave(int[][] caveOriginal, int scale) {
        if (scale < 1) {
            throw new IllegalArgumentException("scale must be positive");
        }
        int[][] caveScaled = new int[caveOriginal.length * scale][caveOriginal[0].length * scale];
        for (int row = 0; row < caveScaled.length; row++) {
            int rowScale = row / caveOriginal.length;
            int rowOriginal = row % caveOriginal.length;
            for (int col = 0; col < caveScaled[row].length; col++) {
                int colScale = col / caveOriginal[rowOriginal].length;
                int colOriginal = col % caveOriginal[rowOriginal].length;
                int chitonsOriginal = caveOriginal[rowOriginal][colOriginal];
                caveScaled[row][col] = scaleChitons(chitonsOriginal, rowScale, colScale);
            }
        }
        return caveScaled;
    }

    private static int scaleChitons(int chitonsOriginal, int rowScale, int colScale) {
        return ((chitonsOriginal + rowScale + colScale - 1) % CavePosition.MAX_CHITONS) + 1;
    }
}
