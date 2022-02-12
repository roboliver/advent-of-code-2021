public class Inp implements Instruction {
    private final char a;

    public Inp (char a) {
        this.a = a;
    }

    @Override
    public State execute(State state) {
        Long[] varsReplace = new Long[4];
        varsReplace[State.varIndex(a)] = state.nextInput();
        return state.next(true, varsReplace);
    }

    @Override
    public String toString() {
        return "inp " + a;
    }
}
