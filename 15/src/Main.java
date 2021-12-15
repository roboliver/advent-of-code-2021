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

    public static int bestPathRisk(BufferedReader lineReader, int caveMultiplier) throws IOException {
        int[][] cavePositionsArray = Utils.readIntArray(lineReader);
        cavePositionsArray = scaleCave(cavePositionsArray, caveMultiplier);
        CavePosition[][] cavePositions = new CavePosition[cavePositionsArray.length][cavePositionsArray[0].length];
        for (int row = 0; row < cavePositions.length; row++) {
            for (int col = 0; col < cavePositions[row].length; col++) {
                int chitons = cavePositionsArray[row][col];
                CavePosition cavePosition;
                if (row == 0 && col == 0) {
                    cavePosition = CavePosition.createStart(chitons, row, col);
                } else {
                    cavePosition = new CavePosition(chitons, row, col);
                }
                cavePositions[row][col] = cavePosition;
                if (row > 0) {
                    //System.out.println("adding vertical neighbour: [r" + (row-1) + ",c" + col + "] to [r" + row + ",c" + col + "]");
                    cavePosition.addNeighbour(cavePositions[row - 1][col]);
                }
                if (col > 0) {
                    //System.out.println("adding horizontal neighbour: [r" + row + ",c" + (col - 1) + "] to [r" + row + ",c" + col + "]");
                    cavePosition.addNeighbour(cavePositions[row][col - 1]);
                }
            }
        }
        //printGrid(cavePositions);
        return cavePositions[cavePositions.length-1][cavePositions[0].length-1].bestPath();
    }

    private static int[][] scaleCave(int[][] caveOriginal, int scale) {
        int[][] caveNew = new int[caveOriginal.length * scale][caveOriginal[0].length * scale];
        for (int row = 0; row < caveNew.length; row++) {
            int rowScalePos = row / caveOriginal.length;
            int rowOriginal = row % caveOriginal.length;
            for (int col = 0; col < caveNew[row].length; col++) {
                int colScalePos = col / caveOriginal[rowOriginal].length;
                int colOriginal = col % caveOriginal[rowOriginal].length;
                int sumScalePos = rowScalePos + colScalePos;
                int chitonOriginal = caveOriginal[rowOriginal][colOriginal];
                int chitonNew = ((chitonOriginal + sumScalePos - 1) % 9) + 1;
                caveNew[row][col] = chitonNew;
            }
        }
        return caveNew;
    }

    private static void printGrid(CavePosition[][] cavePositions) {
        StringBuilder buf = new StringBuilder();
        for (int row = 0; row < cavePositions.length; row++) {
            if (row > 0) {
                buf.append('\n');
            }
            for (int col = 0; col < cavePositions[row].length; col++) {
                buf.append("|");
                buf.append(String.format("%3d", cavePositions[row][col].bestPath()));
            }
            buf.append("|");
        }
        System.out.println(buf.toString());
    }
}
