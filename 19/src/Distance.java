public class Distance {
    private final double distance;
    // source: I made it up
    // But seriously: if this is too high, we will get false positives, where two beacons will look like they're the
    // same distance away until we check properly (not ideal, but basically fine). If this is too low, we will miss
    // beacons that really are a match (very bad). I have arbitrarily picked 16 bits because it "seems like a good
    // number of bits to round to". But really this is just a guess. The beacon positions are seemingly between -1000
    // and 1000... surely we are not going to be very far out at all with these values? (Are we even going to be out
    // even slightly??)
    private static final int ROUND_SIGNIFICAND_TO_BITS = 16;

    public Distance(double distance) {
        this.distance = magicRound(distance);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Distance)) {
            return false;
        } else {
            return this.distance == ((Distance) obj).distance;
        }
    }

    @Override
    public int hashCode() {
        return Double.hashCode(distance);
    }

    private static double magicRound(double distance) {
        // this is some magic technique for rounding floating point values to a desired number of significand bits that
        // I found on StackOverflow: https://stackoverflow.com/a/41588543
        // I'll be honest: I have no idea how this works.
        long magic = 1 + (long) Math.pow(2, 53 - ROUND_SIGNIFICAND_TO_BITS);
        double t = magic * distance;
        return distance - t + t;
    }

    @Override
    public String toString() {
        return Double.toString(distance);
    }
}
