/**
    Represents a square within a bingo board, along with the row and column it's in. This is enough info to be able to
    track when marking the square results in a bingo.
 */
public class BingoSquare {
    private final int number;
    private final RowOrColTracker rowTracker;
    private final RowOrColTracker colTracker;
    private boolean marked = false;

    public BingoSquare(int number, RowOrColTracker rowTracker, RowOrColTracker colTracker) {
        this.number = number;
        this.rowTracker = rowTracker;
        this.colTracker = colTracker;
    }

    /**
     * Marks the square - call when its number is drawn.
     *
     * @return True if marking this square resulted in a bingo, false otherwise.
     */
    public boolean mark() {
        if (marked) {
            throw new IllegalStateException("already marked");
        }
        marked = true;
        rowTracker.markSquareOn();
        colTracker.markSquareOn();
        return rowTracker.bingoOnRowOrCol() || colTracker.bingoOnRowOrCol();
    }

    public int number() {
        return number;
    }

    public boolean isMarked() {
        return marked;
    }
}