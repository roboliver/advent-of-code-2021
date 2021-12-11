import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private enum NavChar {
        PARENTHESIS('(', ')', 3, 1),
        SQUARE_BRACKET('[', ']', 57, 2),
        BRACE('{', '}', 1197, 3),
        CHEVRON('<', '>', 25137, 4);

        private final char left;
        private final char right;
        private final long corruptedScore;
        private final long autocompleteScore;
        NavChar(char left, char right, int corruptedScore, int autocompleteScore) {
            this.left = left;
            this.right = right;
            this.corruptedScore = corruptedScore;
            this.autocompleteScore = autocompleteScore;
        }

        public static NavChar fromLeft(char left) {
            for (NavChar navChar : values()) {
                if (navChar.left == left) {
                    return navChar;
                }
            }
            return null;
        }

        public static NavChar fromRight(char right) {
            for (NavChar navChar : values()) {
                if (navChar.right == right) {
                    return navChar;
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
        line:
        while ((line = lineReader.readLine()) != null) {
            Deque<NavChar> stack = new ArrayDeque<>();
            for (char c : line.toCharArray()) {
                NavChar ncLeft = NavChar.fromLeft(c);
                if (ncLeft != null) {
                    stack.push(ncLeft);
                } else {
                    ncLeft = stack.pop();
                    NavChar ncRight = NavChar.fromRight(c);
                    if (ncLeft != ncRight) {
                        if (corruptedNotIncomplete) {
                            scores.add(ncRight.corruptedScore);
                        }
                        continue line;
                    }
                }
            }
            if (!corruptedNotIncomplete) {
                long autocompleteScore = 0;
                while (!stack.isEmpty()) {
                    NavChar ncRem = stack.pop();
                    autocompleteScore *= 5;
                    autocompleteScore += ncRem.autocompleteScore;
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
