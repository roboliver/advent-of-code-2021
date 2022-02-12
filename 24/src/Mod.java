public class Mod extends BiInstruction {

    public Mod(Character a, Character b) {
        super(a, b, (aVal, bVal) -> aVal % bVal);
    }

    public Mod(Character a, Long b) {
        super(a, b, (aVal, bVal) -> aVal % bVal);
    }

    @Override
    protected String instr() {
        return "mod";
    }
}
