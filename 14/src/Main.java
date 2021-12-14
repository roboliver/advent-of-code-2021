import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Most common minus least common, after 10 steps: " + rangeAfterNSteps(lineReader, 10));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("After 40 steps: " + rangeAfterNSteps(lineReader, 40));
        }
    }

    public static long rangeAfterNSteps(BufferedReader lineReader, int steps) throws IOException {
        // array we will use to store the first and last elements, which our pairwise counts will be one too low for,
        // since these elements only appear as part of one pair not two
        char[] ends = new char[2];
        Map<CharPair, Long> polymer = template(lineReader, ends);
        Map<CharPair, Character> insertionRules = insertionRules(lineReader);
        polymer = executeSteps(polymer, insertionRules, steps);
        Map<Character, Long> elemCounts = countElements(polymer, ends[0], ends[1]);
        long min = elemCounts.values().stream().reduce(Long.MAX_VALUE, Long::min);
        long max = elemCounts.values().stream().reduce(Long.MIN_VALUE, Long::max);
        return max - min;
    }

    private static Map<CharPair, Long> executeSteps(Map<CharPair, Long> polymer, Map<CharPair, Character> insertionRules, int steps) {
        for (int step = 0; step < steps; step++) {
            Map<CharPair, Long> polymerNew = new HashMap<>();
            for (Map.Entry<CharPair, Long> pairCount : polymer.entrySet()) {
                CharPair pair = pairCount.getKey();
                long count = pairCount.getValue();
                Character insert = insertionRules.get(pair);
                if (insert == null) {
                    // no rule for this pair, so just reinsert it -- note that the sample and input don't include this
                    // scenario, but I'm handling it anyway, just for fun
                    polymerNew.merge(pair, count, Long::sum);
                } else {
                    // split the pair into the resultant pairs that are created by inserting the element in the middle
                    CharPair newFirst = new CharPair(pair.first(), insert);
                    CharPair newSecond = new CharPair(insert, pair.second());
                    polymerNew.merge(newFirst, count, Long::sum);
                    polymerNew.merge(newSecond, count, Long::sum);
                }
            }
            polymer = polymerNew;
        }
        return polymer;
    }

    private static Map<Character, Long> countElements(Map<CharPair, Long> polymer, char start, char end) {
        // count the elements from the pairs, but note that the values we get from this are twice as high as they ought
        // to be, since each element appears in two pairs, as the first element of one and the last of another -- we
        // will correct this afterwards
        Map<Character, Long> elemCountsDouble = new HashMap<>();
        for (Map.Entry<CharPair, Long> pairCount : polymer.entrySet()) {
            elemCountsDouble.merge(pairCount.getKey().first(), pairCount.getValue(), Long::sum);
            elemCountsDouble.merge(pairCount.getKey().second(), pairCount.getValue(), Long::sum);
        }
        elemCountsDouble.merge(start, 1L, Long::sum);
        elemCountsDouble.merge(end, 1L, Long::sum);
        // afterwards
        return halveCounts(elemCountsDouble);
    }

    private static Map<Character, Long> halveCounts(Map<Character, Long> counts) {
        return counts.entrySet()
                .stream()
                .map(count -> new AbstractMap.SimpleEntry<>(count.getKey(), count.getValue() / 2))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Map<CharPair, Long> template(BufferedReader lineReader, char[] ends) throws IOException {
        String line = lineReader.readLine();
        char[] elems = line.toCharArray();
        Map<CharPair, Long> template = new HashMap<>();
        for (int i = 0; i < elems.length - 1; i++) {
            CharPair pair = new CharPair(elems[i], elems[i + 1]);
            template.merge(pair, 1L, Long::sum);
        }
        ends[0] = elems[0];
        ends[1] = elems[elems.length - 1];
        return template;
    }

    private static Map<CharPair, Character> insertionRules(BufferedReader lineReader) throws IOException {
        Map<CharPair, Character> insertionRules = new HashMap<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            if (!line.isBlank()) {
                String[] parts = line.split(" -> ");
                CharPair pair = new CharPair(parts[0].charAt(0), parts[0].charAt(1));
                char insert = parts[1].charAt(0);
                insertionRules.put(pair, insert);
            }
        }
        lineReader.close();
        return insertionRules;
    }
}
