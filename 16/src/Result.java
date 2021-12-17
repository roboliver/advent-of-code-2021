/**
 * The result of a packet that has been fully processed.
 */
public class Result {
    private final int versionSum;
    private final long value;
    private final int length;

    public Result(int versionSum, long value, int length) {
        this.versionSum = versionSum;
        this.value = value;
        this.length = length;
    }

    public int getVersionSum() {
        return versionSum;
    }

    public long getValue() {
        return value;
    }

    public int getLength() {
        return length;
    }
}
