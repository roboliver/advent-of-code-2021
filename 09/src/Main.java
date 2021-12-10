import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Lowpoint risk level: " + lowpointRiskLevel(lineReader));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Largest 3 basins product: " + largestNBasinProd(lineReader, 3));
        }
    }

    public static int lowpointRiskLevel(BufferedReader lineReader) throws IOException {
        int[][] heights = heights(lineReader);
        Heightmap heightmap = new Heightmap(heights);
        return heightmap.lowpointRiskLevel();
    }

    public static int largestNBasinProd(BufferedReader lineReader, int n) throws IOException {
        int[][] heights = heights(lineReader);
        Heightmap heightmap = new Heightmap(heights);
        return heightmap.largestNBasins(n);
    }

//    public static int lowpointRiskLevel(BufferedReader lineReader) throws IOException {
//        int[][] heights = heights(lineReader);
//        int riskLevel = 0;
//        for (int i = 0; i < heights.length; i++) {
//            for (int j = 0; j < heights[i].length; j++) {
//                int height = heights[i][j];
//                int heightUp = i == 0 ? Integer.MAX_VALUE : heights[i - 1][j];
//                int heightDown = i == heights.length - 1 ? Integer.MAX_VALUE : heights[i + 1][j];
//                int heightLeft = j == 0 ? Integer.MAX_VALUE : heights[i][j - 1];
//                int heightRight = j == heights[i].length - 1 ? Integer.MAX_VALUE : heights[i][j + 1];
//                boolean lowpoint = height < heightUp && height < heightDown
//                        && height < heightLeft && height < heightRight;
//                if (lowpoint) {
//                    riskLevel += 1 + height;
//                }
//            }
//        }
//        return riskLevel;
//    }

    private static int[][] heights(BufferedReader lineReader) throws IOException {
        List<List<Integer>> heights = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            List<Integer> lineHeights = new ArrayList<>();
            heights.add(lineHeights);
            for (char c : line.toCharArray()) {
                lineHeights.add(Character.getNumericValue(c));
            }
        }
        int[][] heightsArray = new int[heights.size()][heights.get(0).size()];
        for (int i = 0; i < heightsArray.length; i++) {
            List<Integer> lineHeights = heights.get(i);
            heightsArray[i] = new int[lineHeights.size()];
            for (int j = 0; j < lineHeights.size(); j++) {
                heightsArray[i][j] = lineHeights.get(j);
            }
        }
        return heightsArray;
    }
}
