import java.util.Objects;

public class Amphipod {
    public enum Type {
        A('A', Room.SIDEROOM_A, 1),
        B('B', Room.SIDEROOM_B, 10),
        C('C', Room.SIDEROOM_C, 100),
        D('D', Room.SIDEROOM_D, 1000);

        private final char typeChar;
        private final Room homeRoom;
        private final int stepCost;

        Type(char typeChar, Room homeRoom, int stepCost) {
            this.typeChar = typeChar;
            this.homeRoom = homeRoom;
            this.stepCost = stepCost;
        }
    }

    private final Type type;
    private final Room room;
    private final int space;

    public Amphipod(Type type, Room room, int space) {
        assertRoomAndSpaceValid(room, space);
        this.type = type;
        this.room = room;
        this.space = space;
    }

    private static void assertRoomAndSpaceValid(Room room, int space) {
        if (space < 0) {
            throw new IllegalArgumentException("space must be 0 or higher");
        } else if (room == Room.HALLWAY && space > 10) {
            throw new IllegalArgumentException("space must be between 0 and 10 when room is 0");
        }
    }

    public Amphipod moveTo(Room room, int space) {
        return new Amphipod(type, room, space);
    }

    public Room homeRoom() {
        return type.homeRoom;
    }

    public Room room() {
        return room;
    }

    public int space() {
        return space;
    }

    public int stepCost() {
        return type.stepCost;
    }

    public char typeChar() {
        return type.typeChar;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Amphipod)) {
            return false;
        } else {
            Amphipod other = (Amphipod) obj;
            return type == other.type && room == other.room && space == other.space;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, room, space);
    }

    @Override
    public String toString() {
        return "[" + type + ", " + room + ", " + space + "]";
    }
}
