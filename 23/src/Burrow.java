import java.util.*;

public class Burrow {
    private static final int ENTRANCE_A = 2;
    private static final int ENTRANCE_B = 4;
    private static final int ENTRANCE_C = 6;
    private static final int ENTRANCE_D = 8;
    private static final int HALLWAY_SIZE = 11;
    private static final int AMPHIPOD_COUNT = 8;
    private final Amphipod[] amphipods = new Amphipod[AMPHIPOD_COUNT];
    private final Map<Room, Amphipod[]> rooms;

    public Burrow(List<Amphipod> amphipods, int sideRoomSize) {
        Map<Room, Amphipod[]> rooms = emptyRooms(sideRoomSize);
        for (int i = 0; i < amphipods.size(); i++) {
            Amphipod amphipod = amphipods.get(i);
            this.amphipods[i] = amphipod;
            rooms.get(amphipod.room())[amphipod.space()] = amphipod;
        }
        this.rooms = Collections.unmodifiableMap(rooms);
    }

    private static Map<Room, Amphipod[]> emptyRooms(int sideRoomSize) {
        Map<Room, Amphipod[]> rooms = new HashMap<>();
        for (Room room : Room.values()) {
            int roomSize = room == Room.HALLWAY ? HALLWAY_SIZE : sideRoomSize;
            rooms.put(room, new Amphipod[roomSize]);
        }
        return rooms;
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
        if (amphipod.room() == amphipod.homeRoom() || !canEscape(amphipod)) {
            return Collections.emptyList();
        }
        int entrance = entranceFromSideRoom(amphipod.room());
        return validMovesFromHallwaySpace(entrance, amphipod.homeRoom());
    }

    private List<Map.Entry<Room, Integer>> validMovesFromHallwaySpace(int hallwaySpace, Room homeRoom) {
        List<Map.Entry<Room, Integer>> validMoves = new ArrayList<>();
        List<Integer> reachableHallwaySpaces = reachableHallwaySpaces(hallwaySpace);
        for (int space : reachableHallwaySpaces) {
            if (!isEntrance(space)) {
                validMoves.add(new AbstractMap.SimpleImmutableEntry<>(Room.HALLWAY, space));
            } else if (sideRoomFromEntrance(space) == homeRoom && sideRoomLandable(homeRoom)) {
                validMoves.add(new AbstractMap.SimpleImmutableEntry<>(homeRoom, homeLandingSpace(homeRoom)));
            }
        }
        return validMoves;
    }

    private List<Integer> reachableHallwaySpaces(int fromHallwaySpace) {
        List<Integer> reachable = new ArrayList<>();
        Amphipod[] hallway = rooms.get(Room.HALLWAY);
        for (int i = fromHallwaySpace + 1; i < HALLWAY_SIZE; i++) {
            if (hallway[i] != null) {
                break;
            }
            reachable.add(i);
        }
        for (int i = fromHallwaySpace - 1; i >= 0; i--) {
            if (hallway[i] != null) {
                break;
            }
            reachable.add(i);
        }
        return reachable;
    }

    private boolean sideRoomLandable(Room sideRoom) {
        Amphipod[] roomAmphipods = rooms.get(sideRoom);
        for (Amphipod roomAmphipod : roomAmphipods) {
            if (roomAmphipod != null && roomAmphipod.homeRoom() != sideRoom) {
                return false;
            }
        }
        return true;
    }

    private int homeLandingSpace(Room sideRoom) {
        Amphipod[] roomAmphipods = rooms.get(sideRoom);
        for (int i = roomAmphipods.length - 1; i >= 0; i--) {
            if (roomAmphipods[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("side room is full");
    }

    private boolean canEscape(Amphipod amphipod) {
        Amphipod[] sideRoom = rooms.get(amphipod.room());
        for (int i = amphipod.space() - 1; i >= 0; i--) {
            if (sideRoom[i] != null) {
                return false;
            }
        }
        return true;
    }

    public int stepCostTo(Amphipod amphipod, Room room, int space) {
        return amphipod.stepCost() * stepsTo(amphipod, room, space);
    }

    private int stepsTo(Amphipod amphipod, Room room, int space) {
//        if (room == amphipod.room()) {
//            throw new IllegalArgumentException("amphipods must change rooms when moving");
//        }
        return stepsToHallway(amphipod, room, space)
                + stepsWithinHallway(amphipod, room, space)
                + stepsIntoSideRoom(amphipod, room, space);
    }

    private int stepsToHallway(Amphipod amphipod, Room room, int space) {
        return amphipod.room() == Room.HALLWAY ? 0 : amphipod.space() + 1;
//        if (amphipod.room() == Room.HALLWAY) {
//            return 0;
//        } else {
////            assertNotBlocked(room, amphipod.space(), space, amphipod);
//            return amphipod.space() + 1;
//        }
    }

    private int stepsWithinHallway(Amphipod amphipod, Room room, int space) {
        int startHallwaySpace = (amphipod.room() == Room.HALLWAY)
                ? amphipod.space() : entranceFromSideRoom(amphipod.room());
        int endHallwaySpace = (room == Room.HALLWAY) ? space : entranceFromSideRoom(room);
//        assertNotBlocked(Room.HALLWAY, startHallwaySpace, endHallwaySpace, amphipod);
        return Math.abs(endHallwaySpace - startHallwaySpace);
    }

    private int stepsIntoSideRoom(Amphipod amphipod, Room room, int space) {
        return room == Room.HALLWAY ? 0 : space + 1;
//        if (room == Room.HALLWAY) {
//            return 0;
//        } else {
//
//        }
    }

    public Burrow moveAmphipod(Amphipod amphipod, Room room, int space) {
        Amphipod[] amphipodsNew = new Amphipod[AMPHIPOD_COUNT];
        for (int i = 0; i < AMPHIPOD_COUNT; i++) {
            Amphipod amphipodNew = (amphipods[i] == amphipod)
                    ? amphipod.moveTo(room, space) : amphipods[i];
            amphipodsNew[i] = amphipodNew;
        }
        return new Burrow(Arrays.asList(amphipodsNew), sideRoomSize());
    }

    private int sideRoomSize() {
        return rooms.get(Room.SIDEROOM_A).length;
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

//    private void assertNotBlocked(Room room, int fromSpace, int toSpace, Amphipod amphipod) {
//        Amphipod[] roomAmphipods = rooms.get(room);
//        int start = Math.min(fromSpace, toSpace);
//        int end = Math.max(fromSpace, toSpace);
//        for (int i = start; i <= end; i++) {
//            if (roomAmphipods[i] != null && roomAmphipods[i] != amphipod) {
//                throw new IllegalStateException("route blocked");
//            }
//        }
//    }
}
