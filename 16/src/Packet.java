import java.io.IOException;

/**
 * A data packet. Packets are constructed by providing the bit stream they are read from, and they will read the bits
 * necessary to build themselves. Operator packets will stop reading once they have read their length, and will then
 * wait for sub-packets to be added to them once read.
 *
 * When a packet has been fully read, it can be collapsed into a {@code Result} object, containing the useful
 * information from it, after which point the original, expensive {@code Packet} object can be thrown away.
 */
public class Packet {
    private static final int TYPE_SUM = 0;
    private static final int TYPE_PRODUCT = 1;
    private static final int TYPE_MINIMUM = 2;
    private static final int TYPE_MAXIMUM = 3;
    private static final int TYPE_LITERAL = 4;
    private static final int TYPE_GREATER_THAN = 5;
    private static final int TYPE_LESS_THAN = 6;
    private static final int TYPE_EQUAL_TO = 7;

    private final int version;
    private final int metadataLength;
    private final Contents contents;

    public Packet(BitReader bitReader) throws IOException {
        this.version = bitReader.read(3);
        int typeId = bitReader.read(3);
        int metadataLength = 6;
        if (typeId == TYPE_LITERAL) {
            this.contents = parseLiteral(bitReader);
        } else {
            int lengthTypeId = bitReader.read(1);
            metadataLength += 1;
            LengthType lengthType;
            if (lengthTypeId == 0) {
                lengthType = new LengthBits(bitReader.read(15));
                metadataLength += 15;
            } else {
                lengthType = new LengthPackets(bitReader.read(11));
                metadataLength += 11;
            }
            this.contents = operator(typeId, lengthType);
        }
        this.metadataLength = metadataLength;
    }

    private ContentsLiteral parseLiteral(BitReader bitReader) throws IOException {
        int length = 0;
        long literal = 0;
        boolean hasNext = true;
        while (hasNext) {
            int bits = bitReader.read(5);
            length += 5;
            int nibble = bits & 0xF;
            literal <<= 4;
            literal |= nibble;
            hasNext = (bits & 0x10) == 0x10;
        }
        return new ContentsLiteral(length, literal);
    }

    private static Contents operator(int typeId, LengthType lengthType) {
        switch (typeId) {
            case TYPE_SUM:
                return new ContentsCollection(lengthType, 0, Long::sum);
            case TYPE_PRODUCT:
                return new ContentsCollection(lengthType, 1, (a, b) -> a * b);
            case TYPE_MINIMUM:
                return new ContentsCollection(lengthType, Long.MAX_VALUE, Long::min);
            case TYPE_MAXIMUM:
                return new ContentsCollection(lengthType, Long.MIN_VALUE, Long::max);
            case TYPE_GREATER_THAN:
                return new ContentsPair((a, b) -> a > b);
            case TYPE_LESS_THAN:
                return new ContentsPair((a, b) -> a < b);
            case TYPE_EQUAL_TO:
                return new ContentsPair(Long::equals);
            default:
                throw new IllegalArgumentException("invalid type");
        }

    }

    /**
     * Whether the packet has been fully processed (i.e. its contents are full) and can now be collapsed into a
     * {@code Result} object.
     * @return True if the packet has been fully processed, false otherwise.
     */
    public boolean isFullyProcessed() {
        return contents.isFullyProcessed();
    }

    /**
     * Adds a sub-packet to this packet.
     * @param subPacket The sub-packet to add.
     */
    public void addSubPacket(Result subPacket) {
        contents.addSubPacket(subPacket);
    }

    /**
     * Returns the result of the packet, which contains the useful information from it after it's been fully processed.
     * @return The packet's result.
     */
    public Result result() {
        return new Result(version + contents.getVersionSum(),
                metadataLength + contents.getLength(), contents.getValue());
    }
}
