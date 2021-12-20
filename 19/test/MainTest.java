import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    @Test
    public void testBeaconCount() throws IOException {
        assertEquals(79, Main.beaconCount(Utils.inputLineReader("sample.txt")));
    }

}
