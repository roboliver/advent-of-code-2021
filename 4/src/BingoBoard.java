import java.util.*;

/**
 * Represents a single bingo board, mapping the numbers on it to objects that track the state of each square and whether
 * their owning row and col have hit bingo yet.
 */
public class BingoBoard {
    private final Map<Integer, BingoSquare> squares;
    private boolean gotBingo = false;
    private int lastDrawn;

    public BingoBoard(List<List<Integer>> board) {
        Map<Integer, BingoSquare> squaresMut = new HashMap<>();
        // build the row and col trackers, which will be provided to the applicable squares
        int rowCount = board.size();
        int colCount = board.get(0).size();
        List<RowOrColTracker> rowTrackers = rowsOrColsGen(rowCount, colCount);
        List<RowOrColTracker> colTrackers = rowsOrColsGen(colCount, rowCount);
        for (int i = 0; i < board.size(); i++) {
            List<Integer> row = board.get(i);
            if (row.size() != colCount) {
                throw new IllegalArgumentException("all rows must be the same length");
            }
            for (int j = 0; j < row.size(); j++) {
                int number = row.get(j);
                if (squaresMut.containsKey(number)) {
                    throw new IllegalArgumentException("input must not contain duplicates");
                }
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

    /**
     * Marks the number on the board. Call {@code gotBingo} afterwards to see if this resulted in a bingo.
     * @param number
     */
    public void mark(int number) {
        if (gotBingo) {
            throw new IllegalStateException("already got bingo");
        }
        BingoSquare square = squares.get(number);
        if (square != null) {
            boolean bingo = square.mark();
            if (bingo) {
                gotBingo = true;
                lastDrawn = number;
            }
        }
    }

    public boolean gotBingo() {
        return gotBingo;
    }

    /**
     * Returns the score for this board. Note this can only be called once the board reaches bingo.
     * @return The board's score, which is the sum of the unmarked numbers times the last marked number
     */
    public int getScore() {
        if (!gotBingo) {
            throw new IllegalStateException("can't calculate the score until the board reaches bingo");
        }
        int sum = squares.values()
                .stream()
                .filter(square -> !square.isMarked())
                .map(square -> square.number())
                .reduce(0, Integer::sum);
        return sum * lastDrawn;
    }
}