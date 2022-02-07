import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "#############\n"
            + "#...........#\n"
            + "###B#C#B#D###\n"
            + "  #A#D#C#A#\n"
            + "  #########";

    @Test
    public void testOrganiseAmphipodsMinEnergy() throws IOException {
        assertEquals(12521, Main.organiseAmphipodsMinEnergy(Utils.testLineReader(SAMPLE), false));
    }

    @Test
    public void testOrganiseAmphipodsMinEnergyActual() throws IOException {
        assertEquals(44169, Main.organiseAmphipodsMinEnergy(Utils.testLineReader(SAMPLE), true));
    }
}
