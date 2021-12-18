import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

/**
 * Wraps a Reader containing a stream of hex characters and enables them to be read as bits.
 */
public class BitReader {
    private final Reader reader;
    private int hex = -1;
    private int bitsLeftInHex = 0;

    public BitReader(Reader reader) {
        this.reader = Objects.requireNonNull(reader);
    }

    public boolean hasNext() throws IOException {
        ensureHex();
        return hex != -1;
    }

    /**
     * Reads the specified number of bits.
     * @param bitCount The number of bits to read
     * @return The bits, in the form of an integer
     * @throws IOException If the end of the stream had already been reached prior to this read
     */
    public int read(int bitCount) throws IOException {
        if (bitCount < 1 || bitCount > 16) {
            System.out.println("bits read must fit into an integer, i.e. must be between 1 and 16");
        }
        if (!hasNext()) {
            throw new IOException("reached end of bit stream");
        }
        int bits = 0;
        for (int i = 0; i < bitCount && hasNext(); i++) {
            bits <<= 1;
            bits |= readBit();
        }
        return bits;
    }

    private int readBit() throws IOException {
        ensureHex();
        // the bit we read is always the fourth from the right, so shunt it to the 1 position after getting it
        int bit = (hex & 0x08) >> 3;
        hex <<= 1;
        bitsLeftInHex--;
        return bit;
    }

    private void ensureHex() throws IOException {
        if (bitsLeftInHex == 0) {
            char hexChar = (char) reader.read();
            String hexStr = String.valueOf(hexChar);
            try {
                hex = Integer.parseInt(hexStr, 16);
            } catch (NumberFormatException e) {
                // EOF, or an invalid character, which we treat as EOF
                hex = -1;
            }
            bitsLeftInHex = 4;
        }
    }
}
