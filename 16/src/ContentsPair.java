import java.util.function.BiPredicate;

/**
 * Packet contents that contain exactly two sub-packets, where the result value comes from performing some predicate
 * operation on the pair of them.
 */
public class ContentsPair implements Contents {
    private Result first = null;
    private Result second = null;
    private final BiPredicate<Long, Long> predicate;

    public ContentsPair(BiPredicate<Long, Long> predicate) {
        this.predicate = predicate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFullyProcessed() {
        return first != null && second != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSubPacket(Result subPacket) {
        if (isFullyProcessed()) {
            throw new IllegalStateException("already have both subpackets");
        }
        if (first == null) {
            first = subPacket;
        } else {
            second = subPacket;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVersionSum() {
        assertFullyProcessed();
        return first.getVersionSum() + second.getVersionSum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        assertFullyProcessed();
        return first.getLength() + second.getLength();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getValue() {
        assertFullyProcessed();
        return predicate.test(first.getValue(), second.getValue()) ? 1 : 0;
    }

    private void assertFullyProcessed() {
        if (!isFullyProcessed()) {
            throw new IllegalStateException("not fully processed yet");
        }
    }
}
