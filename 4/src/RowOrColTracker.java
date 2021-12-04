/**
 * Tracks how many squares on a row or column remain unmarked.
 */
public class RowOrColTracker {
    private int count;

    public RowOrColTracker(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be at least 1");
        }
        this.count = count;
    }

    public void markSquareOn() {
        if (count == 0) {
            throw new IllegalStateException("all squares already marked");
        }
        count--;
    }

    public boolean bingoOnRowOrCol() {
        return count == 0;
    }
}