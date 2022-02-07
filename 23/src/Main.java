import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Minimum energy to organise amphipods: " + organiseAmphipodsMinEnergy2(lineReader, false));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            //System.out.println("Minimum energy to organise amphipods (actual): " + organiseAmphipodsMinEnergy(lineReader, true));
        }
    }

    public static int organiseAmphipodsMinEnergy2(BufferedReader lineReader, boolean inclMissingLines) throws IOException {
        Burrow burrowInit = burrow(lineReader, inclMissingLines);
        Map<Burrow, Integer> burrowStates = new HashMap<>();
        burrowStates.put(burrowInit, 0);
        do {
            System.out.println("current state count: " + burrowStates.size());
            Map<Burrow, Integer> burrowStatesNew = new HashMap<>();
            for (Map.Entry<Burrow, Integer> burrowState : burrowStates.entrySet()) {
                Burrow burrow = burrowState.getKey();
                int energy = burrowState.getValue();
                if (burrow.isSolved()) {
                    burrowStatesNew.merge(burrow, energy, Integer::min);
                } else {
                    for (Amphipod amphipod : burrow.amphipods()) {
                        for (Map.Entry<Room, Integer> validMove : burrow.validMoves(amphipod)) {
                            Room room = validMove.getKey();
                            int space = validMove.getValue();
                            int moveEnergy = burrow.stepCostTo(amphipod, room, space);
                            Burrow burrowNew = burrow.moveAmphipod(amphipod, room, space);
                            burrowStatesNew.merge(burrowNew, energy + moveEnergy, Integer::min);
                        }
                    }
                }
            }
            burrowStates = burrowStatesNew;
        } while (burrowStates.size() > 1);
        return burrowStates.values().stream().findFirst().get();
    }

    public static int organiseAmphipodsMinEnergy(BufferedReader lineReader, boolean inclMissingLines) throws IOException {
        Burrow burrow = burrow(lineReader, inclMissingLines);
        return organiseAmphipodsMinEnergy(burrow, 0);
    }

    public static int states = 0;
    private static int organiseAmphipodsMinEnergy(Burrow burrow, int energySoFar) {
        states++;
//        System.out.println(burrow.toString());
//        System.out.println();
        if (burrow.isSolved()) {
            System.out.println("Solved it! states so far: " + states);
//            System.out.println("solved!");
//            System.out.println(burrow.toString());
//            System.out.println();
            return energySoFar;
        }
        int minEnergy = Integer.MAX_VALUE;
        for (Amphipod amphipod : burrow.amphipods()) {
            for (Map.Entry<Room, Integer> validMove : burrow.validMoves(amphipod)) {
                Room room = validMove.getKey();
                int space = validMove.getValue();
                int moveEnergy = burrow.stepCostTo(amphipod, room, space);
                Burrow burrowNew = burrow.moveAmphipod(amphipod, room, space);
                minEnergy = Math.min(minEnergy,
                        organiseAmphipodsMinEnergy(burrowNew, energySoFar + moveEnergy));
            }
//            if (burrow.validMoves(amphipod).isEmpty()) {
//                System.out.println("no more valid moves! states so far: " + states);
//            }
        }
        return minEnergy;
    }

    public static Burrow burrow(BufferedReader lineReader, boolean inclMissingLines) throws IOException {
        lineReader.readLine();
        lineReader.readLine();
        List<Amphipod> amphipods = new ArrayList<>();
        amphipods.addAll(amphipods(lineReader.readLine(), 0));
        if (inclMissingLines) {
            amphipods.addAll(missingAmphipods());
        }
        amphipods.addAll(amphipods(lineReader.readLine(), inclMissingLines ? 3 : 1));
        lineReader.close();
        return new Burrow(amphipods);
    }

    private static List<Amphipod> amphipods(String line, int depth) {
        List<Amphipod> amphipods = new ArrayList<>();
        amphipods.add(new Amphipod(amphipodType(line.charAt(3)), Room.SIDEROOM_A, depth));
        amphipods.add(new Amphipod(amphipodType(line.charAt(5)), Room.SIDEROOM_B, depth));
        amphipods.add(new Amphipod(amphipodType(line.charAt(7)), Room.SIDEROOM_C, depth));
        amphipods.add(new Amphipod(amphipodType(line.charAt(9)), Room.SIDEROOM_D, depth));
        return amphipods;
    }

    private static List<Amphipod> missingAmphipods() {
        return List.of(
                new Amphipod(Amphipod.Type.D, Room.SIDEROOM_A, 1),
                new Amphipod(Amphipod.Type.D, Room.SIDEROOM_A, 2),
                new Amphipod(Amphipod.Type.C, Room.SIDEROOM_B, 1),
                new Amphipod(Amphipod.Type.B, Room.SIDEROOM_B, 2),
                new Amphipod(Amphipod.Type.B, Room.SIDEROOM_C, 1),
                new Amphipod(Amphipod.Type.A, Room.SIDEROOM_C, 2),
                new Amphipod(Amphipod.Type.A, Room.SIDEROOM_D, 1),
                new Amphipod(Amphipod.Type.C, Room.SIDEROOM_D, 2)
        );
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
