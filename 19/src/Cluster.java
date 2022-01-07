import java.util.*;

public class Cluster {
    private final Set<Position> beacons;
    private final Set<Position> scanners;

    public Cluster(Collection<Position> beacons) {
        this(Set.of(new Position(0, 0, 0)), Set.copyOf(beacons));
    }

    private Cluster(Set<Position> scanners, Set<Position> beacons) {
        this.scanners = Collections.unmodifiableSet(scanners);
        this.beacons = Collections.unmodifiableSet(beacons);
    }

    public Cluster combine(Cluster cluster) {
        Set<Position> scanners = new HashSet<>();
        scanners.addAll(this.scanners);
        scanners.addAll(cluster.scanners);
        Set<Position> beacons = new HashSet<>();
        beacons.addAll(this.beacons);
        beacons.addAll(cluster.beacons);
        return new Cluster(scanners, beacons);
    }

    public Cluster rotate(int xOrigin, int yOrigin, int zOrigin, int pitch, int roll, int yaw) {
        Set<Position> scannersRotated = new HashSet<>();
        Set<Position> beaconsRotated = new HashSet<>();
        for (Position scanner : scanners) {
            scannersRotated.add(scanner.rotate(xOrigin, yOrigin, zOrigin, pitch, roll, yaw));
        }
        for (Position beacon : beacons) {
            beaconsRotated.add(beacon.rotate(xOrigin, yOrigin, zOrigin, pitch, roll, yaw));
        }
        return new Cluster(scannersRotated, beaconsRotated);
    }

    public Cluster translate(int x, int y, int z) {
        Set<Position> scannersTranslated = new HashSet<>();
        Set<Position> beaconsTranslated = new HashSet<>();
        for (Position scanner : scanners) {
            scannersTranslated.add(scanner.translate(x, y, z));
        }
        for (Position beacon : beacons) {
            beaconsTranslated.add(beacon.translate(x, y, z));
        }
        return new Cluster(scannersTranslated, beaconsTranslated);
    }

    public Set<Position> beacons() {
        return beacons;
    }

    public Set<Position> scanners() {
        return scanners;
    }

    public Map<Position, Distance> distancesToOtherBeacons(Position beaconCompare) {
        Map<Position, Distance> beaconDistances = new HashMap<>();
        for (Position beacon : beacons) {
            if (!beacon.equals(beaconCompare)) {
                beaconDistances.put(beacon, beacon.distanceTo(beaconCompare));
            }
        }
        return beaconDistances;
    }

    public int containsCount(Set<Position> beaconsCheck) {
        int count = 0;
        for (Position beacon : beaconsCheck) {
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
        for (Position beacon : beacons) {
            if (buf.length() > 0) {
                buf.append('\n');
            }
            buf.append(beacon.toString());
        }
        return buf.toString();
    }
}
