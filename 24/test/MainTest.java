import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE_NEGATE =
            "inp x\n"
            + "mul x -1";

    private static final String SAMPLE_IS_SECOND_TRIPLE =
            "inp z\n"
            + "inp x\n"
            + "mul z 3\n"
            + "eql z x";

    private static final String SAMPLE_TO_BINARY =
            "inp w\n"
            + "add z w\n"
            + "mod z 2\n"
            + "div w 2\n"
            + "add y w\n"
            + "mod y 2\n"
            + "div w 2\n"
            + "add x w\n"
            + "mod x 2\n"
            + "div w 2\n"
            + "mod w 2";

    @Test
    public void testNegate() throws IOException {
        assertEquals(-5, Main.executeInstructions(Utils.testLineReader(SAMPLE_NEGATE), 5L, 1).getVar('x'));
    }

    @Test
    public void testIsSecondTriple() throws IOException {
        assertEquals(1, Main.executeInstructions(Utils.testLineReader(SAMPLE_IS_SECOND_TRIPLE), 13L, 2).getVar('z'));
        assertEquals(0, Main.executeInstructions(Utils.testLineReader(SAMPLE_IS_SECOND_TRIPLE), 31L, 2).getVar('z'));
    }

    @Test
    public void testToBinary() throws IOException {
        State toBinary = Main.executeInstructions(Utils.testLineReader(SAMPLE_TO_BINARY), 9000L, 4);
        assertEquals(1, toBinary.getVar('z'));
        assertEquals(0, toBinary.getVar('y'));
        assertEquals(0, toBinary.getVar('x'));
        assertEquals(1, toBinary.getVar('w'));
    }
}
