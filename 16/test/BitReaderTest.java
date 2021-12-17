import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BitReaderTest {
    @Test
    public void testRead() throws IOException {
        BitReader reader = new BitReader(Utils.testLineReader("A9"));
        assertTrue(reader.hasNext());
        assertEquals(1, reader.read(1));
        assertEquals(2, reader.read(3));
        assertEquals(9, reader.read(4));
        assertFalse(reader.hasNext());
    }
}
