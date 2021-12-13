import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Cave {
    public enum Type {
        START,
        END,
        SMALL,
        LARGE;
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

    public Type type() {
        return type;
    }

    public Set<Cave> connected() {
        return Collections.unmodifiableSet(connected);
    }
}
