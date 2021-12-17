/**
 * Length type calculated from the length in bits of the contained sub-packets.
 */
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
    public void addSubPacket(int subPacketLength) {
        if (bits - subPacketLength < 0) {
            throw new IllegalArgumentException("can't add a sub-packet containing " + subPacketLength
                    + " bits as there are only " + bits + " bits left in this packet");
        }
        bits -= subPacketLength;
    }
}
