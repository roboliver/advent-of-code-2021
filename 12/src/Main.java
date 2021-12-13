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
            // we've reached the end on this route -- it was valid unless we were meant to revisit a cave but didn't
            return toRevisit == null || doneRevisit ? 1 : 0;
        } else {
            int paths = 0;
            // the set of caves we've seen and won't revisit on this route, now including the current cave
            Set<Cave> seenNew = new HashSet<>(seen);
            if (cur.type() != Cave.Type.LARGE) {
                seenNew.add(cur);
            }
            for (Cave connected : cur.connected()) {
                if (!seenNew.contains(connected)) {
                    // this is a viable path, so include all the valid routes that span from it
                    paths += exploreCaves(connected, seenNew, allowSingleRevisit, toRevisit, doneRevisit || cur == toRevisit);
                }
            }
            if (allowSingleRevisit && toRevisit == null && cur.type() == Cave.Type.SMALL) {
                // we can do a single revisit, haven't already decided which cave we will revisit, and the current cave
                // is small, and so could be revisited -- so, additionally branch from this point to all routes that
                // would involve revisiting this cave
                for (Cave connected : cur.connected()) {
                    // note we use the original set of seen caves here, i.e. excluding the current cave, so that we will
                    // revisit this cave if we encounter it again
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
