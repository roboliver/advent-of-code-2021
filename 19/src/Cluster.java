import java.util.*;

/**
 * A set of beacons and scanners. These will initially be created from the input, with each cluster containing one
 * scanner (at position 0, 0, 0) and its surrounding beacons, but clusters can be translated, rotated, and merged, to
 * create clusters with multiple scanners, and scanners at positions other than the origin.
 */
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

    public Set<Distance> distancesBetweenBeaconsAllDeduplicated() {
        Set<Distance> distances = new HashSet<>();
        for (Position beacon : beacons) {
            distances.addAll(distancesToBeaconsDeduplicated(beacon));
        }
        return distances;
    }

    public List<Distance> distancesBetweenBeaconsAll() {
        List<Distance> distances = new ArrayList<>();
        for (Position beacon : beacons) {
            distances.addAll(distancesToBeaconsByBeacon(beacon).values());
        }
        return distances;
    }

    public Set<Distance> distancesToBeaconsDeduplicated(Position beaconCompare) {
        Set<Distance> distances = new HashSet<>();
        for (Position beacon : beacons) {
            distances.add(beacon.distanceTo(beaconCompare));
        }
        return distances;
    }

    public Map<Position, Distance> distancesToBeaconsByBeacon(Position beaconCompare) {
        Map<Position, Distance> distances = new HashMap<>();
        for (Position beacon : beacons) {
            distances.put(beacon, beacon.distanceTo(beaconCompare));
        }
        return distances;
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
