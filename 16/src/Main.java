import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Version sum: " + versionSum(lineReader));
        }
    }

    public static int versionSum(BufferedReader lineReader) throws IOException {
        return 0;
    }
}
