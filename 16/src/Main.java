import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = Utils.inputLineReader()) {
            System.out.println("Version sum: " + versionSum(reader));
        }
        try (BufferedReader reader = Utils.inputLineReader()) {
            System.out.println("Value with stack: " + evaluate(reader));
        }
    }

    public static long evaluate(BufferedReader bufReader) throws IOException {
        BitReader reader = new BitReader(bufReader);
        ArrayDeque<Packet> stack = new ArrayDeque<>();
        do {
            Packet packet = new Packet(reader);
            while (packet.isDone()) {
                stack.peek().accept(packet.result());
                if (stack.peek().isDone()) {
                    packet = stack.pop();
                    if (stack.isEmpty()) {
                        return packet.result().getValue();
                    }
                } else {
                    break;
                }
            }
            if (!packet.isDone()) {
                stack.push(packet);
            }
        } while (reader.hasNext());
        throw new IllegalStateException("input stream didn't represent a complete packet");
    }

    public static long versionSum(BufferedReader bufReader) throws IOException {
        System.out.println("--------------");
        BitReader reader = new BitReader(bufReader);
        ArrayDeque<Packet> stack = new ArrayDeque<>();
        stack.push(new Packet(reader));
        PacketResult result = null;
        while (reader.hasNext()) {
            if (stack.isEmpty()) {
            }
            Packet packet = new Packet(reader);
            while (packet.isDone()) {
                stack.peek().accept(packet.result());
                if (stack.peek().isDone()) {
                    packet = stack.pop();
                    if (stack.isEmpty()) {
                        return packet.result().getVersionSum();
                    }
                } else {
                    break;
                }
            }
            if (!packet.isDone()) {
                stack.push(packet);
            }
        }
        return result.getValue();
    }
}
