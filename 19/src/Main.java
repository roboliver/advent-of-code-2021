import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Beacon count: " + beaconCount(lineReader));
        }
    }

    public static int beaconCount(BufferedReader lineReader) {
        return 0;
    }
}
