public class LengthPackets implements LengthType {
    private int packets;
    private final int id;

    public LengthPackets(int packets, int id) {
        System.out.println("packet " + id + " uses packets length. there are " + packets + " packets");
        this.packets = packets;
        this.id = id;
    }

    @Override
    public boolean hasNextSubPacket() {
        return packets != 0;
    }

    @Override
    public void consume(int subPacketLength) {
        if (packets == 0) {
            throw new IllegalArgumentException("packet " + id + ": out of sub-packets, can't consume any more");
        }
        packets--;
        System.out.println("packet " + id + ": consumed via packets len, packets is now " + packets);
    }
}
