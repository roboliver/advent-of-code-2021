import java.util.ArrayList;
import java.util.List;

/**
 * Represents a position within the cave. When a neighbour is added, we automatically recalculate the best path to the
 * new neighbour via this position, and to this position via the new neighbour.
 */
public class CavePosition {
    public static final int MIN_CHITONS = 1;
    public static final int MAX_CHITONS = 9;

    private final List<CavePosition> neighbours = new ArrayList<>();
    private final int chitons;
    private int bestPathTo;

    public CavePosition(int chitons, boolean isStart) {
        if (chitons < MIN_CHITONS || chitons > MAX_CHITONS) {
            throw new IllegalArgumentException("chitons must be between " + MIN_CHITONS + " and " + MAX_CHITONS);
        }
        this.chitons = chitons;
        this.bestPathTo = isStart ? 0 : Integer.MAX_VALUE;
    }

    /**
     * Add a neighbour to this cave position, and also recalculate the best path to this position given the new route
     * possibilities this neighbour provides.
     * @param neighbour The neighbouring cave position.
     */
    public void addNeighbour(CavePosition neighbour) {
        if (neighbour == this) {
            throw new IllegalArgumentException("a cave position can't neighbour itself");
        }
        // connect the positions to each other
        this.neighbours.add(neighbour);
        neighbour.neighbours.add(this);
        // update the best route for this position and its new neighbour, now that they are joined
        recalculateBestPathVia(neighbour);
        neighbour.recalculateBestPathVia(this);
    }

    private void recalculateBestPathVia(CavePosition via) {
        // see if there is a better path to this position via the neighbour given as argument
        int pathViaNeighbour = via.bestPathTo == Integer.MAX_VALUE ? Integer.MAX_VALUE : via.bestPathTo + this.chitons;
        if (pathViaNeighbour < this.bestPathTo) {
            // found one, so also update all this position's neighbours (excluding the one we got the route through,
            // which would be pointless) in case this gives them a better route too
            this.bestPathTo = pathViaNeighbour;
            for (CavePosition neighbour : this.neighbours) {
                if (neighbour != via) {
                    neighbour.recalculateBestPathVia(this);
                }
            }
        }
    }

    /**
     * Returns the best path from the starting position to this position.
     * @return The best (lowest risk) path to this position.
     */
    public int bestPathTo() {
        return this.bestPathTo;
    }
}
