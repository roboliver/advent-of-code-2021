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
        int[][] heights = Utils.readIntArray(lineReader);
        Heightmap heightmap = new Heightmap(heights);
        return heightmap.lowpointRiskLevel();
    }

    public static int largestNBasinsProduct(BufferedReader lineReader, int n) throws IOException {
        int[][] heights = Utils.readIntArray(lineReader);
        Heightmap heightmap = new Heightmap(heights);
        return heightmap.largestNBasinsProduct(n);
    }
}
