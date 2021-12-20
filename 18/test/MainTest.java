import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE_MEDIUM_FOR_SUM =
            "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]\n" +
            "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]\n" +
            "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]\n" +
            "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]\n" +
            "[7,[5,[[3,8],[1,4]]]]\n" +
            "[[2,[2,2]],[8,[8,1]]]\n" +
            "[2,9]\n" +
            "[1,[[[9,3],9],[[9,0],[0,7]]]]\n" +
            "[[[5,[7,4]],7],1]\n" +
            "[[[[4,2],2],6],[8,7]]";

    private static final String SAMPLE =
            "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]\n" +
            "[[[5,[2,8]],4],[5,[[9,9],0]]]\n" +
            "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]\n" +
            "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]\n" +
            "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]\n" +
            "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]\n" +
            "[[[[5,4],[7,7]],8],[[8,3],8]]\n" +
            "[[9,3],[[9,9],[6,[4,9]]]]\n" +
            "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]\n" +
            "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]";

    @Test
    public void testSnailfishSumMagnitude() throws IOException {
        assertEquals(4140, Main.snailfishSumMagnitude(Utils.testLineReader(SAMPLE)));
    }

    @Test
    public void testLargestMagnitudeFromTwoSum() throws IOException {
        assertEquals(3993, Main.largestMagnitudeFromTwoSum(Utils.testLineReader(SAMPLE)));
    }

    @Test
    public void testSnailfishSum() throws IOException {
        assertEquals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", Main.snailfishSum(Utils.testLineReader(SAMPLE_MEDIUM_FOR_SUM)).toString());
    }

    @Test
    public void testSnailfishNumbersToString() throws IOException {
        BufferedReader lineReader = Utils.testLineReader(SAMPLE_MEDIUM_FOR_SUM);
        StringBuilder buf = new StringBuilder();
        String line;
        while ((line = lineReader.readLine()) != null) {
            if (buf.length() > 0) {
                buf.append('\n');
            }
            buf.append(Main.snailfishNumber(line));
        }
        assertEquals(SAMPLE_MEDIUM_FOR_SUM, buf.toString());
    }

    @Test
    public void testAddSnailfishNumbers() {
        assertEquals("[[1,2],[3,4]]", Main.addSnailfishNumbers(Main.snailfishNumber("[1,2]"), Main.snailfishNumber("[3,4]")).toString());
        // explode
        assertEquals("[[[[0,9],2],3],4]", Main.addSnailfishNumbers(Main.snailfishNumber("[[[[9,8],1],2],3]"), new RegularNumber(4, 0)).toString());
        assertEquals("[1,[1,[1,[8,0]]]]", Main.addSnailfishNumbers(new RegularNumber(1, 0), Main.snailfishNumber("[1,[1,[3,[5,6]]]]")).toString());
        // split
        assertEquals("[[8,9],[1,2]]", Main.addSnailfishNumbers(new RegularNumber(17,0), Main.snailfishNumber("[1,2]")).toString());
        // several steps
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
                Main.addSnailfishNumbers(
                        Main.snailfishNumber("[[[[4,3],4],4],[7,[[8,4],9]]]"),
                        Main.snailfishNumber("[1,1]"))
                        .toString());
    }

    @Test
    public void testMagnitude() {
        assertEquals(143, Main.snailfishNumber("[[1,2],[[3,4],5]]").getMagnitude());
        assertEquals(1384, Main.snailfishNumber("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").getMagnitude());
        assertEquals(445, Main.snailfishNumber("[[[[1,1],[2,2]],[3,3]],[4,4]]").getMagnitude());
        assertEquals(791, Main.snailfishNumber("[[[[3,0],[5,3]],[4,4]],[5,5]]").getMagnitude());
        assertEquals(1137, Main.snailfishNumber("[[[[5,0],[7,4]],[5,5]],[6,6]]").getMagnitude());
        assertEquals(3488, Main.snailfishNumber("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").getMagnitude());
    }
}
