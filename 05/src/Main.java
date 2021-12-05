import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {
    // indexes into the array we use to save the minimum and maximum x and y values found when reading the lines, to use
    // to construct the seafloor to the appropriate size
    private static final int SEAFLOOR_XMIN = 0;
    private static final int SEAFLOOR_XMAX = 1;
    private static final int SEAFLOOR_YMIN = 2;
    private static final int SEAFLOOR_YMAX = 3;

    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Vent overlaps (without diagonals): " + ventOverlaps(lineReader, false));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Vent overlaps (with diagonals): " + ventOverlaps(lineReader, true));
        }
    }

    public static int ventOverlaps(BufferedReader lineReader, boolean includeDiagonals) throws IOException {
        int[] seafloorSize = new int[4];
        Collection<Vent> vents = vents(lineReader, seafloorSize, includeDiagonals);
        Seafloor seafloor = new Seafloor(seafloorSize[SEAFLOOR_XMIN], seafloorSize[SEAFLOOR_XMAX],
                seafloorSize[SEAFLOOR_YMIN], seafloorSize[SEAFLOOR_YMAX]);
        for (Vent vent : vents) {
            seafloor.addVent(vent);
        }
        return seafloor.ventOverlaps();
    }

    private static Collection<Vent> vents(BufferedReader lineReader, int[] seafloorSize,
                                          boolean includeDiagonals) throws IOException {
        List<Vent> vents = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            String[] points = line.split(" -> ");
            Point start = point(points[0]);
            Point end = point(points[1]);
            if (includeDiagonals || start.x() == end.x() || start.y() == end.y()) {
                vents.add(new Vent(start, end));
                seafloorSize[SEAFLOOR_XMIN] = newMin(seafloorSize[SEAFLOOR_XMIN], start.x(), end.x());
                seafloorSize[SEAFLOOR_XMAX] = newMax(seafloorSize[SEAFLOOR_XMAX], start.x(), end.x());
                seafloorSize[SEAFLOOR_YMIN] = newMin(seafloorSize[SEAFLOOR_YMIN], start.y(), end.y());
                seafloorSize[SEAFLOOR_YMAX] = newMax(seafloorSize[SEAFLOOR_YMAX], start.y(), end.y());
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
