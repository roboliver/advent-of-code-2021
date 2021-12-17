import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE_VERSION_16 = "8A004A801A8002F478";
    private static final String SAMPLE_VERSION_12 = "620080001611562C8802118E34";
    private static final String SAMPLE_VERSION_23 = "C0015000016115A2E0802F182340";
    private static final String SAMPLE_VERSION_31 = "A0016C880162017C3686B18A3D4780";

    private static final String SAMPLE_SUM = "C200B40A82";
    private static final String SAMPLE_PRODUCT = "04005AC33890";
    private static final String SAMPLE_MINIMUM = "880086C3E88112";
    private static final String SAMPLE_MAXIMUM = "CE00C43D881120";
    private static final String SAMPLE_LESS_THAN = "D8005AC2A8F0";
    private static final String SAMPLE_NOT_GREATER_THAN = "F600BC2D8F";
    private static final String SAMPLE_EQUAL_TO = "9C0141080250320F1802104A08";

    @Test
    public void testVersionSum() throws IOException {
        assertEquals(16, Main.versionSum(Utils.testLineReader(SAMPLE_VERSION_16)));
        assertEquals(12, Main.versionSum(Utils.testLineReader(SAMPLE_VERSION_12)));
        assertEquals(23, Main.versionSum(Utils.testLineReader(SAMPLE_VERSION_23)));
        assertEquals(31, Main.versionSum(Utils.testLineReader(SAMPLE_VERSION_31)));
    }

    @Test
    public void testEvaluate() throws IOException {
        assertEquals(3, Main.evaluate(Utils.testLineReader(SAMPLE_SUM)));
        assertEquals(54, Main.evaluate(Utils.testLineReader(SAMPLE_PRODUCT)));
        assertEquals(7, Main.evaluate(Utils.testLineReader(SAMPLE_MINIMUM)));
        assertEquals(9, Main.evaluate(Utils.testLineReader(SAMPLE_MAXIMUM)));
        assertEquals(1, Main.evaluate(Utils.testLineReader(SAMPLE_LESS_THAN)));
        assertEquals(0, Main.evaluate(Utils.testLineReader(SAMPLE_NOT_GREATER_THAN)));
        assertEquals(1, Main.evaluate(Utils.testLineReader(SAMPLE_EQUAL_TO)));
    }
}
