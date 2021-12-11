import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Main {
    /**
     * Enum used to represent navigation system character pairs. Each element is a pair of left and right characters,
     * and the corrupted and autocomplete scores it results in when it causes the respective errors.
     */
    private enum NavPair {
        PARENTHESIS('(', ')', 3, 1),
        SQUARE_BRACKET('[', ']', 57, 2),
        BRACE('{', '}', 1197, 3),
        CHEVRON('<', '>', 25137, 4);

        private final char left;
        private final char right;
        private final long corruptedScore;
        private final long autocompleteScore;
        NavPair(char left, char right, int corruptedScore, int autocompleteScore) {
            this.left = left;
            this.right = right;
            this.corruptedScore = corruptedScore;
            this.autocompleteScore = autocompleteScore;
        }

        public static NavPair fromLeft(char left) {
            for (NavPair navPair : values()) {
                if (navPair.left == left) {
                    return navPair;
                }
            }
            return null;
        }

        public static NavPair fromRight(char right) {
            for (NavPair navPair : values()) {
                if (navPair.right == right) {
                    return navPair;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Corrupted lines error score: " + errorScore(lineReader, true));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Incomplete lines error score: " + errorScore(lineReader, false));
        }
    }

    public static long errorScore(BufferedReader lineReader, boolean corruptedNotIncomplete) throws IOException {
        List<Long> scores = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            Deque<NavPair> stack = new ArrayDeque<>();
            boolean lineCorrupted = false;
            for (char c : line.toCharArray()) {
                NavPair npLeft = NavPair.fromLeft(c);
                if (npLeft != null) {
                    // an opening character, so push it and proceed to the next character
                    stack.push(npLeft);
                } else {
                    // a closing character, so check whether it fits the one on top of the stack
                    npLeft = stack.pop();
                    NavPair npRight = NavPair.fromRight(c);
                    if (npLeft != npRight) {
                        lineCorrupted = true;
                        if (corruptedNotIncomplete) {
                            scores.add(npRight.corruptedScore);
                        }
                        // give up on the line now -- we've either added the corrupted score, or need to skip this line
                        // if we're looking for the autocomplete score
                        break;
                    }
                }
            }
            if (!corruptedNotIncomplete && !lineCorrupted) {
                // we are looking for the autocomplete score, and this line isn't corrupted, so we should include it
                long autocompleteScore = 0;
                while (!stack.isEmpty()) {
                    NavPair np = stack.pop();
                    autocompleteScore *= 5;
                    autocompleteScore += np.autocompleteScore;
                }
                scores.add(autocompleteScore);
            }
        }
        if (corruptedNotIncomplete) {
            return scores.stream()
                    .reduce(0L, Long::sum);
        } else {
            Collections.sort(scores);
            return scores.get(scores.size() / 2);
        }
    }
}