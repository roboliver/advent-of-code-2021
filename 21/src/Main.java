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
        Player player1 = new Player(startingPos(lineReader));
        Player player2 = new Player(startingPos(lineReader));
        lineReader.close();
        GameState gameState = new GameState(player1, player2);
        int scoreToWin = 1000;
        while (!player1.hasWon(scoreToWin) && !player2.hasWon(scoreToWin)) {
            gameState = haveGoDeterministic(gameState, die, GameState.WhichPlayer.ONE);
            //printState(gameState.getPlayer1(), gameState.getPlayer2(), die);
            if (!gameState.getPlayer1().hasWon(scoreToWin)) {
                gameState = haveGoDeterministic(gameState, die, GameState.WhichPlayer.TWO);
                //printState(gameState.getPlayer1(), gameState.getPlayer2(), die);
            }
            player1 = gameState.getPlayer1();
            player2 = gameState.getPlayer2();
        }
        System.out.println("player 1 score: " + player1.getScore());
        System.out.println("player 2 score: " + player2.getScore());
        System.out.println("rolls: " + die.rollCount());
        return Math.min(player1.getScore(), player2.getScore()) * die.rollCount();
    }

    public static long maxWinnerWinsDirac(BufferedReader lineReader) throws IOException {
        DiracDie die = new DiracDie();
        Player player1 = new Player(startingPos(lineReader));
        Player player2 = new Player(startingPos(lineReader));
        lineReader.close();
        Map<GameState, Long> gameStates = new HashMap<>();
        gameStates.put(new GameState(player1, player2), 1L);
        long player1Wins = 0;
        long player2Wins = 0;
        while (!gameStates.isEmpty()) {
//            System.out.println("----------");
//            for (Map.Entry<GameState, Long> gameState : gameStates.entrySet()) {
//                System.out.println(stateToString(gameState.getKey().getPlayer1(), gameState.getKey().getPlayer2(), new DeterministicDie(7)) + ": " + gameState.getValue());
//            }
            System.out.println(gameStates.size());
            System.out.println("p1 wins: " + player1Wins);
            System.out.println("p2 wins: " + player2Wins);
            Map<GameState, Long> gameStatesNew = new HashMap<>();
            int scoreToWin = 21;
            for (Map.Entry<GameState, Long> gameState : gameStates.entrySet()) {
                Map<GameState, Long> nextGameStatesP1Go = gameState.getKey().haveGo(GameState.WhichPlayer.ONE, die);
                for (Map.Entry<GameState, Long> nextGameStateP1Go : nextGameStatesP1Go.entrySet()) {
                    if (nextGameStateP1Go.getKey().getPlayer1().hasWon(scoreToWin)) {
                        player1Wins += gameState.getValue() * nextGameStateP1Go.getValue();
                    } else {
                        Map<GameState, Long> nextGameStatesP2Go = nextGameStateP1Go.getKey().haveGo(GameState.WhichPlayer.TWO, die);
                        for (Map.Entry<GameState, Long> nextGameStateP2Go : nextGameStatesP2Go.entrySet()) {
                            if (nextGameStateP2Go.getKey().getPlayer2().hasWon(scoreToWin)) {
                                player2Wins += gameState.getValue() * nextGameStateP1Go.getValue() * nextGameStateP2Go.getValue();
                            } else {
                                gameStatesNew.merge(nextGameStateP2Go.getKey(), gameState.getValue() * nextGameStateP1Go.getValue() * nextGameStateP2Go.getValue(), Long::sum);
                            }
                        }
                    }
                }
            }
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
