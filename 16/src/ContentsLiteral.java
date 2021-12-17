public class ContentsLiteral implements Contents {
    private final long literal;
    private final int length;

    public ContentsLiteral(long literal, int length) {
        this.literal = literal;
        this.length = length;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public void accept(PacketResult subPacket) {
        throw new IllegalArgumentException("literal packets can't have subpackets");
    }

    @Override
    public long getValue() {
        return literal;
    }

    @Override
    public int getVersionSum() {
        return 0;
    }

    @Override
    public int getLength() {
        return length;
    }
}
