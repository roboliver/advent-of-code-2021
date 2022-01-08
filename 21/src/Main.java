import java.io.BufferedReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Losing score multiplied by die rolls: " + losingScoreTimesDieRolls(lineReader));
        }
    }

    public static int losingScoreTimesDieRolls(BufferedReader lineReader) throws IOException {
        DeterministicDie die = new DeterministicDie(100);
        Player player1 = new Player(startingPos(lineReader));
        Player player2 = new Player(startingPos(lineReader));
        lineReader.close();
        while (!player1.hasWon() && !player2.hasWon()) {
            player1.haveGo(die);
            if (!player1.hasWon()) {
                player2.haveGo(die);
            }
        }
        return Math.min(player1.getScore(), player2.getScore()) * die.rollCount();
    }

    private static int startingPos(BufferedReader lineReader) throws IOException {
        String line = lineReader.readLine();
        return Integer.parseInt(line.substring(line.indexOf(": ") + 2));
    }
}
