import java.util.*;

/**
 * Class used to decode seven-segment display inputs. An instance of this class is constructed with the full set of
 * encoded signal patterns for digits 0-9, and builds an internal mapping that lets it convert encoded digit strings
 * into the decoded int that the string represents.
 *
 * The segments that correspond to each decoded character, for reference:
 *
 *     aaaa
 *    b    c
 *    b    c
 *     dddd
 *    e    f
 *    e    f
 *     gggg
 *
 */
public class SevenSegmentDecoder {
    private static final Set<Character> VALID_CHARS = Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
    private final Map<Set<Character>, Integer> decoder;

    public SevenSegmentDecoder(String[] signalPatterns) {
        if (signalPatterns.length != 10) {
            throw new IllegalArgumentException("signal patterns must contain exactly 10 entries, one for each digit");
        }
        this.decoder = createDecoder(signalPatterns);
    }

    private static Map<Set<Character>, Integer> createDecoder(String[] signalPatterns) {
        // map from number of instances of each char a-g across all patterns, to the chars with that instance count
        Map<Integer, List<Character>> charsByCount = charsByCount(signalPatterns);
        // map from pattern sizes to the patterns of that size
        Map<Integer, List<String>> patternsBySize = patternsBySize(signalPatterns);
        // get the chars we can identify by their counts being unique: b (6 instances) and f (9 instances)
        char b = charsByCount.get(6).get(0);
        char f = charsByCount.get(9).get(0);
        // get the rest of the chars by removing chars we already know from patterns where only one char is unknown
        char c = remainingChar(patternsBySize.get(2), f);
        char a = remainingChar(patternsBySize.get(3), c, f);
        char d = remainingChar(patternsBySize.get(4), b, c, f);
        char g = remainingChar(patternsBySize.get(5), a, c, d, f);
        char e = remainingChar(patternsBySize.get(7), a, b, c, d, f, g);
        // add all the decoded digits
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

    private static Map<Integer, List<Character>> charsByCount(String[] signalPatterns) {
        // first build a map from each char to its count, then reverse and return it
        Map<Character, Integer> charCounts = new HashMap<>();
        for (String pattern : signalPatterns) {
            for (char c : pattern.toCharArray()) {
                if (!VALID_CHARS.contains(c)) {
                    throw new IllegalArgumentException("patterns must contain only characters a-g");
                }
                Integer count = charCounts.get(c);
                if (count == null) {
                    count = 0;
                }
                charCounts.put(c, count + 1);
            }
        }
        return charCountsReverse(charCounts);
    }

    private static Map<Integer, List<Character>> charCountsReverse(Map<Character, Integer> charCounts) {
        // convert chars->counts to counts->chars
        Map<Integer, List<Character>> charsByCount = new HashMap<>();
        for (Map.Entry<Character, Integer> charCount : charCounts.entrySet()) {
            List<Character> charsWithCount = charsByCount.computeIfAbsent(charCount.getValue(), k -> new ArrayList<>());
            charsWithCount.add(charCount.getKey());
        }
        return charsByCount;
    }

    private static Map<Integer, List<String>> patternsBySize(String[] signalPatterns) {
        Map<Integer, List<String>> patternsBySize = new HashMap<>();
        for (String pattern : signalPatterns) {
            int size = pattern.length();
            List<String> patternsWithSize = patternsBySize.computeIfAbsent(size, k -> new ArrayList<>());
            patternsWithSize.add(pattern);
        }
        return patternsBySize;
    }

    private static char remainingChar(Collection<String> patterns, char... knownCharsArray) {
        for (String pattern : patterns) {
            Set<Character> knownChars = new HashSet<>();
            for (char c : knownCharsArray) {
                knownChars.add(c);
            }
            int unknownCharIndex = -1; // the index of the single unknown char in the pattern
            char[] patternChars = pattern.toCharArray();
            for (int i = 0; i < patternChars.length; i++) {
                if (!knownChars.contains(patternChars[i])) {
                    if (unknownCharIndex != -1) {
                        // we have already found an unknown char in this pattern, so we can't narrow it down to a single
                        // one -- reset to -1 so we skip this pattern and try again with the next one
                        unknownCharIndex = -1;
                        break;
                    }
                    unknownCharIndex = i;
                }
            }
            if (unknownCharIndex != -1) {
                return patternChars[unknownCharIndex];
            }
        }
        throw new IllegalStateException("none of the patterns had all the provided chars bar one");
    }

    /**
     * Takes a digit encoded in the SSD's encoding, and returns the decoded digit as an int.
     * @param encodedDigit The encoded digit string
     * @return The decoded digit int
     */
    public int decode(String encodedDigit) {
        Set<Character> digitChars = new HashSet<>();
        for (char c : encodedDigit.toCharArray()) {
            digitChars.add(c);
        }
        return decoder.get(digitChars);
    }
}
