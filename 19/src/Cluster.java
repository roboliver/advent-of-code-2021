import java.util.*;

public class Cluster {
    private final Set<Beacon> beacons;
    private final Set<Beacon> scanners;

    public Cluster(Collection<Beacon> beacons) {
        this(Set.of(new Beacon(0, 0, 0)), Set.copyOf(beacons));
    }

    private Cluster(Set<Beacon> scanners, Set<Beacon> beacons) {
        this.scanners = scanners;
        this.beacons = beacons;
    }

    public Cluster addCluster(Cluster cluster) {
        Set<Beacon> scanners = new HashSet<>();
        scanners.addAll(this.scanners);
        scanners.addAll(cluster.scanners);
        Set<Beacon> beacons = new HashSet<>();
        beacons.addAll(this.beacons);
        beacons.addAll(cluster.beacons);
        return new Cluster(scanners, beacons);
    }

    public Cluster rotate(int xOrigin, int yOrigin, int zOrigin, int pitch, int roll, int yaw) {
        Set<Beacon> scannersRotated = new HashSet<>();
        Set<Beacon> beaconsRotated = new HashSet<>();
        for (Beacon scanner : scanners) {
            scannersRotated.add(scanner.rotate(xOrigin, yOrigin, zOrigin, pitch, roll, yaw));
        }
        for (Beacon beacon : beacons) {
            beaconsRotated.add(beacon.rotate(xOrigin, yOrigin, zOrigin, pitch, roll, yaw));
        }
        return new Cluster(Collections.unmodifiableSet(scannersRotated),
                Collections.unmodifiableSet(beaconsRotated));
    }

    public Cluster translate(int x, int y, int z) {
        Set<Beacon> scannersTranslated = new HashSet<>();
        Set<Beacon> beaconsTranslated = new HashSet<>();
        for (Beacon scanner : scanners) {
            scannersTranslated.add(scanner.translate(x, y, z));
        }
        for (Beacon beacon : beacons) {
            beaconsTranslated.add(beacon.translate(x, y, z));
        }
        return new Cluster(Collections.unmodifiableSet(scannersTranslated),
                Collections.unmodifiableSet(beaconsTranslated));
    }

    public Set<Beacon> beacons() {
        return Collections.unmodifiableSet(beacons);
    }

    public Set<Beacon> scanners() {
        return Collections.unmodifiableSet(scanners);
    }

    public Set<Distance> distancesToOtherBeacons(Beacon beaconCompare) {
        assertContainsBeacon(beaconCompare);
        Set<Distance> distances = new HashSet<>();
        for (Beacon beacon : beacons) {
            if (!beacon.equals(beaconCompare)) {
                distances.add(beacon.distanceTo(beaconCompare));
            }
        }
        return distances;
    }

    public Map<Beacon, Distance> otherBeaconDistances(Beacon beaconCompare) {
        assertContainsBeacon(beaconCompare);
        Map<Beacon, Distance> beaconsByDistance = new HashMap<>();
        for (Beacon beacon : beacons) {
            if (!beacon.equals(beaconCompare)) {
                beaconsByDistance.put(beacon, beacon.distanceTo(beaconCompare));
            }
        }
        return beaconsByDistance;
    }

    private void assertContainsBeacon(Beacon beacon) {
        if (!beacons.contains(beacon)) {
            throw new IllegalArgumentException("this cluster must contain the specified beacon");
        }
    }

    public int containsCount(Set<Beacon> beaconsCheck) {
        //System.out.println("checking " + beaconsCheck + " beacons.");
        int count = 0;
        for (Beacon beacon : beaconsCheck) {
            //System.out.println("checking beacon " + beacon);
            if (beacons.contains(beacon)) {
                count++;
            }
        }
        return count;
    }

    public int size() {
        return beacons.size();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (Beacon beacon : beacons) {
            if (buf.length() > 0) {
                buf.append('\n');
            }
            buf.append(beacon.toString());
        }
        return buf.toString();
    }
}
