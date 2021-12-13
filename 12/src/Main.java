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
        return exploreCaves(caveSystem, new HashSet<>(), !allowSingleRevisit);
    }

    private static int exploreCaves(Cave cur, Set<Cave> seen, boolean doneRevisit) {
        if (cur.isEnd()) {
            // we've reached the end -- this was a valid route
            return 1;
        } else {
            // the set of caves we've seen on this route, now including the current cave if applicable
            Set<Cave> seenNew = new HashSet<>(seen);
            if (!cur.isLarge()) {
                seenNew.add(cur);
            }
            int paths = 0;
            for (Cave connected : cur.connected()) {
                if (!seenNew.contains(connected)
                        || (!doneRevisit && !connected.isStart())) {
                    // we either haven't seen this cave, or we have seen it, but can revisit it, and haven't revisited
                    // a cave on this route yet -- so it's valid, and we should include all the routes that go through
                    // it from our current cave
                    paths += exploreCaves(connected, seenNew, doneRevisit || seenNew.contains(connected));
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
            if (first.isStart()) {
                start = first;
            } else if (second.isStart()) {
                start = second;
            }
        }
        if (start == null) {
            throw new IllegalArgumentException("no starting cave");
        }
        return start;
    }
}
