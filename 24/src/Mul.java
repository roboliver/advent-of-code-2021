public class Mul extends BiInstruction {

    public Mul(Character a, Character b) {
        super(a, b, (aMul, bMul) -> aMul * bMul);
    }

    public Mul(Character a, Long b) {
        super(a, b, (aMul, bMul) -> aMul * bMul);
    }

    @Override
    protected String instr() {
        return "mul";
    }
}
