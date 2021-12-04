import org.junit.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class RowOrColTrackerTest {

    @Test
    public void testConstructor() {
        // positive ints
        IntStream validCounts = new Random().ints(1000, 1, Integer.MAX_VALUE);
        validCounts.forEach(count -> {
            new RowOrColTracker(count);
        });
        // negative ints
        IntStream invalidCounts = new Random().ints(1000, Integer.MIN_VALUE, 0);
        invalidCounts.forEach(count -> {
            assertThrows(Exception.class, () -> new RowOrColTracker(count));
        });
        // zero
        assertThrows(Exception.class, () -> new RowOrColTracker(0));
    }

    @Test
    public void testMarkSquareOn() {
        RowOrColTracker rowOrColTracker = new RowOrColTracker(2);
        // 2 left
        assertFalse(rowOrColTracker.bingoOnRowOrCol());
        rowOrColTracker.markSquareOn();
        // 1 left
        assertFalse(rowOrColTracker.bingoOnRowOrCol());
        rowOrColTracker.markSquareOn();
        // 0 left
        assertTrue(rowOrColTracker.bingoOnRowOrCol());
    }

    @Test
    public void testOvermarked() {
        RowOrColTracker rowOrColTracker = new RowOrColTracker(1);
        rowOrColTracker.markSquareOn();
        // now empty, should refuse to update
        assertThrows(Exception.class, rowOrColTracker::markSquareOn);
    }
}
