import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Largest accepted model number: " + maxModelNumber(lineReader));
        }
    }

    public static int maxModelNumber(BufferedReader lineReader) throws IOException {
        return 0;
    }
}
