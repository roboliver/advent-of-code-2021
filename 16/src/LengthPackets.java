public class LengthPackets implements LengthType {
    private int packets;

    public LengthPackets(int packets) {
        this.packets = packets;
    }

    @Override
    public boolean hasNextSubPacket() {
        return packets != 0;
    }

    @Override
    public void addSubPacket(int subPacketLength) {
        if (packets == 0) {
            throw new IllegalArgumentException("out of sub-packets, can't consume any more");
        }
        packets--;
    }
}
