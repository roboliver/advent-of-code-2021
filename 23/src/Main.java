import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        Burrow burrow = burrow(lineReader, inclMissingLines);
    }

    private static Burrow burrow(BufferedReader lineReader, boolean inclMissingLines) throws IOException {
        lineReader.readLine();
        lineReader.readLine();
        List<Amphipod> amphipods = new ArrayList<>();
        amphipods.addAll(amphipods(lineReader.readLine(), inclMissingLines ? 4 : 2));
        if (inclMissingLines) {
            amphipods.addAll(amphipods);
        }
        amphipods.addAll(amphipods(lineReader.readLine(), inclMissingLines ? 4 : 2));
        lineReader.close();
        return new Burrow(amphipods, inclMissingLines ? 4 : 2);
    }

    private static List<Amphipod> amphipods(String line, int depth) {
        List<Amphipod> amphipods = new ArrayList<>();
        amphipods.add(new Amphipod(amphipodType(line.charAt(3)), Room.SIDEROOM_A, depth));
        amphipods.add(new Amphipod(amphipodType(line.charAt(5)), Room.SIDEROOM_B, depth));
        amphipods.add(new Amphipod(amphipodType(line.charAt(7)), Room.SIDEROOM_C, depth));
        amphipods.add(new Amphipod(amphipodType(line.charAt(9)), Room.SIDEROOM_D, depth));
        return amphipods;
    }

    private static Amphipod.Type amphipodType(char c) {
        switch (c) {
            case 'A':
                return Amphipod.Type.A;
            case 'B':
                return Amphipod.Type.B;
            case 'C':
                return Amphipod.Type.C;
            case 'D':
                return Amphipod.Type.D;
            default:
                throw new IllegalArgumentException("not an amphipod type");
        }
    }
}
