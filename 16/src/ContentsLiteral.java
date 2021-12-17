/**
 * Packet contents that directly contain a literal value.
 */
public class ContentsLiteral implements Contents {
    private final int length;
    private final long literal;

    public ContentsLiteral(int length, long literal) {
        this.length = length;
        this.literal = literal;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public long getValue() {
        return literal;
    }
}
