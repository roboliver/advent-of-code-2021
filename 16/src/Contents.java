public interface Contents {
    public boolean isDone();

    public void addSubPacket(Result subPacket);

    public long getValue();

    public int getVersionSum();

    public int getLength();
}
