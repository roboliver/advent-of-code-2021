import java.util.*;

public class Cluster {
    private final Set<Beacon> beacons = new HashSet<>();

    public void addBeacon(Beacon beacon) {
        beacons.add(Objects.requireNonNull(beacon));
    }

    public void addCluster(Cluster cluster) {
        for (Beacon beacon : cluster.beacons) {
            addBeacon(beacon);
        }
    }

    public Cluster rotate(int xOrigin, int yOrigin, int zOrigin, int pitch, int roll, int yaw) {
        Cluster rotated = new Cluster();
        for (Beacon beacon : beacons) {
            Beacon beaconRotated = beacon.translate(0 - xOrigin, 0 - yOrigin, 0 - zOrigin)
                    .rotate(pitch, roll, yaw)
                    .translate(xOrigin, yOrigin, zOrigin);
            rotated.addBeacon(beaconRotated);
        }
        return rotated;
    }

    public Cluster translate(int x, int y, int z) {
        Cluster translated = new Cluster();
        for (Beacon beacon : beacons) {
            translated.addBeacon(beacon.translate(x, y, z));
        }
        return translated;
    }

    public Set<Beacon> beacons() {
        return Collections.unmodifiableSet(beacons);
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
