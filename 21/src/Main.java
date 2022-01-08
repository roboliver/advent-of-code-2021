import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Losing score multiplied by die rolls: " + losingScoreTimesDieRolls(lineReader));
        }
    }

    public static int losingScoreTimesDieRolls(BufferedReader lineReader) throws IOException {
        return 0;
    }
}
