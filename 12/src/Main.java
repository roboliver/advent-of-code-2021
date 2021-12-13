import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Number of paths: " + countPaths(lineReader, false));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Number of paths, with revisiting: " + countPaths(lineReader, true));
        }
    }

    public static int countPaths(BufferedReader lineReader, boolean allowSingleRevisit) throws IOException {
        Cave caveSystem = caveSystem(lineReader);
        return exploreCaves(caveSystem, new HashSet<>(), allowSingleRevisit, null, false);
    }

    private static int exploreCaves(Cave cur, Set<Cave> seen, boolean allowSingleRevisit, Cave toRevisit, boolean doneRevisit) {
        if (cur.type() == Cave.Type.END) {
            Set<Cave> seenNew = new HashSet<>(seen);
            seenNew.add(cur);
            return toRevisit == null || doneRevisit ? 1 : 0;
        } else {
            Set<Cave> seenNew = new HashSet<>(seen);
            if (cur.type() != Cave.Type.LARGE) {
                seenNew.add(cur);
            }
            int paths = 0;
            for (Cave connected : cur.connected()) {
                if (!seenNew.contains(connected)) {
                    paths += exploreCaves(connected, seenNew, allowSingleRevisit, toRevisit, doneRevisit || cur == toRevisit);
                }
            }
            if (allowSingleRevisit && toRevisit == null && cur.type() == Cave.Type.SMALL) {
                for (Cave connected : cur.connected()) {
                    if (!seen.contains(connected)) {
                        paths += exploreCaves(connected, seen, true, cur, false);
                    }
                }
            }
            return paths;
        }
    }

    private static Cave caveSystem(BufferedReader lineReader) throws IOException {
        Cave start = null;
        Map<String, Cave> caves = new HashMap<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            String[] names = line.split("-");
            Cave first = caves.computeIfAbsent(names[0], k -> new Cave(names[0]));
            Cave second = caves.computeIfAbsent(names[1], k -> new Cave(names[1]));
            first.connect(second);
            if (first.type() == Cave.Type.START) {
                start = first;
            } else if (second.type() == Cave.Type.START) {
                start = second;
            }
        }
        if (start == null) {
            throw new IllegalArgumentException("no starting cave");
        }
        return start;
    }
}
