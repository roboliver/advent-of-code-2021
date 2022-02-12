import java.util.function.BinaryOperator;

public abstract class BiInstruction implements Instruction {
    private final char a;
    private final char bVar;
    private final long bNum;
    private final boolean bIsVarNotNum;
    private final BinaryOperator<Long> operator;

    public BiInstruction(Character a, Character b, BinaryOperator<Long> operator) {
        this.a = a;
        this.bVar = b;
        this.bNum = 0;
        this.bIsVarNotNum = true;
        this.operator = operator;
    }

    public BiInstruction(Character a, Long b, BinaryOperator<Long> operator) {
        this.a = a;
        this.bNum = b;
        this.bVar = '\u0000';
        this.bIsVarNotNum = false;
        this.operator = operator;
    }

    @Override
    public State execute(State state) {
        Long[] varsReplace = new Long[4];
        varsReplace[State.varIndex(a)] = operator.apply(state.getVar(a), b(state));
        return state.next(varsReplace);
    }

    private long b(State state) {
        if (bIsVarNotNum) {
            return state.getVar(bVar);
        } else {
            return bNum;
        }
    }

    @Override
    public String toString() {
        return instr() + " " + a + " " + (bIsVarNotNum ? Character.toString(bVar) : bNum);
    }

    protected abstract String instr();
}
