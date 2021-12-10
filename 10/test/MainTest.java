import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "[({(<(())[]>[[{[]{<()<>>\n" +
            "[(()[<>])]({[<{<<[]>>(\n" +
            "{([(<{}[<>[]}>{[]{[(<()>\n" +
            "(((({<>}<{<{<>}{[]{[]{}\n" +
            "[[<[([]))<([[{}[[()]]]\n" +
            "[{[{({}]{}}([{[{{{}}([]\n" +
            "{<[[]]>}<{[{[{[]{()[[[]\n" +
            "[<(<(<(<{}))><([]([]()\n" +
            "<{([([[(<>()){}]>(<<{{\n" +
            "<{([{{}}[<[[[<>{}]]]>[]]";

    @Test
    public void testCorruptedLinesErrorScore() throws IOException {
        assertEquals(26397, Main.errorScore(Utils.testLineReader(SAMPLE), true));
    }

    @Test
    public void testIncompleteLinesErrorScore() throws IOException {
        assertEquals(288957, Main.errorScore(Utils.testLineReader(SAMPLE), false));
    }
}
