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

    public static int versionSum(BufferedReader reader) throws IOException {
        return parse(reader).getVersionSum();
    }

    public static long evaluate(BufferedReader reader) throws IOException {
        return parse(reader).getValue();
    }

    private static Result parse(BufferedReader reader) throws IOException {
        BitReader bitReader = new BitReader(reader);
        ArrayDeque<Packet> stack = new ArrayDeque<>();
        while (true) {
            Packet packetCur = new Packet(bitReader);
            while (packetCur.isFullyProcessed() && !stack.isEmpty()) {
                Packet packetPrev = stack.pop();
                packetPrev.addSubPacket(packetCur.result());
                packetCur = packetPrev;
            }
            if (packetCur.isFullyProcessed()) {
                return packetCur.result();
            } else {
                stack.push(packetCur);
            }
        }
    }
}
