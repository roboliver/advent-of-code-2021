import org.junit.Test;

import static org.junit.Assert.*;

public class BingoSquareTest {
    @Test
    public void testConstructor() {
        RowOrColTracker rowTracker = new RowOrColTracker(5);
        RowOrColTracker colTracker = new RowOrColTracker((9));
        new BingoSquare(1325, rowTracker, colTracker);
        assertThrows(Exception.class, () -> new BingoSquare(3523, null, colTracker));
        assertThrows(Exception.class, () -> new BingoSquare(523890, rowTracker, null));
    }

    @Test
    public void testMarkingBingo() {
        RowOrColTracker rowTracker = new RowOrColTracker(1);
        RowOrColTracker colTracker = new RowOrColTracker(1);
        BingoSquare square = new BingoSquare(890, rowTracker, colTracker);
        assertFalse(square.isMarked());
        square.mark();
        assertTrue(square.isMarked());
        assertTrue(rowTracker.bingoOnRowOrCol());
        assertTrue(colTracker.bingoOnRowOrCol());
    }

    @Test
    public void testMarkingNotBingo() {
        RowOrColTracker rowTracker = new RowOrColTracker(2);
        RowOrColTracker colTracker = new RowOrColTracker(2);
        BingoSquare square = new BingoSquare(890123, rowTracker, colTracker);
        assertFalse(square.isMarked());
        square.mark();
        assertTrue(square.isMarked());
        assertFalse(rowTracker.bingoOnRowOrCol());
        assertFalse(rowTracker.bingoOnRowOrCol());
    }
}
