/**
 * The contents of a packet. Implemented as necessary to support logic for storing contents of each packet type.
 */
public interface Contents {
    /**
     * Whether the contents contain everything they need to.
     * @return True if the contents are now fully processed, false otherwise.
     */
    public boolean isFullyProcessed();

    /**
     * Adds a sub-packet to the contents. Should throw an exception if the contents can't accept any more packets (i.e.
     * if {@code isFullyProcessed} return true).
     * @param subPacket The sub-packet to add.
     */
    public void addSubPacket(Result subPacket);

    /**
     * Returns the value of the packet contents, which will depend on its type.
     * @return The contents' value.
     */
    public long getValue();

    /**
     * Returns the sum of the versions of all contained sub-packets.
     * @return The version sum.
     */
    public int getVersionSum();

    /**
     * Returns the length of the contents, in bits.
     * @return The contents' length.
     */
    public int getLength();
}
