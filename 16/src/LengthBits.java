public class LengthBits implements LengthType {
    private int bits;
    private final int id;

    public LengthBits(int bits, int id) {
        //System.out.println("packet " + id + " uses bit length. there are " + bits + " bits");
        this.bits = bits;
        this.id = id;
    }

    @Override
    public boolean hasNextSubPacket() {
        return bits != 0;
    }

    @Override
    public void consume(int subPacketLength) {
        //System.out.println("packet " + id + ":  consuming " + subPacketLength + " bits, we currently have " + bits + " left");
        if (bits - subPacketLength < 0) {
            throw new IllegalArgumentException("packet " + id + ": can't consume " + subPacketLength
                    + " bits as there are only " + bits + " bits left");
        }
        bits -= subPacketLength;
        //System.out.println("packet " + id + ": consumed via bits len, bits is now " + bits);
    }
}
