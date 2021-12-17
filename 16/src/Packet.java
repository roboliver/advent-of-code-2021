import java.io.IOException;

public class Packet {
    private static final int TYPE_SUM = 0;
    private static final int TYPE_PRODUCT = 1;
    private static final int TYPE_MINIMUM = 2;
    private static final int TYPE_MAXIMUM = 3;
    private static final int TYPE_LITERAL = 4;
    private static final int TYPE_GREATER_THAN = 5;
    private static final int TYPE_LESS_THAN = 6;
    private static final int TYPE_EQUAL_TO = 7;

    private final int length;
    private final Contents contents;
    private final int version;

    public Packet(BitReader reader) throws IOException {
        this.version = reader.read(3);
        int typeId = reader.read(3);
        int length = 6;
        if (typeId == TYPE_LITERAL) {
            this.contents = parseLiteral(reader);
        } else {
            int lengthTypeId = reader.read(1);
            length += 1;
            LengthType lengthType;
            if (lengthTypeId == 0) {
                lengthType = new LengthBits(reader.read(15));
                length += 15;
            } else {
                lengthType = new LengthPackets(reader.read(11));
                length += 11;
            }
            this.contents = operator(typeId, lengthType);
        }
        this.length = length;
    }

    private ContentsLiteral parseLiteral(BitReader reader) throws IOException {
        long literal = 0;
        int length = 0;
        boolean hasNext = true;
        while (hasNext) {
            int read = reader.read(5);
            hasNext = (read & 0x10) == 0x10;
            int nibble = read & 0xF;
            literal <<= 4;
            literal |= nibble;
            length += 5;
        }
        return new ContentsLiteral(literal, length);
    }

    private static Contents operator(int typeId, LengthType lengthType) {
        switch (typeId) {
            case TYPE_SUM:
                return new ContentsCollection(0, Long::sum, lengthType);
            case TYPE_PRODUCT:
                return new ContentsCollection(1, (a, b) -> a * b, lengthType);
            case TYPE_MINIMUM:
                return new ContentsCollection(Long.MAX_VALUE, Long::min, lengthType);
            case TYPE_MAXIMUM:
                return new ContentsCollection(Long.MIN_VALUE, Long::max, lengthType);
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

    public boolean isFullyProcessed() {
        return contents.isDone();
    }

    public void addSubPacket(Result subPacket) {
        contents.addSubPacket(subPacket);
    }

    public Result result() {
        if (!isFullyProcessed()) {
            throw new IllegalStateException("not fully processed yet");
        }
        return new Result(version + contents.getVersionSum(),
                contents.getValue(), length + contents.getLength());
    }
}
