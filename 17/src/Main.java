import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = Utils.inputLineReader()) {
            System.out.println("highest arc: " + highestArc(reader));
        }
        try (BufferedReader reader = Utils.inputLineReader()) {
            System.out.println("number of valid arcs: " + arcCount(reader));
        }
    }

    public static int highestArc(BufferedReader reader) throws IOException {
        Target target = target(reader);
        return 0;
    }

    public static int arcCount(BufferedReader reader) throws IOException {
        return 0;
    }

    private static Target target(BufferedReader reader) throws IOException {
        String[] parts = reader.readLine().split(",");
        int xMin = Integer.parseInt(parts[0].substring(parts[0].indexOf("=") + 1, parts[0].indexOf("..")));
        int xMax = Integer.parseInt(parts[0].substring(parts[0].indexOf("..") + 2));
        int yMin = Integer.parseInt(parts[1].substring(parts[1].indexOf("=") + 1, parts[1].indexOf("..")));
        int yMax = Integer.parseInt(parts[1].substring(parts[1].indexOf("..") + 2));
        return new Target(xMin, xMax, yMin, yMax);
    }
}
