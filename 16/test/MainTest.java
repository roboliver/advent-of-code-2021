import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE_16 = "8A004A801A8002F478";
    private static final String SAMPLE_12 = "620080001611562C8802118E34";
    private static final String SAMPLE_23 = "C0015000016115A2E0802F182340";
    private static final String SAMPLE_31 = "A0016C880162017C3686B18A3D4780";

    @Test
    public void testVersionSum() throws IOException {
        assertEquals(16, Main.versionSum(Utils.testLineReader(SAMPLE_16)));
        assertEquals(12, Main.versionSum(Utils.testLineReader(SAMPLE_12)));
        assertEquals(23, Main.versionSum(Utils.testLineReader(SAMPLE_23)));
        assertEquals(31, Main.versionSum(Utils.testLineReader(SAMPLE_31)));
    }
}
