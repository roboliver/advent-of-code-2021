public class LengthBits implements LengthType {
    private int bits;

    public LengthBits(int bits) {
        this.bits = bits;
    }

    @Override
    public boolean hasNextSubPacket() {
        return bits != 0;
    }

    @Override
    public void consume(int subPacketLength) {
        if (bits - subPacketLength < 0) {
            throw new IllegalArgumentException("can't consume " + subPacketLength
                    + " bits as there are only " + bits + " bits left");
        }
        bits -= subPacketLength;
    }
}
