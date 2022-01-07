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

    private static Cluster supercluster(BufferedReader lineReader) throws IOException {
        List<Cluster> clusters = clusters(lineReader);
        // build the clusters up into a single supercluster, which has all clusters correctly oriented and positioned,
        // with overlapping beacons deduplicated
        List<Cluster> scClusters = new ArrayList<>();
        Cluster supercluster = clusters.remove(0);
        scClusters.add(supercluster);
        while (!clusters.isEmpty()) {
            Cluster clusterToAdd = extractClusterToAdd(clusters, scClusters);
            supercluster = supercluster.combine(clusterToAdd);
            scClusters.add(clusterToAdd);
        }
        return supercluster;
    }

    private static Cluster extractClusterToAdd(List<Cluster> clusters, List<Cluster> scClusters) {
        // find a cluster we can add, align it with the supercluster, remove it from the cluster list, and return it
        for (int i = 0; i < clusters.size(); i++) {
            Cluster cluster = clusters.get(i);
            Cluster clusterToAdd = clusterMatch(scClusters, cluster);
            if (clusterToAdd != null) {
                clusters.remove(i);
                return clusterToAdd;
            }
        }
        throw new IllegalStateException("couldn't merge any of the clusters into the supercluster");
    }

    private static Cluster clusterMatch(List<Cluster> scClusters, Cluster cluster) {
        // try each cluster within the supercluster to see if overlaps the cluster we're trying to add
        for (Cluster scCluster : scClusters) {
            // try each beacon in the supercluster in turn, initially just checking the beacon's distances to the other
            // beacons -- once we find a sufficiently large set of matching distances, we will ensure that there's a way
            // to align the candidate cluster such that enough beacons overlap
            for (Position scBeacon : scCluster.beacons()) {
                Map<Position, Distance> scDistances = scCluster.distancesToBeaconsByBeacon(scBeacon);
                int cBeaconsTried = 0;
                for (Position cBeacon : cluster.beacons()) {
                    Set<Distance> cDistances = cluster.distancesToBeacons(cBeacon);
                    Set<Position> scMatches = new HashSet<>();
                    for (Map.Entry<Position, Distance> scDistance : scDistances.entrySet()) {
                        if (cDistances.contains(scDistance.getValue())) {
                            scMatches.add(scDistance.getKey());
                        }
                    }
                    if (scMatches.size() >= SHARED_BEACONS) {
                        // we've got enough distance matches for this to plausibly be a true match, so see if there's a
                        // way to orient the cluster to create one
                        Cluster clusterToAdd = clusterAlign(cluster, scBeacon, cBeacon, scMatches);
                        if (clusterToAdd != null) {
                            return clusterToAdd;
                        }
                    }
                    if (cBeaconsTried == cluster.size() - (SHARED_BEACONS - 1)) {
                        // no point in trying more beacons -- if the supercluster beacon we are trying corresponded to
                        // a beacon in the candidate cluster, we would have found it by now, so give up and move on to
                        // the next supercluster beacon
                        break;
                    }
                }
            }
        }
        return null;
    }

    private static Cluster clusterAlign(Cluster cluster, Position scBeacon, Position cBeacon, Set<Position> scMatches) {
        // reposition the candidate cluster so that the beacon it potentially shares with the supercluster is aligned
        // with that beacon in the supercluster
        Cluster clusterTranslated = cluster.translate(scBeacon.x() - cBeacon.x(),
                scBeacon.y() - cBeacon.y(),
                scBeacon.z() - cBeacon.z());
        // try every orientation of the cluster to see if any of them result in enough of the supercluster beacons
        // overlapping it
        for (int pitch = 0; pitch < 4; pitch++) {
            for (int roll = 0; roll < 4; roll++) {
                for (int yaw = 0; yaw < 4; yaw++) {
                    Cluster clusterRotated = clusterTranslated.rotate(scBeacon.x(), scBeacon.y(), scBeacon.z(), pitch, roll, yaw);
                    if (clusterRotated.containsCount(scMatches) >= SHARED_BEACONS) {
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
