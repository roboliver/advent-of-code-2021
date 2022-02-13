import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Step when sea cucumbers stop: " + stepWhenStopped(lineReader));
        }
    }

    public static int stepWhenStopped(BufferedReader lineReader) throws IOException {
        Seafloor seafloor = seafloor(lineReader);
        int steps = 0;
        boolean stopped = false;
        while (!stopped) {
            stopped = !seafloor.step();
            steps++;
        }
        return steps;
    }

    private static Seafloor seafloor(BufferedReader lineReader) throws IOException {
        List<List<SeaCucumber>> seaCucumbersList = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            List<SeaCucumber> lineSeaCucumbers = new ArrayList<>();
            seaCucumbersList.add(lineSeaCucumbers);
            for (char c : line.toCharArray()) {
                if (c == '>') {
                    lineSeaCucumbers.add(SeaCucumber.EAST);
                } else if (c == 'v') {
                    lineSeaCucumbers.add(SeaCucumber.SOUTH);
                } else {
                    lineSeaCucumbers.add(null);
                }
            }
        }
        SeaCucumber[][] seaCucumbersArray = new SeaCucumber[seaCucumbersList.size()][];
        for (int row = 0; row < seaCucumbersArray.length; row++) {
            List<SeaCucumber> lineSeaCucumbers = seaCucumbersList.get(row);
            seaCucumbersArray[row] = new SeaCucumber[lineSeaCucumbers.size()];
            for (int col = 0; col < lineSeaCucumbers.size(); col++) {
                seaCucumbersArray[row][col] = lineSeaCucumbers.get(col);
            }
        }
        lineReader.close();
        return new Seafloor(seaCucumbersArray);
    }
}
