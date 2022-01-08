import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MainTest {
    private static final String SAMPLE =
            "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#\n"
            + "\n"
            + "#..#.\n"
            + "#....\n"
            + "##..#\n"
            + "..#..\n"
            + "..###";
    @Test
    public void testPixelsLitAfterEnhance2Times() throws IOException {
        assertEquals(35, Main.pixelsLitAfterEnhance(Utils.testLineReader(SAMPLE), 2));
    }

    @Test
    public void testPixelsLitAfterEnhance50Times() throws IOException {
        assertEquals(3351, Main.pixelsLitAfterEnhance(Utils.testLineReader(SAMPLE), 50));
    }
}
