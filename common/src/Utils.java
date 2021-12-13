import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public static int[][] readIntArray(BufferedReader lineReader) throws IOException {
        List<List<Integer>> valuesList = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            List<Integer> lineValues = new ArrayList<>();
            valuesList.add(lineValues);
            for (char c : line.toCharArray()) {
                lineValues.add(Character.getNumericValue(c));
            }
        }
        int[][] valuesArray = new int[valuesList.size()][];
        for (int i = 0; i < valuesArray.length; i++) {
            List<Integer> lineValues = valuesList.get(i);
            valuesArray[i] = new int[lineValues.size()];
            for (int j = 0; j < lineValues.size(); j++) {
                valuesArray[i][j] = lineValues.get(j);
            }
        }
        lineReader.close();
        return valuesArray;
    }
}
