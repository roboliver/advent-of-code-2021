import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {
    private static final String INPUT = "input.txt";

    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = inputLineReader()) {
            System.out.println("Vent overlaps (without diagonals): " + ventOverlaps(lineReader, false));
        }
        try (BufferedReader lineReader = inputLineReader()) {
            System.out.println("Vent overlaps (with diagonals): " + ventOverlaps(lineReader, true));
        }
    }

    private static BufferedReader inputLineReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(INPUT));
    }

    public static int ventOverlaps(BufferedReader lineReader, boolean includeDiagonals) throws IOException {
        Collection<Vent> vents = vents(lineReader, includeDiagonals);
        Seafloor seafloor = new Seafloor();
        for (Vent vent : vents) {
            seafloor.addVent(vent);
        }
        return seafloor.ventOverlaps();
    }

    private static Collection<Vent> vents(BufferedReader lineReader, boolean includeDiagonals) throws IOException {
        List<Vent> vents = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            String[] points = line.split(" -> ");
            Point start = point(points[0]);
            Point end = point(points[1]);
            if (includeDiagonals || start.x() == end.x() || start.y() == end.y()) {
                vents.add(new Vent(start, end));
            }
        }
        return vents;
    }

    private static Point point(String pointStr) {
        int x = Integer.parseInt(pointStr.substring(0, pointStr.indexOf(',')));
        int y = Integer.parseInt(pointStr.substring(pointStr.indexOf(',') + 1));
        return new Point(x, y);
    }

    private static int newMin(int cur, int new1, int new2) {
        return Math.min(cur, Math.min(new1, new2));
    }

    private static int newMax(int cur, int new1, int new2) {
        return Math.max(cur, Math.max(new1, new2));
    }
}
