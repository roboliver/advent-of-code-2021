import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader reader = Utils.inputLineReader()) {
            //System.out.println("Version sum: " + versionSum(reader));
        }
        try (BufferedReader reader = Utils.inputLineReader()) {
            System.out.println("Value: " + evaluate(reader));
        }
    }

    public static int versionSum(BufferedReader bufReader) throws IOException {
        BitReader reader = new BitReader(bufReader);
        int versionSum = 0;
        while (reader.hasNext()) {
            System.out.println("reading a new packet in the main loop.");
            if (versionSum > 0) {
                System.out.println(bufReader.readLine());
            }
            Packet packet = new Packet(reader);
            versionSum += packet.getVersionSum();
        }
        return versionSum;
    }

    public static long evaluate(BufferedReader bufReader) throws IOException {
        System.out.println("---------------");
        BitReader reader = new BitReader(bufReader);
        Packet packet = new Packet(reader);
        return packet.getValue();
    }
}
