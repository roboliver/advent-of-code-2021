import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "on x=10..12,y=10..12,z=10..12\n"
            + "on x=11..13,y=11..13,z=11..13\n"
            + "off x=9..11,y=9..11,z=9..11\n"
            + "on x=10..10,y=10..10,z=10..10";

    @Test
    public void testCubesOnIn50Region() throws IOException {
        assertEquals(590784, Main.cubesOn(Utils.testLineReader(SAMPLE), -50, 50));
    }
}
