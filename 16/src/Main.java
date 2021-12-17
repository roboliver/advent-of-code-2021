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

    private static Result parse(BufferedReader bufReader) throws IOException {
        BitReader reader = new BitReader(bufReader);
        ArrayDeque<Packet> stack = new ArrayDeque<>();
        while (true) {
            Packet packetCur = new Packet(reader);
            while (packetCur.isFullyProcessed()) {
                Packet packetPrev = stack.pop();
                packetPrev.addSubPacket(packetCur.result());
                packetCur = packetPrev;
                if (stack.isEmpty() && packetCur.isFullyProcessed()) {
                    return packetCur.result();
                }
            }
            stack.push(packetCur);
        }
    }
}
