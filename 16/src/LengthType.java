/**
 * A length type, determining how the length of the sub-packets within a packet is calculated.
 */
public interface LengthType {

    /**
     * Whether the packet has any further sub-packets.
     * @return True if there are more sub-packets, false otherwise.
     */
    boolean hasNextSubPacket();

    /**
     * Adds a sub-packet, updating this length tracker accordingly.
     * @param subPacketLength The length of the added sub-packet, in bits.
     */
    void addSubPacket(int subPacketLength);
}
