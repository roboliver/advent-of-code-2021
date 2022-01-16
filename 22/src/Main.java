import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Cubes on in region -50..50 (in all dimensions): " + cubesOn(lineReader, -50, 50));
        }
    }

    public static int cubesOn(BufferedReader lineReader) {
        return cubesOn(lineReader, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int cubesOn(BufferedReader lineReader, int regionMin, int regionMax) {
        return 0;
    }
}
