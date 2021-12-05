import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {
    private static final String INPUT = "input.txt";

    // indexes into the array we use to save the minimum and maximum x and y values found when reading the lines, to use
    // to construct the seafloor to the appropriate size
    private static final int SEAFLOOR_XMIN = 0;
    private static final int SEAFLOOR_YMIN = 1;
    private static final int SEAFLOOR_XMAX = 2;
    private static final int SEAFLOOR_YMAX = 3;

    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = inputLineReader()) {
            System.out.println("Vent overlaps: " + ventOverlaps(lineReader));
        }
    }

    private static BufferedReader inputLineReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(INPUT));
    }

    public static int ventOverlaps(BufferedReader lineReader) throws IOException {
        int[] seafloorSize = new int[4];
        Collection<Vent> vents = vents(lineReader, seafloorSize);
        Seafloor seafloor = new Seafloor(seafloorSize[SEAFLOOR_XMIN], seafloorSize[SEAFLOOR_XMAX],
                seafloorSize[SEAFLOOR_YMIN], seafloorSize[SEAFLOOR_YMAX]);
        for (Vent vent : vents) {
            seafloor.addVent(vent);
        }
        return seafloor.ventOverlaps();
    }

    private static Collection<Vent> vents(BufferedReader lineReader, int[] seafloorSize) throws IOException {
        List<Vent> vents = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            String[] coords = line.split(" -> ");
            int x1 = xCoord(coords[0]);
            int y1 = yCoord(coords[0]);
            int x2 = xCoord(coords[1]);
            int y2 = yCoord(coords[1]);
            vents.add(new Vent(x1, y1, x2, y2));
            seafloorSize[SEAFLOOR_XMIN] = newMin(seafloorSize[SEAFLOOR_XMIN], x1, x2);
            seafloorSize[SEAFLOOR_YMIN] = newMin(seafloorSize[SEAFLOOR_YMIN], y1, y2);
            seafloorSize[SEAFLOOR_XMAX] = newMax(seafloorSize[SEAFLOOR_XMAX], x1, x2);
            seafloorSize[SEAFLOOR_YMAX] = newMax(seafloorSize[SEAFLOOR_YMAX], y1, y2);
        }
        return vents;
    }

    private static int xCoord(String coord) {
        return Integer.parseInt(coord.substring(0, coord.indexOf(',')));
    }

    private static int yCoord(String coord) {
        return Integer.parseInt(coord.substring(coord.indexOf(',') + 1));
    }

    private static int newMin(int cur, int new1, int new2) {
        return Math.min(cur, Math.min(new1, new2));
    }

    private static int newMax(int cur, int new1, int new2) {
        return Math.max(cur, Math.max(new1, new2));
    }
}
