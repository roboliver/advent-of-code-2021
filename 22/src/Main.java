import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Cubes on in region -50..50 (in all dimensions): " + cubesOn(lineReader, -50, 50));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Cubes on in entire region: " + cubesOn(lineReader));
        }
    }

    public static long cubesOn(BufferedReader lineReader) throws IOException {
        return cubesOn(lineReader, -1000000, 1000000);
    }

    public static long cubesOn(BufferedReader lineReader, int regionMin, int regionMax) throws IOException {
        Region region = new Region(new Volume(regionMin, regionMin, regionMin,
                regionMax, regionMax, regionMax));
        String line;
        int stepCount = 0;
        while ((line = lineReader.readLine()) != null) {
            RebootStep step = rebootStep(line);
            region.executeStep(step);
            stepCount++;
        }
        return region.cubesOn();
    }

    private static RebootStep rebootStep(String line) {
        boolean isOnNotOff;
        if (line.startsWith("on")) {
            isOnNotOff = true;
            line = line.substring("on x=".length());
        } else {
            isOnNotOff = false;
            line = line.substring("off x=".length());
        }
        int xMin = Integer.parseInt(line.substring(0, line.indexOf("..")));
        int xMax = Integer.parseInt(line.substring(line.indexOf("..") + 2, line.indexOf(",")));
        line = line.substring(line.indexOf("y=") + 2);
        int yMin = Integer.parseInt(line.substring(0, line.indexOf("..")));
        int yMax = Integer.parseInt(line.substring(line.indexOf("..") + 2, line.indexOf(",")));
        line = line.substring(line.indexOf("z=") + 2);
        int zMin = Integer.parseInt(line.substring(0, line.indexOf("..")));
        int zMax = Integer.parseInt(line.substring(line.indexOf("..") + 2));
        return new RebootStep(isOnNotOff, new Volume(xMin, yMin, zMin, xMax, yMax, zMax));
    }
}
