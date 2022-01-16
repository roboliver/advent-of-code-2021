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
        GameState gameState = new GameState(player1, player2);
        while (!player1.hasWon() && !player2.hasWon()) {
            gameState = haveGoDeterministic(gameState, die, GameState.WhichPlayer.ONE);
            if (!gameState.getPlayer1().hasWon()) {
                gameState = haveGoDeterministic(gameState, die, GameState.WhichPlayer.TWO);
            }
            player1 = gameState.getPlayer1();
            player2 = gameState.getPlayer2();
        }
        System.out.println("player 1 score: " + player1.getScore());
        System.out.println("player 2 score: " + player2.getScore());
        System.out.println("rolls: " + die.rollCount());
        return Math.min(player1.getScore(), player2.getScore()) * die.rollCount();
    }

    private static GameState haveGoDeterministic(GameState gameState, Die die, GameState.WhichPlayer whichPlayer) {
        return gameState.haveGo(whichPlayer, die)
                .entrySet()
                .stream()
                .findFirst()
                .get()
                .getKey();
    }

    private static int startingPos(BufferedReader lineReader) throws IOException {
        String line = lineReader.readLine();
        return Integer.parseInt(line.substring(line.indexOf(": ") + 2));
    }
}
