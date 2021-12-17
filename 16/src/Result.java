/**
 * The result of a packet that has been fully processed.
 */
public class Result {
    private final int versionSum;
    private final int length;
    private final long value;

    public Result(int versionSum, int length, long value) {
        this.versionSum = versionSum;
        this.length = length;
        this.value = value;
    }

    public int getVersionSum() {
        return versionSum;
    }

    public int getLength() {
        return length;
    }

    public long getValue() {
        return value;
    }
}
