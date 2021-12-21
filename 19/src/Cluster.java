import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
