import java.util.*;

/**
 * Represents a single bingo board, mapping the numbers on it to objects that track the state of each square and whether
 * their owning row and col have hit bingo yet.
 */
public class BingoBoard {
    private final Map<Integer, BingoSquare> squares;
    private boolean gotBingo = false;

    public BingoBoard(List<List<Integer>> board) {
        Map<Integer, BingoSquare> squaresMut = new HashMap<>();
        // build the row and col trackers, which will be provided to the applicable squares
        int rowCount = board.size();
        int colCount = board.get(0).size();
        List<RowOrColTracker> rowTrackers = rowsOrColsGen(rowCount, colCount);
        List<RowOrColTracker> colTrackers = rowsOrColsGen(colCount, rowCount);
        for (int i = 0; i < board.size(); i++) {
            List<Integer> row = board.get(i);
            for (int j = 0; j < row.size(); j++) {
                int number = row.get(j);
                squaresMut.put(number, new BingoSquare(number, rowTrackers.get(i), colTrackers.get(j)));
            }
        }
        this.squares = Collections.unmodifiableMap(squaresMut);
    }

    private static List<RowOrColTracker> rowsOrColsGen(int entries, int initVal) {
        List<RowOrColTracker> rowsOrCols = new ArrayList<>();
        for (int i = 0; i < entries; i++) {
            rowsOrCols.add(new RowOrColTracker(initVal));
        }
        return rowsOrCols;
    }

    public void mark(int number) {
        BingoSquare square = squares.get(number);
        if (square != null) {
            boolean bingo = square.mark();
            if (bingo) {
                gotBingo = true;
            }
        }
    }

    public boolean gotBingo() {
        return gotBingo;
    }

    public int getScore(int lastDrawn) {
        int sum = squares.values()
                .stream()
                .filter(square -> !square.isMarked())
                .map(square -> square.number())
                .reduce(0, Integer::sum);
        return sum * lastDrawn;
    }
}