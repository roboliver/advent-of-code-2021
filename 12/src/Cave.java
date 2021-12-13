import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Cave {
    private enum CaveType {
        START(true, false, false),
        END(false, true, false),
        SMALL(false, false, false),
        LARGE(false, false, true);

        private final boolean isStart;
        private final boolean isEnd;
        private final boolean isRevisitable;

        CaveType(boolean isStart, boolean isEnd, boolean isRevisitable) {
            this.isStart = isStart;
            this.isEnd = isEnd;
            this.isRevisitable = isRevisitable;
        }
    }

    public String name() {return name;}

    public final String name;
    private final CaveType type;
    private final Set<Cave> connected = new HashSet<>();

    public Cave(String name) {
        this.name = Objects.requireNonNull(name);
        this.type = determineType(name);
    }

    private static CaveType determineType(String name) {
        if (name.equals("start")) {
            return CaveType.START;
        } else if (name.equals("end")) {
            return CaveType.END;
        } else if (name.equals(name.toLowerCase())) {
            return CaveType.SMALL;
        } else {
            return CaveType.LARGE;
        }
    }

    public void connect(Cave other) {
        connected.add(other);
        other.connected.add(this);
    }

    public boolean isStart() {
        return type.isStart;
    }

    public boolean isEnd() {
        return type.isEnd;
    }

    public boolean isRevisitable() {
        return type.isRevisitable;
    }

    public Set<Cave> connected() {
        return Collections.unmodifiableSet(connected);
    }
}
