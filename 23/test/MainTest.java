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

//private static final String SAMPLE =
//        "#############\n"
//        + "#...........#\n"
//        + "###B#A#D#C###\n"
//        + "  #A#B#C#D#\n"
//        + "  #########";

//    private static final String SAMPLE =
//            "#############\n"
//            + "#...........#\n"
//            + "###B#B#C#D###\n"
//            + "  #A#A#C#D#\n"
//            + "  #########";

    //@Test
    public void testIterate() {
        int j = 0;
        for (int i = 0; i < 28183436; i++) {
            for (int k = 0; k < 1000; k++) {
                new String("fjdak");
            }
        }
    }

    @Test
    public void testOrganiseAmphipodsMinEnergy() throws IOException {
        assertEquals(12521, Main.organiseAmphipodsMinEnergy2(Utils.testLineReader(SAMPLE), false));
        System.out.println("states: " + Main.states);
    }

    //@Test
    public void testOrganiseAmphipodsMinEnergyActual() throws IOException {
        assertEquals(44169, Main.organiseAmphipodsMinEnergy(Utils.testLineReader(SAMPLE), true));
    }

    @Test
    public void testEqualBurrows() throws IOException {
        Burrow burrow1 = Main.burrow(Utils.testLineReader(SAMPLE), false);
        Burrow burrow2 = Main.burrow(Utils.testLineReader(SAMPLE), false);
        assertEquals(burrow1, burrow2);
    }
}
