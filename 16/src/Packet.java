import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Packet {
    private static int id = 0;

    private enum Type {
        LITERAL,
        OPERATOR
    }
    private final int length;
    private final int version;
    private final Type type;
    private final int literal;
    private final LengthType lengthType;
    private final List<Packet> subPackets;

    public Packet(BitReader reader) throws IOException {
        int id = Packet.id++;
        System.out.println("made a packet with id " + id);
        this.version = reader.read(3);
        this.type = parseType(reader.read(3));
        int length = 6;
        if (this.type == Type.LITERAL) {
            int[] literalLen = new int[1];
            this.literal = parseLiteral(reader, literalLen);
            length += literalLen[0];
            this.lengthType = null;
            this.subPackets = Collections.emptyList();
        } else {
            int lengthTypeId = reader.read(1);
            length += 1;
            if (lengthTypeId == 0) {
                lengthType = new LengthBits(reader.read(15), id);
                length += 15;
            } else {
                lengthType = new LengthPackets(reader.read(11), id);
                length += 11;
            }
            List<Packet> subPackets = new ArrayList<>();
            while (lengthType.hasNextSubPacket()) {
                System.out.println("packet " + id + ": adding a child packet " + (Packet.id));
                Packet subPacket = new Packet(reader);
                subPackets.add(subPacket);
                length += subPacket.length;
                lengthType.consume(subPacket.length);
            }
            this.subPackets = Collections.unmodifiableList(subPackets);
            literal = -1;
        }
        this.length = length;
        System.out.println("completed packet " + id + ". length: " + length + ", literal: " + literal + ", subPackets: " + subPackets.size());
    }

    private static Type parseType(int typeId) {
        switch (typeId) {
            case 4:
                return Type.LITERAL;
            default:
                return Type.OPERATOR;
        }
    }

    private static int parseLiteral(BitReader reader, int[] literalLen) throws IOException {
        int literal = 0;
        boolean hasNext = true;
        while (hasNext) {
            int read = reader.read(5);
            hasNext = (read & 0x10) == 0x10;
            int nibble = read & 0xF;
            literal <<= 4;
            literal |= nibble;
            literalLen[0] += 5;
        }
        return literal;
    }

    public int getVersionSum() {
        int versionSum = this.version;
        for (Packet packet : subPackets) {
            versionSum += packet.getVersionSum();
        }
        return versionSum;
    }
}
