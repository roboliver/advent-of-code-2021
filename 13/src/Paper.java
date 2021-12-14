import java.util.*;

public class Paper {
    private final Set<Point> dots = new HashSet<>();
    private int width = Integer.MIN_VALUE;
    private int height = Integer.MIN_VALUE;

    public void addDot(Point dot) {
        dots.add(Objects.requireNonNull(dot));
        width = Math.max(width, dot.x() + 1);
        height = Math.max(height, dot.y() + 1);
    }

    public int dotCount() {
        return dots.size();
    }

    public void fold(Fold fold) {
        Set<Point> movedDots = new HashSet<>();
        Iterator<Point> iter = dots.iterator();
        while (iter.hasNext()) {
            Point dot = iter.next();
            if (isPastFold(dot, fold)) {
                movedDots.add(reflect(dot, fold));
                iter.remove();
            }
        }
        dots.addAll(movedDots);
        if (fold.isXAxis()) {
            width = fold.position();
        } else {
            height = fold.position();
        }
    }

    private static boolean isPastFold(Point dot, Fold fold) {
        return fold.isXAxis() && dot.x() > fold.position()
                || fold.isYAxis() && dot.y() > fold.position();
    }

    private static Point reflect(Point dot, Fold fold) {
        if (fold.isXAxis()) {
            return new Point(fold.position() - (dot.x() - fold.position()), dot.y());
        } else {
            return new Point(dot.x(), fold.position() - (dot.y() - fold.position()));
        }
    }

    public String toOutput() {
        char[][] chars = toCharArray();
        StringBuilder buf = new StringBuilder();
        for (int row = 0; row < chars.length; row++) {
            if (row > 0) {
                buf.append('\n');
            }
            for (char c : chars[row]) {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    private char[][] toCharArray() {
        char[][] chars = new char[height][width];
        for (int row = 0; row < chars.length; row++) {
            for (int col = 0; col < chars[row].length; col++) {
                Point dot = new Point(col, row);
                chars[row][col] = dots.contains(dot) ? '#' : '.';
            }
        }
        return chars;
    }
}
