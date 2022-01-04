import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static final int SHARED_BEACONS = 12;
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            long timeBefore = System.currentTimeMillis();
            System.out.println("Beacon count: " + beaconCount(lineReader));
            long timeAfter = System.currentTimeMillis();
            System.out.println("it took " + (timeAfter - timeBefore) + " millis.");
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Maximum manhattan distance between beacons: " + maxManhattanDistance(lineReader));
        }
    }

    public static int beaconCount(BufferedReader lineReader) throws IOException {
        return supercluster(lineReader).size();
    }

    public static int maxManhattanDistance(BufferedReader lineReader) throws IOException {
        Cluster supercluster = supercluster(lineReader);
        int max = 0;
        for (Position beacon : supercluster.scanners()) {
            for (Position beaconCompare : supercluster.scanners()) {
                int manhattanDistance = Math.abs(beacon.x() - beaconCompare.x())
                        + Math.abs(beacon.y() - beaconCompare.y())
                        + Math.abs(beacon.z() - beaconCompare.z());
                max = Math.max(max, manhattanDistance);
            }
        }
        return max;
    }

    public static Cluster supercluster(BufferedReader lineReader) throws IOException {
        List<Cluster> clusters = clusters(lineReader);
        List<Cluster> scClusters = new ArrayList<>();
        Cluster superCluster = clusters.remove(0);
        scClusters.add(superCluster);
        while (!clusters.isEmpty()) {
            for (int i = 0; i < clusters.size(); i++) {
                Cluster cluster = clusters.get(i);
                Cluster clusterToAdd = null;
                for (Cluster scCluster : scClusters) {
                    for (Position scBeacon : scCluster.beacons()) {
                        Map<Position, Distance> scDistances = scCluster.otherBeaconDistances(scBeacon);
                        int cBeaconsTried = 0;
                        for (Position cBeacon : cluster.beacons()) {
                            Set<Distance> cDistances = cluster.distancesToOtherBeacons(cBeacon);
                            Set<Position> scMatches = new HashSet<>();
                            for (Map.Entry<Position, Distance> scDistance : scDistances.entrySet()) {
                                if (cDistances.contains(scDistance.getValue())) {
                                    scMatches.add(scDistance.getKey());
                                }
                            }
                            if (scMatches.size() >= (SHARED_BEACONS - 1)) {
                                clusterToAdd = clusterMatch(cluster, scBeacon, cBeacon, scMatches);
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
                        break;
                    }
                }
                if (clusterToAdd != null) {
                    superCluster = superCluster.combine(clusterToAdd);
                    scClusters.add(clusterToAdd);
                    clusters.remove(i);
                    break;
                }
                else if (i == clusters.size() - 1) {
                    throw new IllegalStateException("couldn't merge any of the clusters into the supercluster");
                }
            }
        }
        return superCluster;
    }

    private static Cluster clusterMatch(Cluster cluster, Position scBeacon, Position cBeacon, Set<Position> scMatches) {
        Cluster clusterTranslated = cluster.translate(scBeacon.x() - cBeacon.x(),
                scBeacon.y() - cBeacon.y(),
                scBeacon.z() - cBeacon.z());
        for (int pitch = 0; pitch < 4; pitch++) {
            for (int roll = 0; roll < 4; roll++) {
                for (int yaw = 0; yaw < 4; yaw++) {
                    Cluster clusterRotated = clusterTranslated.rotate(scBeacon.x(), scBeacon.y(), scBeacon.z(), pitch, roll, yaw);
                    if (clusterRotated.containsCount(scMatches) >= SHARED_BEACONS - 1) {
                        return clusterRotated;
                    }
                }
            }
        }
        return null;
    }

    public static List<Cluster> clusters(BufferedReader lineReader) throws IOException {
        List<Cluster> clusters = new ArrayList<>();
        List<Position> beacons = null;
        String line;
        while ((line = lineReader.readLine()) !=  null) {
            if (line.isBlank()) {
                // end of a cluster
                clusters.add(new Cluster(beacons));
            } else if (line.startsWith("---")) {
                // start of a new cluster
                beacons = new ArrayList<>();
            } else {
                // in a cluster
                String[] coords = line.split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int z = Integer.parseInt(coords[2]);
                beacons.add(new Position(x, y, z));
            }
        }
        // add the last cluster
        if (beacons != null) {
            clusters.add(new Cluster(beacons));
        }
        return clusters;
    }
}
