import org.junit.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class DeterministicDieTest {
    @Test
    public void testRoll() {
        DeterministicDie die = new DeterministicDie(100);
        for (int i = 0; i < 100; i++) {
            int roll = die.roll().get(0);
            assertEquals(i + 1, roll);
        }
        int roll = die.roll().get(0);
        assertEquals(1, roll);
    }

    @Test
    public void testRollCount() {
        IntStream rollCounts = new Random().ints(1000, 1, 1000);
        rollCounts.forEach(rollCount -> {
            DeterministicDie die = new DeterministicDie(100);
            for (int i = 0; i < rollCount; i++) {
                die.roll();
            }
            assertEquals(rollCount, die.rollCount());
        });
    }
}
