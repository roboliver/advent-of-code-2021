import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Step when sea cucumbers stop: " + stepWhenStopped(lineReader));
        }
    }

    public static int stepWhenStopped(BufferedReader lineReader) throws IOException {
        return 0;
    }
}
