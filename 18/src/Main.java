import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Snailfish sum magnitude: " + snailfishSumMagnitude(lineReader));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Largest magnitude from sum of two snailfish numbers: "
                    + largestMagnitudeFromTwoSum(lineReader));
        }
    }

    public static int snailfishSumMagnitude(BufferedReader lineReader) throws IOException {
        return snailfishSum(lineReader).getMagnitude();
    }

    public static int largestMagnitudeFromTwoSum(BufferedReader lineReader) throws IOException {
        List<Element> snailfishNumbers = snailfishNumbers(lineReader);
        int max = 0;
        for (int i = 0; i < snailfishNumbers.size(); i++) {
            for (int j = 0; j < snailfishNumbers.size(); j++) {
                if (i != j) {
                    // duplicate the numbers before summing them, so we can reuse them afterwards
                    Element first = snailfishNumbers.get(i).duplicate();
                    Element second = snailfishNumbers.get(j).duplicate();
                    max = Math.max(max, addSnailfishNumbers(first, second).getMagnitude());
                }
            }
        }
        return max;
    }

    public static Element snailfishSum(BufferedReader lineReader) throws IOException {
        List<Element> snailfishNumbers = snailfishNumbers(lineReader);
        Element sum = snailfishNumbers.get(0);
        for (int i = 1; i < snailfishNumbers.size(); i++) {
            sum = addSnailfishNumbers(sum, snailfishNumbers.get(i));
        }
        return sum;
    }

    public static Element snailfishNumber(String line) {
        ArrayDeque<Element> stack = new ArrayDeque<>();
        int depth = 0;
        for (char c : line.toCharArray()) {
            if (c == '[') {
                depth++;
            } else if (c == ']') {
                depth--;
                Element right = stack.pop();
                Element left = stack.pop();
                Pair pair = new Pair(left, right, depth);
                stack.push(pair);
            } else if (Character.isDigit(c)) {
                stack.push(new RegularNumber(Character.digit(c, 10), depth));
            }
        }
        return stack.pop();
    }

    public static Element addSnailfishNumbers(Element left, Element right) {
        left.increaseDepth();
        right.increaseDepth();
        Pair sum = new Pair(left, right, 0);
        boolean reduced = false;
        while (!reduced) {
            boolean exploded = sum.checkExplode();
            if (!exploded) {
                boolean split = sum.checkSplit();
                if (!split) {
                    reduced = true;
                }
            }
        }
        return sum;
    }

    private static List<Element> snailfishNumbers(BufferedReader lineReader) throws IOException {
        List<Element> snailfishNumbers = new ArrayList<>();
        String line;
        while ((line = lineReader.readLine()) != null) {
            snailfishNumbers.add(snailfishNumber(line));
        }
        lineReader.close();
        return snailfishNumbers;
    }
}
