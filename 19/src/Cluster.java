import java.util.*;

public class Cluster {
    private final Set<Beacon> beacons = new HashSet<>();

    public void addBeacon(Beacon beacon) {
        beacons.add(Objects.requireNonNull(beacon));
    }

    public Cluster rotate(int pitch, int roll, int yaw) {
        Cluster rotated = new Cluster();
        for (Beacon beacon : beacons) {
            rotated.addBeacon(beacon.rotate(pitch, roll, yaw));
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

    public Map<Distance, Beacon> distancesToOtherBeacons(Beacon beaconCompare) {
        if (!beacons.contains(beaconCompare)) {
            throw new IllegalArgumentException(("this cluster must contain the specified beacon"));
        }
        Map<Distance, Beacon> distances = new HashMap<>();
        for (Beacon beacon : beacons) {
            if (!beacon.equals(beaconCompare)) {
                distances.put(beacon.distanceTo(beaconCompare), beacon);
            }
        }
        return distances;
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
