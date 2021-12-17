/**
 * Length type calculated from the number of directly contained sub-packets.
 */
public class LengthPackets implements LengthType {
    private int packets;

    public LengthPackets(int packets) {
        this.packets = packets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNextSubPacket() {
        return packets != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSubPacket(int subPacketLength) {
        if (packets == 0) {
            throw new IllegalArgumentException("out of sub-packets, can't add any more");
        }
        packets--;
    }
}
