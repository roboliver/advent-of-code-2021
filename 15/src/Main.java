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
        int[][] caveArray = scaleCave(Utils.readIntArray(lineReader), scale);
        CavePosition[][] cave = navigateCave(caveArray);
        long timeAfter = System.currentTimeMillis();
        System.out.println("calculated in " + (timeAfter - timeBefore) + "ms");
        return caveExit(cave).bestPathTo();
    }

    private static CavePosition[][] navigateCave(int[][] caveArray) {
        CavePosition[][] cave = new CavePosition[caveArray.length][caveArray[0].length];
        for (int row = 0; row < cave.length; row++) {
            for (int col = 0; col < cave[row].length; col++) {
                cave[row][col] = new CavePosition(caveArray[row][col], row == 0 && col == 0);
                if (row > 0) {
                    cave[row][col].addNeighbour(cave[row - 1][col]);
                }
                if (col > 0) {
                    cave[row][col].addNeighbour(cave[row][col - 1]);
                }
            }
        }
        return cave;
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

    public static void printGrid(CavePosition[][] cave) {
        StringBuilder buf = new StringBuilder();
        for (int row = 0; row < cave.length; row++) {
            if (row > 0) {
                buf.append('\n');
            }
            for (int col = 0; col < cave[row].length; col++) {
                buf.append('|');
                buf.append(String.format("%3d", cave[row][col].bestPathTo()));
            }
            buf.append('|');
        }
        System.out.println(buf);
    }
}
