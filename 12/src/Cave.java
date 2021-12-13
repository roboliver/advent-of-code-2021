import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A cave (including the start and end positions, which I am treating as caves), and the other caves it is connected to.
 */
public class Cave {
    private enum Type {
        START,
        END,
        SMALL,
        LARGE
    }

    public final String name;
    private final Type type;
    private final Set<Cave> connected = new HashSet<>();

    public Cave(String name) {
        this.name = Objects.requireNonNull(name);
        this.type = determineType(name);
    }

    private static Type determineType(String name) {
        if (name.equals("start")) {
            return Type.START;
        } else if (name.equals("end")) {
            return Type.END;
        } else if (name.equals(name.toLowerCase())) {
            return Type.SMALL;
        } else {
            return Type.LARGE;
        }
    }

    public void connect(Cave other) {
        connected.add(other);
        other.connected.add(this);
    }

    public boolean isStart() {
        return type == Type.START;
    }

    public boolean isEnd() {
        return type == Type.END;
    }

    public boolean isSmall() {
        return type == Type.SMALL;
    }

    public boolean isLarge() {
        return type == Type.LARGE;
    }

    public Set<Cave> connected() {
        return Collections.unmodifiableSet(connected);
    }
}
