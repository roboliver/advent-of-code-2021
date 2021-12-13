import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Number of paths: " + countPaths(lineReader));
        }
//        try (BufferedReader lineReader = Utils.inputLineReader()) {
//            System.out.println("Number of paths, with revisiting: " + countPaths(lineReader));
//        }
    }

    public static int countPaths(BufferedReader lineReader) throws IOException {
        Cave caveSystem = caveSystem(lineReader);
        return exploreCaves(caveSystem, new HashSet<>());
    }

    private static int exploreCaves(Cave cur, Set<Cave> seen) {
        //System.out.println("we are in cave " + cur.name);
        //System.out.println("seen so far: [" + seen.stream().map(Cave::name).collect(Collectors.joining(",")) + "]");
        if (cur.isEnd()) {
            //System.out.println("reached the end.");
            return 1;
        } else {
            Set<Cave> seenNew = new HashSet<>(seen);
            if (!cur.isRevisitable()) {
                seenNew.add(cur);
                //System.out.println("can't revisit this cave. now seen: [" + seenNew.stream().map(Cave::name).collect(Collectors.joining(",")) + "]");
            }
            int paths = 0;
            //System.out.println("caves connected to " + cur.name + ": [" + cur.connected().stream().map(Cave::name).collect(Collectors.joining(",")) + "]");
            for (Cave connected : cur.connected()) {
                //System.out.println("checking connected cave " + connected.name);
                if (!seenNew.contains(connected)) {
                    //System.out.println("we can enter it.");
                    int result = exploreCaves(connected, seenNew);
                    //System.out.println("we got " + result);
                    paths += result; //exploreCaves(connected, seenNew);
                }
            }
            //System.out.println("from cave " + cur.name + ", we found " + paths + " paths.");
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
            //System.out.println("first is: " + names[0] + ", second is: " + names[1]);
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
