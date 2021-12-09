import java.util.*;

public class SevenSegmentDecoder {
    private static final Set<Character> VALID_CHARS = Collections.unmodifiableSet(
            Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g')
    );
    private final Map<Set<Character>, Integer> decoder;

    public SevenSegmentDecoder(String[] signalPatterns) {
        if (signalPatterns.length != 10) {
            throw new IllegalArgumentException("signal patterns must contain exactly 10 entries, one for each digit");
        }
        this.decoder = createDecoder(signalPatterns);
    }

    private static Map<Set<Character>, Integer> createDecoder(String[] signalPatterns) {
        // map from chars a-g to the number of instances of them across all patterns
        Map<Character, Integer> charCounts = new HashMap<>();
        // reverse of the above map: so, from counts to chars with that count
        Map<Integer, List<Character>> charsByCount;
        // map from pattern sizes to the patterns of that size
        Map<Integer, List<String>> patternsBySize = new HashMap<>();
        for (String pattern : signalPatterns) {
            charCountsUpdate(charCounts, pattern);
            patternsBySizeUpdate(patternsBySize, pattern);
        }
        charsByCount = charCountsReverse(charCounts);
        // get the chars we can identify by their counts being unique: b (6 instances) and f (9 instances)
        char b = charsByCount.get(6).get(0);
        char f = charsByCount.get(9).get(0);
        // get the rest of the chars by removing chars we already know from patterns where only one char is unknown
        char c = remainingChar(patternsBySize.get(2), f);
        char a = remainingChar(patternsBySize.get(3), c, f);
        char d = remainingChar(patternsBySize.get(4), b, c, f);
        char g = remainingChar(patternsBySize.get(6), a, b, c, d, f);
        char e = remainingChar(patternsBySize.get(7), a, b, c, d, f, g);
        //
        Map<Set<Character>, Integer> decoder = new HashMap<>();
        decoder.put(Set.of(a, b, c, e, f, g), 0);
        decoder.put(Set.of(c, f), 1);
        decoder.put(Set.of(a, c, d, e, g), 2);
        decoder.put(Set.of(a, c, d, f, g), 3);
        decoder.put(Set.of(b, c, d, f), 4);
        decoder.put(Set.of(a, b, d, f, g), 5);
        decoder.put(Set.of(a, b, d, e, f, g), 6);
        decoder.put(Set.of(a, c, f), 7);
        decoder.put(Set.of(a, b, c, d, e, f, g), 8);
        decoder.put(Set.of(a, b, c, d, f, g), 9);
        return decoder;
    }

    private static void charCountsUpdate(Map<Character, Integer> charCountsReverse, String pattern) {
        for (char c : pattern.toCharArray()) {
            if (!VALID_CHARS.contains(c)) {
                throw new IllegalArgumentException("patterns must contain only characters a-g");
            }
            Integer count = charCountsReverse.get(c);
            if (count == null) {
                count = 0;
            }
            charCountsReverse.put(c, count + 1);
        }
    }

    private static void patternsBySizeUpdate(Map<Integer, List<String>> segCounts, String pattern) {
        int segCount = pattern.length();
        List<String> patternsWithCount = segCounts.get(segCount);
        if (patternsWithCount == null) {
            patternsWithCount = new ArrayList<>();
            segCounts.put(segCount, patternsWithCount);
        }
        patternsWithCount.add(pattern);
    }

    private static Map<Integer, List<Character>> charCountsReverse(Map<Character, Integer> charCountsReverse) {
        Map<Integer, List<Character>> charCounts = new HashMap<>();
        for (Map.Entry<Character, Integer> charCountReverse : charCountsReverse.entrySet()) {
            List<Character> charsWithCount = charCounts.get(charCountReverse.getValue());
            if (charsWithCount == null) {
                charsWithCount = new ArrayList<>();
                charCounts.put(charCountReverse.getValue(), charsWithCount);
            }
            charsWithCount.add(charCountReverse.getKey());
        }
        return charCounts;
    }

    private static char remainingChar(Collection<String> patterns, char... removeCharsArray) {
        for (String pattern : patterns) {
            Set<Character> removeChars = new HashSet<>();
            for (char c : removeCharsArray) {
                removeChars.add(c);
            }
            int iRemaining = -1;
            char[] chars = pattern.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (!removeChars.contains(c)) {
                    if (iRemaining != -1) {
                        iRemaining = -1;
                        break;
                    }
                    iRemaining = i;
                }
            }
            if (iRemaining != -1) {
                return chars[iRemaining];
            }
        }
        throw new IllegalStateException("none of the patterns had all the provided chars bar one");
    }

    public int decode(String encodedDigit) {
        Set<Character> digitChars = new HashSet<>();
        for (char c : encodedDigit.toCharArray()) {
            digitChars.add(c);
        }
        return decoder.get(digitChars);
    }
}
