import java.util.ArrayList;
import java.util.List;

public class CavePosition {
    public static final int MIN_CHITONS = 1;
    public static final int MAX_CHITONS = 9;

    private List<CavePosition> neighbours = new ArrayList<>();
    private final int chitons;
    private int bestPathTo;

    public CavePosition(int chitons, boolean isStart) {
        if (chitons < MIN_CHITONS || chitons > MAX_CHITONS) {
            throw new IllegalArgumentException("chitons must be between " + MIN_CHITONS + " and " + MAX_CHITONS);
        }
        this.chitons = chitons;
        this.bestPathTo = isStart ? 0 : Integer.MAX_VALUE;
    }

    public void addNeighbour(CavePosition neighbour) {
        if (neighbour == this) {
            throw new IllegalArgumentException("a cave position can't neighbour itself");
        }
        neighbours.add(neighbour);
        neighbour.neighbours.add(this);
        recalculateBestPath(neighbour);
    }

    private void recalculateBestPath(CavePosition newNeighbour) {
        recalculateBestPath(this, newNeighbour);
        recalculateBestPath(newNeighbour, this);
    }

    private static void recalculateBestPath(CavePosition to, CavePosition via) {
        int pathToPositionVia = via.bestPathTo == Integer.MAX_VALUE ? Integer.MAX_VALUE : via.bestPathTo + to.chitons;
        if (pathToPositionVia < to.bestPathTo) {
            to.bestPathTo = pathToPositionVia;
            for (CavePosition neighbour : to.neighbours) {
                to.recalculateBestPath(neighbour);
            }
        }
    }

    public int bestPathTo() {
        return this.bestPathTo;
    }
}
