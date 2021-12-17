import java.util.function.BiPredicate;

public class ContentsPair implements Contents {
    private PacketResult first = null;
    private PacketResult second = null;
    private final BiPredicate<Long, Long> predicate;

    public ContentsPair(BiPredicate<Long, Long> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean isDone() {
        return first != null && second != null;
    }

    @Override
    public void accept(PacketResult subPacket) {
        if (isDone()) {
            throw new IllegalStateException("already have both subpackets");
        }
        if (first == null) {
            first = subPacket;
        } else {
            second = subPacket;
        }
    }

    @Override
    public long getValue() {
        assertDone();
        return predicate.test(first.getValue(), second.getValue()) ? 1 : 0;
    }

    @Override
    public int getVersionSum() {
        assertDone();
        return first.getVersionSum() + second.getVersionSum();
    }

    @Override
    public int getLength() {
        assertDone();
        return first.getLength() + second.getLength();
    }

    private void assertDone() {
        if (!isDone()) {
            throw new IllegalStateException("not fully processed yet");
        }
    }
}
