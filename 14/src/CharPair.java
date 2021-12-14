import java.util.Arrays;

public class CharPair {
    private final char first;
    private final char second;

    public CharPair(char first, char second) {
        this.first = first;
        this.second = second;
    }

    public char first() {
        return first;
    }

    public char second() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof CharPair)) {
            return false;
        } else {
            return this.first == ((CharPair) obj).first && this.second == ((CharPair) obj).second;
        }
    }

    @Override
    public int hashCode() {
        char[] vals = {first, second};
        return Arrays.hashCode(vals);
    }
}
