/**
 * Packet contents that directly contain a literal value.
 */
public class ContentsLiteral implements Contents {
    private final long literal;
    private final int length;

    public ContentsLiteral(long literal, int length) {
        this.literal = literal;
        this.length = length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFullyProcessed() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSubPacket(Result subPacket) {
        throw new IllegalArgumentException("literal packets can't have subpackets");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getValue() {
        return literal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVersionSum() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return length;
    }
}
