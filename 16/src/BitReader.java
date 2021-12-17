import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

public class BitReader {
    private final BufferedReader reader;
    private int bitsLeft = 0;
    private int hex = -1;
    private final int bitmask = 8;

    public BitReader(BufferedReader reader) {
        this.reader = Objects.requireNonNull(reader);
    }

    public boolean hasNext() throws IOException {
        ensureHex();
        return hex != -1;
    }

    public int read(int bitCount) throws IOException {
        int bits = 0;
        for (int i = 0; i < bitCount && hasNext(); i++) {
            bits <<= 1;
            bits |= readBit();
        }
        return bits;
    }

    private int readBit() throws IOException {
        ensureHex();
        int bit = (hex & bitmask) >> 3;
        hex <<= 1;
        bitsLeft--;
        return bit;
    }

    private void ensureHex() throws IOException {
        if (bitsLeft == 0) {
            int nextInt = reader.read();
            String nextStr = String.valueOf((char) nextInt);
            try {
                hex = Integer.parseInt(nextStr, 16);
            } catch (NumberFormatException e) {
                hex = -1;
            }
            bitsLeft = 4;
        }
    }
}
