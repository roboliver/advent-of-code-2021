import java.util.function.BinaryOperator;

public class ContentsCollection implements Contents {
    private long value;
    private final BinaryOperator<Long> operator;
    private int versionSum = 0;
    private final LengthType lengthType;
    private int length = 0;

    public ContentsCollection(long init, BinaryOperator<Long> operator, LengthType lengthType) {
        this.value = init;
        this.operator = operator;
        this.lengthType = lengthType;
    }

    @Override
    public boolean isDone() {
        return !lengthType.hasNextSubPacket();
    }

    @Override
    public void accept(PacketResult subPacket) {
        lengthType.consume(subPacket.getLength());
        value = operator.apply(value, subPacket.getValue());
        versionSum += subPacket.getVersionSum();
        length += subPacket.getLength();
    }

    @Override
    public long getValue() {
        assertDone();
        return value;
    }

    @Override
    public int getVersionSum() {
        assertDone();
        return versionSum;
    }

    @Override
    public int getLength() {
        assertDone();
        return length;
    }

    private void assertDone() {
        if (!isDone()) {
            throw new IllegalStateException("not fully processed yet");
        }
    }
}
