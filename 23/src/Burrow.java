import java.util.*;

public class Burrow {
    private static final int ENTRANCE_A = 2;
    private static final int ENTRANCE_B = 4;
    private static final int ENTRANCE_C = 6;
    private static final int ENTRANCE_D = 8;
    private static final int HALLWAY_SIZE = 11;
    private final Set<Amphipod> amphipods;
    private final Map<Room, List<Amphipod>> rooms;

    public Burrow(Set<Amphipod> amphipods) {
        int sideRoomSize = amphipods.size() / 4;
        this.amphipods = Set.copyOf(amphipods);
        Map<Room, List<Amphipod>> rooms = emptyRooms(sideRoomSize);
        for (Amphipod amphipod : amphipods) {
            rooms.get(amphipod.room()).set(amphipod.space(), amphipod);
        }
        this.rooms = Collections.unmodifiableMap(rooms);
    }

    private static Map<Room, List<Amphipod>> emptyRooms(int sideRoomSize) {
        Map<Room, List<Amphipod>> rooms = new HashMap<>();
        for (Room room : Room.values()) {
            int roomSize = room == Room.HALLWAY ? HALLWAY_SIZE : sideRoomSize;
            List<Amphipod> amphipods = new ArrayList<>();
            for (int i = 0; i < roomSize; i++) {
                amphipods.add(null);
            }
            rooms.put(room, amphipods);
        }
        return rooms;
    }

    public Set<Amphipod> amphipods() {
        return amphipods;
    }

    public boolean isSolved() {
        for (Map.Entry<Room, List<Amphipod>> room : rooms.entrySet()) {
            if (room.getKey() != Room.HALLWAY) {
                for (Amphipod amphipod : room.getValue()) {
                    if (amphipod == null || amphipod.homeRoom() != room.getKey()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<Map.Entry<Room, Integer>> validMoves(Amphipod amphipod) {
        if (amphipod.room() == Room.HALLWAY) {
            return validMovesFromHallway(amphipod);
        } else {
            return validMovesFromSideRoom(amphipod);
        }
    }

    private List<Map.Entry<Room, Integer>> validMovesFromHallway(Amphipod amphipod) {
        return validMovesFromHallwaySpace(amphipod.space(), amphipod.homeRoom());
    }

    private List<Map.Entry<Room, Integer>> validMovesFromSideRoom(Amphipod amphipod) {
        if ((amphipod.room() == amphipod.homeRoom() && sideRoomLandable(amphipod.room()))
                || !canEscape(amphipod)) {
            return Collections.emptyList();
        }
        int entrance = entranceFromSideRoom(amphipod.room());
        return validMovesFromHallwaySpace(entrance, amphipod.homeRoom());
    }

    private List<Map.Entry<Room, Integer>> validMovesFromHallwaySpace(int fromSpace, Room homeRoom) {
        List<Map.Entry<Room, Integer>> validMoves = new ArrayList<>();
        List<Integer> reachableHallwaySpaces = reachableHallwaySpaces(fromSpace);
        for (int reachableHallwaySpace : reachableHallwaySpaces) {
            if (!isEntrance(reachableHallwaySpace)) {
                if (isEntrance(fromSpace)) {
                    validMoves.add(new AbstractMap.SimpleImmutableEntry<>(Room.HALLWAY, reachableHallwaySpace));
                }
            } else if (sideRoomFromEntrance(reachableHallwaySpace) == homeRoom && sideRoomLandable(homeRoom)) {
                validMoves.add(new AbstractMap.SimpleImmutableEntry<>(homeRoom, homeLandingSpace(homeRoom)));
            }
        }
        return validMoves;
    }

    private List<Integer> reachableHallwaySpaces(int fromHallwaySpace) {
        List<Integer> reachable = new ArrayList<>();
        List<Amphipod> hallway = rooms.get(Room.HALLWAY);
        for (int i = fromHallwaySpace + 1; i < HALLWAY_SIZE; i++) {
            if (hallway.get(i) != null) {
                break;
            }
            reachable.add(i);
        }
        for (int i = fromHallwaySpace - 1; i >= 0; i--) {
            if (hallway.get(i) != null) {
                break;
            }
            reachable.add(i);
        }
        return reachable;
    }

    private boolean sideRoomLandable(Room sideRoom) {
        List<Amphipod> roomAmphipods = rooms.get(sideRoom);
        for (Amphipod roomAmphipod : roomAmphipods) {
            if (roomAmphipod != null && roomAmphipod.homeRoom() != sideRoom) {
                return false;
            }
        }
        return true;
    }

    private int homeLandingSpace(Room sideRoom) {
        List<Amphipod> roomAmphipods = rooms.get(sideRoom);
        for (int i = roomAmphipods.size() - 1; i >= 0; i--) {
            if (roomAmphipods.get(i) == null) {
                return i;
            }
        }
        throw new IllegalStateException("side room is full");
    }

    private boolean canEscape(Amphipod amphipod) {
        List<Amphipod> sideRoom = rooms.get(amphipod.room());
        for (int i = amphipod.space() - 1; i >= 0; i--) {
            if (sideRoom.get(i) != null) {
                return false;
            }
        }
        return true;
    }

    public int stepCostTo(Amphipod amphipod, Room room, int space) {
        return amphipod.stepCost() * stepsTo(amphipod, room, space);
    }

    private int stepsTo(Amphipod amphipod, Room room, int space) {
        return stepsToHallway(amphipod)
                + stepsWithinHallway(amphipod, room, space)
                + stepsIntoSideRoom(room, space);
    }

    private int stepsToHallway(Amphipod amphipod) {
        return amphipod.room() == Room.HALLWAY ? 0 : amphipod.space() + 1;
    }

    private int stepsWithinHallway(Amphipod amphipod, Room room, int space) {
        int startHallwaySpace = (amphipod.room() == Room.HALLWAY)
                ? amphipod.space() : entranceFromSideRoom(amphipod.room());
        int endHallwaySpace = (room == Room.HALLWAY) ? space : entranceFromSideRoom(room);
        return Math.abs(endHallwaySpace - startHallwaySpace);
    }

    private int stepsIntoSideRoom(Room room, int space) {
        return room == Room.HALLWAY ? 0 : space + 1;
    }

    public Burrow moveAmphipod(Amphipod amphipod, Room room, int space) {
        Set<Amphipod> amphipodsNew = new HashSet<>();
        for (Amphipod amphipodCur : amphipods) {
            Amphipod amphipodNew = (amphipodCur == amphipod) ? amphipod.moveTo(room, space) : amphipodCur;
            amphipodsNew.add(amphipodNew);
        }
        return new Burrow(amphipodsNew);
    }

    private Room sideRoomFromEntrance(int entrance) {
        switch (entrance) {
            case ENTRANCE_A:
                return Room.SIDEROOM_A;
            case ENTRANCE_B:
                return Room.SIDEROOM_B;
            case ENTRANCE_C:
                return Room.SIDEROOM_C;
            case ENTRANCE_D:
                return Room.SIDEROOM_D;
            default:
                throw new IllegalArgumentException("space specified isn't a sideroom entrance");
        }
    }

    private int entranceFromSideRoom(Room room) {
        switch (room) {
            case SIDEROOM_A:
                return ENTRANCE_A;
            case SIDEROOM_B:
                return ENTRANCE_B;
            case SIDEROOM_C:
                return ENTRANCE_C;
            case SIDEROOM_D:
                return ENTRANCE_D;
            case HALLWAY:
            default:
                throw new IllegalArgumentException("no single determinable hallway space for the hallway");
        }
    }

    private boolean isEntrance(int hallwaySpace) {
        switch (hallwaySpace) {
            case ENTRANCE_A:
            case ENTRANCE_B:
            case ENTRANCE_C:
            case ENTRANCE_D:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("#############\n");
        buf.append('#');
        for (Amphipod amphipod : rooms.get(Room.HALLWAY)) {
            buf.append(amphipodChar(amphipod));
        }
        buf.append("#\n");
        for (int i = 0; i < rooms.get(Room.SIDEROOM_A).size(); i++) {
            buf.append(i > 0 ? "  #" : "###");
            buf.append(amphipodChar(rooms.get(Room.SIDEROOM_A).get(i)));
            buf.append('#');
            buf.append(amphipodChar(rooms.get(Room.SIDEROOM_B).get(i)));
            buf.append('#');
            buf.append(amphipodChar(rooms.get(Room.SIDEROOM_C).get(i)));
            buf.append('#');
            buf.append(amphipodChar(rooms.get(Room.SIDEROOM_D).get(i)));
            buf.append(i > 0 ? "#  \n" : "###\n");
        }
        buf.append("  #########");
        return buf.toString();
    }

    private static char amphipodChar(Amphipod amphipod) {
        return amphipod == null ? '.' : amphipod.typeChar();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Burrow)) {
            return false;
        } else {
            Burrow other = (Burrow) obj;
            return amphipods.equals(other.amphipods) && rooms.equals(other.rooms);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(amphipods, rooms);
    }
}
