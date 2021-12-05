import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

public class Utils {
    private static final String INPUT_DEFAULT = "input.txt";

    /**
     * Returns a reader for the default input file name.
     *
     * @return The file reader.
     * @throws FileNotFoundException If the file doesn't exist.
     */
    public static BufferedReader inputLineReader() throws FileNotFoundException {
        return inputLineReader(INPUT_DEFAULT);
    }

    /**
     * Returns a reader for the specified file name.
     *
     * @param inputFile The file name.
     * @return The file reader.
     * @throws FileNotFoundException If the file doesn't exist.
     */
    public static BufferedReader inputLineReader(String inputFile) throws FileNotFoundException {
        return new BufferedReader(new FileReader(inputFile));
    }

    /**
     * Returns a reader that will return the specified text, for use in testing.
     *
     * @param sampleText The text to return from the reader.
     * @return The reader.
     */
    public static BufferedReader testLineReader(String sampleText) {
        return new BufferedReader(new StringReader(sampleText));
    }
}
