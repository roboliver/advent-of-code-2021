public interface Contents {
    public boolean isDone();

    public void accept(PacketResult subPacket);

    public long getValue();

    public int getVersionSum();

    public int getLength();
}
