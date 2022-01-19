public class Amphipod {
    public enum Type {
        A(Room.SIDEROOM_A, 1),
        B(Room.SIDEROOM_B, 10),
        C(Room.SIDEROOM_C, 100),
        D(Room.SIDEROOM_D, 1000);

        private final Room homeRoom;
        private final int stepCost;

        Type(Room homeRoom, int stepCost) {
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
}
