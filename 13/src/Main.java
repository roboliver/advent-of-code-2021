import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Dots after one fold: " + dotsAfterNFolds(lineReader, 1));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("The code is: \n" + fullyFold(lineReader));
        }
    }

    public static int dotsAfterNFolds(BufferedReader lineReader, int foldCount) throws IOException {
        Paper paper = paper(lineReader);
        List<Fold> folds = folds(lineReader);
        for (int i = 0; i < foldCount; i++) {
            paper.fold(folds.get(i));
        }
        return paper.dotCount();
    }

    public static String fullyFold(BufferedReader lineReader) throws IOException {
        Paper paper = paper(lineReader);
        List<Fold> folds = folds(lineReader);
        for (Fold fold : folds) {
            paper.fold(fold);
        }
        return paper.toOutput();
    }

    private static Paper paper(BufferedReader lineReader) throws IOException {
        Paper paper = new Paper();
        String line;
        while (!(line = lineReader.readLine()).isBlank()) {
            String[] coords = line.split(",");
            Point dot = new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
            paper.addDot(dot);
        }
        return paper;
    }

    private static List<Fold> folds(BufferedReader lineReader) throws IOException {
        List<Fold> folds = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            if (!line.isBlank()) {
                char axis = line.charAt("fold along ".length());
                int pos = Integer.parseInt(line.substring(line.indexOf("=") + 1));
                folds.add(new Fold(axis, pos));
            }
        }
        lineReader.close();
        return folds;
    }
}
