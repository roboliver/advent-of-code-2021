import java.util.function.BinaryOperator;

/**
 * Packet contents that contain a variable number of sub-packets, where the result value comes from collating the value
 * of all of them somehow.
 */
public class ContentsCollection implements Contents {
    private long value;
    private final BinaryOperator<Long> operator;
    private int versionSum = 0;
    private final LengthType lengthType;
    private int length = 0;

    public ContentsCollection(long valueInit, BinaryOperator<Long> operator, LengthType lengthType) {
        this.value = valueInit;
        this.operator = operator;
        this.lengthType = lengthType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFullyProcessed() {
        return !lengthType.hasNextSubPacket();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSubPacket(Result subPacket) {
        lengthType.addSubPacket(subPacket.getLength());
        value = operator.apply(value, subPacket.getValue());
        versionSum += subPacket.getVersionSum();
        length += subPacket.getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getValue() {
        assertFullyProcessed();
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVersionSum() {
        assertFullyProcessed();
        return versionSum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        assertFullyProcessed();
        return length;
    }

    private void assertFullyProcessed() {
        if (!isFullyProcessed()) {
            throw new IllegalStateException("not fully processed yet");
        }
    }
}
