import java.util.Arrays;

public class State {
    private final long input;
    private final int inputLen;
    private final long[] vars;

    public State(long input, int inputLen) {
        if (inputLen < 1 || inputLen > 18) {
            throw new IllegalArgumentException("input length must be between 1 and 18");
        }
        this.input = reverse(input, inputLen);
        this.inputLen = inputLen;
        this.vars = new long[4];
    }

    private State(long input, int inputLen, long[] vars) {
        this.input = input;
        this.inputLen = inputLen;
        this.vars = Arrays.copyOf(vars, vars.length);
    }

    private static long reverse(long input, int inputLen) {
        long reversed = 0;
        for (int i = 0; i < inputLen; i++) {
            reversed *= 10;
            reversed += input % 10;
            input /= 10;
        }
        return reversed;
    }

    public long nextInput() {
        return input % 10;
    }

    public State next(boolean readInput, Long[] varsReplace) {
        long inputNew;
        int inputLenNew;
        if (readInput) {
            if (inputLen == 0) {
                throw new IllegalStateException("input already fully read");
            }
            inputNew = input / 10;
            inputLenNew = inputLen - 1;
        } else {
            inputNew = input;
            inputLenNew = inputLen;
        }
        long[] varsNew = new long[4];
        for (int i = 0; i < varsReplace.length; i++) {
            varsNew[i] = varsReplace[i] == null ? vars[i] : varsReplace[i];
        }
        return new State(inputNew, inputLenNew, varsNew);
    }

    public State next(Long[] varsReplace) {
        return next(false, varsReplace);
    }

    public long getVar(char var) {
        return vars[varIndex(var)];
    }

    public static int varIndex(char var) {
        switch (var) {
            case 'w':
                return 0;
            case 'x':
                return 1;
            case 'y':
                return 2;
            case 'z':
                return 3;
            default:
                throw new IllegalArgumentException("invalid var: " + var);
        }
    }

    public String toString() {
        return "[input=" + input + ", w=" + vars[0] + ", x=" + vars[1] + ", y=" + vars[2] + ", z=" + vars[3] + "]";
    }
}
