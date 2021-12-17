import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = Utils.inputLineReader()) {
            System.out.println("Version sum: " + versionSum(reader));
        }
    }

    public static int versionSum(BufferedReader bufReader) throws IOException {
        BitReader reader = new BitReader(bufReader);
        int versionSum = 0;
        while (reader.hasNext()) {
            System.out.println("reading a new packet in the main loop.");
            Packet packet = new Packet(reader);
            versionSum += packet.getVersionSum();
        }
        return versionSum;
    }
}
