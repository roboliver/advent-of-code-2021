public class Eql extends BiInstruction {

    public Eql(Character a, Character b) {
        super(a, b, (aVal, bVal) -> aVal.longValue() == bVal.longValue() ? 1L : 0L);
    }

    public Eql(Character a, Long b) {
        super(a, b, (aVal, bVal) -> aVal.longValue() == bVal.longValue() ? 1L : 0L);
    }

    @Override
    protected String instr() {
        return "eql";
    }
}
