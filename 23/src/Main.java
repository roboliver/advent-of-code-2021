import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Minimum energy to organise amphipods: " + organiseAmphipodsMinEnergy(lineReader, false));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Minimum energy to organise amphipods (actual): " + organiseAmphipodsMinEnergy(lineReader, true));
        }
    }

    public static int organiseAmphipodsMinEnergy(BufferedReader lineReader, boolean inclMissingLines) throws IOException {
        return 0;
    }
}
