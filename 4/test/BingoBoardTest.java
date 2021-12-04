import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BingoBoardTest {

    @Test
    public void testConstructor() {
        // invalid inputs
        assertThrows(Exception.class, () -> new BingoBoard(null));
        int[][] firstRowShorter = new int[][]{
                {4, 5},
                {3, 1, 6}
        };
        int[][] firstRowLonger = new int[][]{
                {9, 0, 1},
                {3}
        };
        int[][] duplicates = new int[][]{
                {3, 2},
                {2, 4}
        };
        assertThrows(Exception.class, () -> new BingoBoard(inputGen(firstRowShorter)));
        assertThrows(Exception.class, () -> new BingoBoard(inputGen(firstRowLonger)));
        assertThrows(Exception.class, () -> new BingoBoard(inputGen(duplicates)));
        // valid inputs
        int[][] oneByOne = new int[][]{
                {9090909}
        };
        int[][] fourByThree = new int[][]{
                {523, 409, 235, 902},
                {4, 54, 2309, 5490},
                {9543, -3253, 0, 7}
        };
        new BingoBoard(inputGen(oneByOne));
        new BingoBoard(inputGen(fourByThree));
    }

    @Test
    public void testBingo() {
        int[][] inputArrays = new int[][]{
                {2, 5, 6},
                {4, 0, 1},
                {9, 7, 8}
        };
        BingoBoard board = new BingoBoard(inputGen(inputArrays));
        board.mark(0);
        assertFalse(board.gotBingo());
        board.mark(5);
        assertFalse(board.gotBingo());
        board.mark(4);
        assertFalse(board.gotBingo());
        board.mark(9);
        assertFalse(board.gotBingo());
        board.mark(2);
        assertTrue(board.gotBingo());
        assertThrows(Exception.class, () -> board.mark(7));
    }

    @Test
    public void testScore() {
        int[][] inputArrays = new int[][]{
                {3, 9},
                {7, 8}
        };
        BingoBoard board = new BingoBoard(inputGen(inputArrays));
        assertThrows(Exception.class, () -> board.getScore());
        board.mark(3);
        assertThrows(Exception.class, () -> board.getScore());
        board.mark(7);
        assertEquals(119, board.getScore());
    }

    private static List<List<Integer>> inputGen(int[][] inputArrays) {
        List<List<Integer>> input = new ArrayList<>();
        for (int[] rowArray : inputArrays) {
            List<Integer> row = new ArrayList<>();
            for (int number : rowArray) {
                row.add(number);
            }
            input.add(row);
        }
        return input;
    }
}
