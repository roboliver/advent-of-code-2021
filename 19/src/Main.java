import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final int SHARED_BEACONS = 12;
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Beacon count: " + beaconCount(lineReader));
        }
    }

    public static int beaconCount(BufferedReader lineReader) throws IOException {
        List<Cluster> clusters = clusters(lineReader);
        Cluster superCluster = clusters.remove(0);
        while (!clusters.isEmpty()) {
            //System.out.println("we're here. there are " + clusters.size() + " clusters left to add.");
            //System.out.println("current supercluster size: " + superCluster.size());
            for (int i = 0; i < clusters.size(); i++) {
                Cluster cluster = clusters.get(i);
                Cluster clusterToAdd = null;
                for (Beacon scBeacon : superCluster.beacons()) {
                    Map<Beacon, Distance> scBeaconDistances = superCluster.otherBeaconDistances(scBeacon);
                    int cBeaconsTried = 0;
                    for (Beacon cBeacon : cluster.beacons()) {
                        Set<Distance> cDistances = cluster.distancesToOtherBeacons(cBeacon);
                        Set<Beacon> scBeaconMatches = new HashSet<>();
                        for (Map.Entry<Beacon, Distance> scBeaconDistance : scBeaconDistances.entrySet()) {
                            if (cDistances.contains(scBeaconDistance.getValue())) {
                                scBeaconMatches.add(scBeaconDistance.getKey());
                            }
                        }
                        if (scBeaconMatches.size() >= (SHARED_BEACONS - 1)) {
                            //System.out.println("possible match on cluster " + i + ", beacon " + cBeacon + ", with " + scBeaconMatches.size());
                            clusterToAdd = clusterMatch(cluster, scBeacon, cBeacon, scBeaconMatches);
                            if (clusterToAdd != null) {
                                break;
                            }
                        }
                        if (cBeaconsTried == cluster.size() - (SHARED_BEACONS - 1)) {
                            break;
                        }
                    }
                    if (clusterToAdd != null) {
                        break;
                    }
                }
                if (clusterToAdd != null) {
                    //System.out.println("adding a cluster to the supercluster!");
                    superCluster.addCluster(clusterToAdd);
                    clusters.remove(i);
                    break;
                }
                else if (i == clusters.size() - 1) {
                    throw new IllegalStateException("couldn't merge any of the clusters into the supercluster");
                }
            }
        }
        return superCluster.size();
    }

    private static Cluster clusterMatch(Cluster cluster, Beacon scBeacon, Beacon cBeacon, Set<Beacon> scBeaconMatches) {
        //System.out.println("got a possible match!");
        Cluster clusterTranslated = cluster.translate(scBeacon.x() - cBeacon.x(),
                scBeacon.y() - cBeacon.y(),
                scBeacon.z() - cBeacon.z());
        for (int pitch = 0; pitch < 4; pitch++) {
            for (int roll = 0; roll < 4; roll++) {
                for (int yaw = 0; yaw < 4; yaw++) {
                    Cluster clusterRotated = clusterTranslated.rotate(scBeacon.x(), scBeacon.y(), scBeacon.z(), pitch, roll, yaw);
                    //System.out.println("contains count : " + clusterRotated.containsCount(scBeaconMatches));
                    if (clusterRotated.containsCount(scBeaconMatches) >= SHARED_BEACONS - 1) {
                        //System.out.println("rotated it and actually got a match!");
                        return clusterRotated;
                    }
                }
            }
        }
        return null;
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
