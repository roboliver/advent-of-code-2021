import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Beacon count: " + beaconCount(lineReader));
        }
    }

    public static int beaconCount(BufferedReader lineReader) throws IOException {
        List<Cluster> clusters = clusters(lineReader);

        return 0;
    }



    public static List<Cluster> clusters(BufferedReader lineReader) throws IOException {
        List<Cluster> clusters = new ArrayList<>();
        Cluster cluster = null;
        String line;
        while ((line = lineReader.readLine()) !=  null) {
            if (line.isBlank()) {
                // end of a cluster
                clusters.add(cluster);
            } else if (line.startsWith("---")) {
                // start of a new cluster
                cluster = new Cluster();
            } else {
                // in a cluster
                String[] coords = line.split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int z = Integer.parseInt(coords[2]);
                cluster.addBeacon(new Beacon(x, y, z));
            }
        }
        // add the last cluster
        if (cluster != null) {
            clusters.add(cluster);
        }
        return clusters;
    }
}
