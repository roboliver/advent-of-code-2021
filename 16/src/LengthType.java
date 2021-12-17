public interface LengthType {

    public boolean hasNextSubPacket();

    public void consume(int subPacketLength);
}
