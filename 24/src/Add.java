public class Add extends BiInstruction {

    public Add(Character a, Character b) {
        super(a, b, Long::sum);
    }

    public Add(Character a, Long b) {
        super(a, b, Long::sum);
    }

    @Override
    protected String instr() {
        return "add";
    }
}
