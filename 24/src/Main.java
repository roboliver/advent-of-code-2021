import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Largest accepted model number: " + maxModelNumber(lineReader));
        }
    }

    public static long maxModelNumber(BufferedReader lineReader) throws IOException {
        List<Instruction> instructions = instructions(lineReader);
        long minZ = Long.MAX_VALUE;
        long minZInput;
        for (long input = 71131111111111L; input <= 71131351919891L; input++) {
            if (!isValidModelNumber(input)) {
                continue;
            }
            State state = new State(input, 14);
            int i = 1;
            for (Instruction instruction : instructions) {
                state = instruction.execute(state);
                i++;
            }
            if (state.getVar('z') == 0) {
                return input;
            } else if (state.getVar('z') < minZ) {
                minZ = state.getVar('z');
                minZInput = input;
                System.out.println("new lowest z: " + minZ + " at input " + minZInput);
            }
        }
        throw new IllegalStateException("no model numbers were valid!");
    }

    public static State executeInstructions(BufferedReader lineReader, long input, int inputLen) throws IOException {
        List<Instruction> instructions = instructions(lineReader);
        State state = new State(input, inputLen);
        System.out.println(state);
        for (Instruction instruction : instructions) {
            state = instruction.execute(state);
            System.out.println(state);
        }
        return state;
    }

    private static boolean isValidModelNumber(long input) {
        for (int i = 0; i < 14; i++) {
            if (input % 10 == 0) {
                return false;
            }
            input /= 10;
        }
        return true;
    }

    private static List<Instruction> instructions(BufferedReader lineReader) throws IOException {
        List<Instruction> instructions = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            instructions.add(instruction(line));
        }
        lineReader.close();
        return instructions;
    }

    private static Instruction instruction(String line) {
        String[] parts = line.split(" ");
        String instruction = parts[0];
        if (instruction.equals("inp")) {
            return new Inp(parts[1].charAt(0));
        } else {
            char a = parts[1].charAt(0);
            String b = parts[2];
            switch (instruction) {
                case "add":
                    return isVar(b) ? new Add(a, bVar(b)) : new Add(a, bNum(b));
                case "mul":
                    return isVar(b) ? new Mul(a, bVar(b)) : new Mul(a, bNum(b));
                case "div":
                    return isVar(b) ? new Div(a, bVar(b)) : new Div(a, bNum(b));
                case "mod":
                    return isVar(b) ? new Mod(a, bVar(b)) : new Mod(a, bNum(b));
                case "eql":
                    return isVar(b) ? new Eql(a, bVar(b)) : new Eql(a, bNum(b));
                default:
                    throw new IllegalArgumentException("not a valid instruction: " + instruction);
            }
        }
    }

    private static boolean isVar(String b) {
        return Character.isAlphabetic(b.charAt(0));
    }

    private static char bVar(String b) {
        return b.charAt(0);
    }

    private static long bNum(String b) {
        return Long.parseLong(b);
    }
}
