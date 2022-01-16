import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Losing score multiplied by die rolls (deterministic): " + losingScoreTimesDieRollsDeterministic(lineReader));
        }
        try (BufferedReader lineReader = Utils.inputLineReader()) {
            System.out.println("Max winner wins (Dirac): " + maxWinnerWinsDirac(lineReader));
        }
    }

    public static int losingScoreTimesDieRollsDeterministic(BufferedReader lineReader) throws IOException {
        DeterministicDie die = new DeterministicDie(100);
        Player player1Init = new Player(startingPos(lineReader));
        Player player2Init = new Player(startingPos(lineReader));
        lineReader.close();
        int scoreToWin = 1000;
        GameState gameState = new GameState(player1Init, player2Init);
        GameState.WhichPlayer currentPlayer = GameState.WhichPlayer.ONE;
        while (!currentPlayer.other().getPlayer(gameState).hasWon(scoreToWin)) {
            gameState = haveGoDeterministic(gameState, die, currentPlayer);
            currentPlayer = currentPlayer.other();
        }
        return Math.min(gameState.getPlayer1().getScore(), gameState.getPlayer2().getScore()) * die.rollCount();
    }

    public static long maxWinnerWinsDirac(BufferedReader lineReader) throws IOException {
        DiracDie die = new DiracDie();
        Player player1 = new Player(startingPos(lineReader));
        Player player2 = new Player(startingPos(lineReader));
        lineReader.close();
        int scoreToWin = 21;
        Map<GameState, Long> gameStates = new HashMap<>();
        gameStates.put(new GameState(player1, player2), 1L);
        long player1Wins = 0;
        long player2Wins = 0;
        GameState.WhichPlayer currentPlayer = GameState.WhichPlayer.ONE;
        while (!gameStates.isEmpty()) {
            Map<GameState, Long> gameStatesNew = new HashMap<>();
            for (Map.Entry<GameState, Long> gameState : gameStates.entrySet()) {
                if (currentPlayer.other().getPlayer(gameState.getKey()).hasWon(scoreToWin)) {
                    if (currentPlayer == GameState.WhichPlayer.ONE) {
                        player1Wins += gameState.getValue();
                    } else {
                        player2Wins += gameState.getValue();
                    }
                } else {
                    Map<GameState, Long> nextGameStates = gameState.getKey().haveGo(currentPlayer, die);
                    for (Map.Entry<GameState, Long> nextGameState : nextGameStates.entrySet()) {
                        gameStatesNew.merge(nextGameState.getKey(),
                                gameState.getValue() * nextGameState.getValue(), Long::sum);
                    }
                }
            }
            currentPlayer = currentPlayer.other();
            gameStates = gameStatesNew;
        }
        return Math.max(player1Wins, player2Wins);
    }



    private static String stateToString(Player player1, Player player2, DeterministicDie die) {
        return "p1=" + player1.getScore() + ",p2=" + player2.getScore() + ",rolls=" + die.rollCount();
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
