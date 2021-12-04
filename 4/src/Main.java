import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static final String INPUT = "input.txt";

    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = inputLineReader()) {
            System.out.println("Winning bingo score: " + winningBingoScore(lineReader));
        }
    }

    private static BufferedReader inputLineReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(INPUT));
    }

    public static int winningBingoScore(BufferedReader lineReader) throws IOException {
        // todo: implement
        return 0;
    }
}
