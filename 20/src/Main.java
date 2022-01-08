import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final char PIXEL_LIGHT = '#';
    private static final char PIXEL_DARK = '.';

    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("pixels lit after 2 enhances: " + pixelsLitAfterEnhance(lineReader, 2));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("pixels lit after 50 enhances: " + pixelsLitAfterEnhance(lineReader, 50));
        }
    }

    public static int pixelsLitAfterEnhance(BufferedReader lineReader, int iterations) throws IOException {
        int[] algorithm = algorithm(lineReader);
        int[][] image = image(lineReader);
        int background = 0;
        for (int i = 0; i < iterations; i++) {
            image = enhance(algorithm, image, background);
            background = backgroundRecalc(background, algorithm);
        }
        int sum = 0;
        for (int row = 0; row < image.length; row++) {
            for (int col = 0; col < image[row].length; col++) {
                sum += image[row][col];
            }
        }
        return sum;
    }

    private static int[][] enhance(int[] algorithm, int[][] image, int background) {
        int[][] imageEnhanced = new int[image.length + 2][image[0].length + 2];
        for (int row = 0; row < imageEnhanced.length; row++) {
            for (int col = 0; col < imageEnhanced[row].length; col++) {
                int algorithmPos = 0;
                for (int focusRow = row - 1; focusRow < row + 2; focusRow++) {
                    for (int focusCol = col - 1; focusCol < col + 2; focusCol++) {
                        int pixel;
                        if (focusRow > 0 && focusRow <= image.length
                                && focusCol > 0 && focusCol <= image[0].length) {
                            pixel = image[focusRow - 1][focusCol - 1];
                        } else {
                            pixel = background;
                        }
                        algorithmPos *= 2;
                        algorithmPos += pixel;
                    }
                }
                imageEnhanced[row][col] = algorithm[algorithmPos];
            }
        }
        return imageEnhanced;
    }

    private static int backgroundRecalc(int background, int[] algorithm) {
        return (background == 0) ? algorithm[0] : algorithm[algorithm.length - 1];
    }

    private static int[] algorithm(BufferedReader lineReader) throws IOException {
        return lineParse(lineReader.readLine());
    }

    private static int[][] image(BufferedReader lineReader) throws IOException {
        lineReader.readLine();
        String line;
        List<int[]> imageList = new ArrayList<>();
        while ((line = lineReader.readLine()) != null) {
            int[] lineParsed = lineParse(line);
            imageList.add(lineParsed);
        }
        int[][] image = new int[imageList.size()][];
        for (int i = 0; i < imageList.size(); i++) {
            image[i] = imageList.get(i);
        }
        lineReader.close();
        return image;
    }

    private static int[] lineParse(String line) {
        int[] lineParsed = new int[line.length()];
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == PIXEL_LIGHT) {
                lineParsed[i] = 1;
            } else if (line.charAt(i) == PIXEL_DARK) {
                lineParsed[i] = 0;
            } else {
                throw new IllegalStateException("lines must contain only # and . chars");
            }
        }
        return lineParsed;
    }

    private static String imageToString(int[][] image) {
        StringBuilder buf = new StringBuilder();
        for (int row = 0; row < image.length; row++) {
            if (row > 0) {
                buf.append('\n');
            }
            for (int col = 0; col < image[row].length; col++) {
                buf.append(image[row][col] == 1 ? PIXEL_LIGHT : PIXEL_DARK);
            }
        }
        return buf.toString();
    }
}
