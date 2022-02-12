public class Div extends BiInstruction {

    public Div(Character a, Character b) {
        super(a, b, (aVal, bVal) -> aVal / bVal);
    }

    public Div(Character a, Long b) {
        super(a, b, (aVal, bVal) -> aVal / bVal);
    }

    @Override
    protected String instr() {
        return "div";
    }
}
