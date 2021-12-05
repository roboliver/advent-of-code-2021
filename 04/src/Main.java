import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Winning bingo score: " + winningBingoScore(lineReader));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Last winning bingo score: " + lastWinningBingoScore(lineReader));
        }
    }

    public static int winningBingoScore(BufferedReader lineReader) throws IOException {
        List<Integer> drawOrder = drawOrder(lineReader);
        Collection<BingoBoard> boards = bingoBoards(lineReader);
        for (int number : drawOrder) {
            for (BingoBoard board : boards) {
                board.mark(number);
                if (board.gotBingo()) {
                    return board.getScore();
                }
            }
        }
        throw new IllegalStateException("no winners!");
    }

    public static int lastWinningBingoScore(BufferedReader lineReader) throws IOException {
        List<Integer> drawOrder = drawOrder(lineReader);
        Collection<BingoBoard> boards = bingoBoards(lineReader);
        for (int number : drawOrder) {
            Iterator<BingoBoard> boardIterator = boards.iterator();
            while (boardIterator.hasNext()) {
                BingoBoard board = boardIterator.next();
                board.mark(number);
                if (board.gotBingo()) {
                    if (boards.size() == 1) {
                        return board.getScore();
                    }
                    boardIterator.remove();
                }
            }
        }
        throw new IllegalStateException("not everyone's a winner!");
    }

    private static List<Integer> drawOrder(BufferedReader lineReader) throws IOException {
        String line = lineReader.readLine();
        String[] drawOrderArray = line.split(",");
        List<Integer> drawOrderList = new ArrayList<>();
        for (String draw : drawOrderArray) {
            drawOrderList.add(Integer.valueOf(draw));
        }
        return drawOrderList;
    }

    private static Collection<BingoBoard> bingoBoards(BufferedReader lineReader) throws IOException {
        List<BingoBoard> boards = new ArrayList<>();
        List<List<Integer>> board = null;
        String line;
        while ((line = lineReader.readLine()) != null) {
            boolean inBoardPrev = board != null;
            boolean inBoardCur = line.length() > 0;
            if (!inBoardPrev && inBoardCur) {
                // start of a new board
                board = new ArrayList<>();
                board.add(boardLine(line));
            } else if (inBoardPrev && !inBoardCur) {
                // end of a board, so add it to the list and reset
                boards.add(new BingoBoard(board));
                board = null;
            } else if (inBoardPrev && inBoardCur) {
                // new line in the same board
                board.add(boardLine(line));
            }
        }
        if (board != null) {
            // add the final board if we didn't already
            boards.add(new BingoBoard(board));
        }
        return boards;
    }

    private static List<Integer> boardLine(String line) {
        String[] boardLineArray = line.split(" +");
        List<Integer> boardLineList = new ArrayList<>();
        for (String num : boardLineArray) {
            if (num.length() > 0) { // exclude the empty string that will be created if the line starts with a space
                boardLineList.add(Integer.valueOf(num));
            }
        }
        return boardLineList;
    }
}
