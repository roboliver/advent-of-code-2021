import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE = "16,1,2,0,4,2,7,1,2,14";

    @Test
    public void testFuelRequired() throws IOException {
        assertEquals(37, Main.fuelRequired(Utils.testLineReader(SAMPLE)));
    }

    @Test
    public void testFuelRequiredActual() throws IOException {
        assertEquals(168, Main.fuelRequiredActual(Utils.testLineReader(SAMPLE)));
    }
}
