import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Lowpoint risk level: " + lowpointRiskLevel(lineReader));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Largest 3 basins product: " + largestNBasinsProduct(lineReader, 3));
        }
    }

    public static int lowpointRiskLevel(BufferedReader lineReader) throws IOException {
        int[][] heights = heights(lineReader);
        Heightmap heightmap = new Heightmap(heights);
        return heightmap.lowpointRiskLevel();
    }

    public static int largestNBasinsProduct(BufferedReader lineReader, int n) throws IOException {
        int[][] heights = heights(lineReader);
        Heightmap heightmap = new Heightmap(heights);
        return heightmap.largestNBasinsProduct(n);
    }

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
        int[][] heightsArray = new int[heights.size()][];
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
