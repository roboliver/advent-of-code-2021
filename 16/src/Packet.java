import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Packet {
    private static int id = 0;

    private enum Type {
        SUM(0, packet -> {
            long sum = packet.subPackets.stream()
                    .map(Packet::getValue)
                    .reduce(0L, Long::sum);
            System.out.println("did a sum, got: " + sum);
            return sum;
        }),
        PRODUCT(1, packet -> {
            long product = packet.subPackets.stream()
                    .map(Packet::getValue)
                    .reduce(1L, (a, b) -> a * b);
            System.out.println("did a product, got: " + product);
            return product;
        }),
        MINIMUM(2, packet -> {
            long minimum = packet.subPackets.stream()
                    .map(Packet::getValue)
                    .reduce(Long.MAX_VALUE, Long::min);
            System.out.println("got minimum as " + minimum);
            return minimum;
        }),
        MAXIMUM(3, packet -> {
            long maximum = packet.subPackets.stream()
                    .map(Packet::getValue)
                    .reduce(Long.MIN_VALUE, Long::max);
            System.out.println("got maximum as " + maximum);
            return maximum;
        }),
        LITERAL(4, packet -> {
            System.out.println("it's a literal: " + packet.literal);
            return packet.literal;
        }),
        GREATER_THAN(5, packet -> {
            long gt = packet.subPackets.get(0).getValue() > packet.subPackets.get(1).getValue() ? 1 : 0;
            System.out.println("did greater than, got " + gt);
            return gt;
        }),
        LESS_THAN(6, packet -> {
            long lt = packet.subPackets.get(0).getValue() < packet.subPackets.get(1).getValue() ? 1 : 0;
            System.out.println("did less than, got " + lt);
            return lt;
        }),
        EQUAL_TO(7, packet -> {
            long eq = packet.subPackets.get(0).getValue() == packet.subPackets.get(1).getValue() ? 1 : 0;
            System.out.println("did equal to, got " + eq);
            return eq;
        });

        private final int id;
        private final Function<Packet, Long> operator;

        private Type(int id, Function<Packet, Long> operator) {
            this.id = id;
            this.operator = operator;
        }

        private int id() {
            return id;
        }

        private long operate(Packet packet) {
            return operator.apply(packet);
        }

        private static Type parseType(int id) {
            for (Type type : values()) {
                if (type.id == id) {
                    return type;
                }
            }
            throw new IllegalArgumentException("invalid id");
        }
    }

    private final int length;
    private final int version;
    private final Type type;
    private final long literal;
    private final LengthType lengthType;
    private final List<Packet> subPackets;

    public Packet(BitReader reader) throws IOException {
        int id = Packet.id++;
        //System.out.println("made a packet with id " + id);
        this.version = reader.read(3);
        this.type = Type.parseType(reader.read(3));
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
                //System.out.println("packet " + id + ": adding a child packet " + (Packet.id));
                Packet subPacket = new Packet(reader);
                subPackets.add(subPacket);
                length += subPacket.length;
                lengthType.consume(subPacket.length);
            }
            this.subPackets = Collections.unmodifiableList(subPackets);
            literal = -1;
        }
        this.length = length;
        //System.out.println("completed packet " + id + ". length: " + length + ", literal: " + literal + ", subPackets: " + subPackets.size());
    }

    private static long parseLiteral(BitReader reader, int[] literalLen) throws IOException {
        long literal = 0;
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

    public long getValue() {
        return type.operate(this);
    }

    public int getVersionSum() {
        int versionSum = this.version;
        for (Packet packet : subPackets) {
            versionSum += packet.getVersionSum();
        }
        return versionSum;
    }
}
