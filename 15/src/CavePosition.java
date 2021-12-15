import java.util.ArrayList;
import java.util.List;

public class CavePosition {
    private List<CavePosition> neighbours = new ArrayList<>();
    private final int chitons;
    private int bestPath = Integer.MAX_VALUE;
    private final int row;
    private final int col;

    public static CavePosition createStart(int chitons, int row, int col) {
        CavePosition cavePosition = new CavePosition(chitons, row, col);
        cavePosition.bestPath = 0;
        return cavePosition;
    }

    public CavePosition(int chitons, int row, int col) {
        this.chitons = chitons;
        this.row = row;
        this.col = col;
    }

    public void addNeighbour(CavePosition neighbour) {
        //System.out.println("adding neighbour [r" + neighbour.row + ",c" + neighbour.col + "] to [r" + row + ",c" + col + "]");
        //System.out.println("adding a neighbour, chitons=" + chitons);
        neighbours.add(neighbour);
        neighbour.neighbours.add(this);
        recalculateBestPath(neighbour);
    }

    private void recalculateBestPath(CavePosition newNeighbour) {
        int pathHereViaNeighbour = newNeighbour.bestPath == Integer.MAX_VALUE ? Integer.MAX_VALUE : newNeighbour.bestPath + chitons;
        int pathToNeighbourViaHere = bestPath == Integer.MAX_VALUE ? Integer.MAX_VALUE : bestPath + newNeighbour.chitons;
        if (pathHereViaNeighbour < bestPath) {
            //System.out.println("found a shorter path here! chitons=" + chitons + ", neighChitons=" + newNeighbour.chitons);
            bestPath = pathHereViaNeighbour;
            for (CavePosition neighbour : neighbours) {
                recalculateBestPath(neighbour);
            }
        } else if (pathToNeighbourViaHere < newNeighbour.bestPath) {
            //System.out.println("found a shorter path to neighbour! chitons=" + chitons + ", neighChitons=" + newNeighbour.chitons);
            newNeighbour.bestPath = pathToNeighbourViaHere;
            for (CavePosition neighbour : newNeighbour.neighbours) {
                newNeighbour.recalculateBestPath(neighbour);
            }
        }
    }

    public int bestPath() {
        return this.bestPath;
    }
}
