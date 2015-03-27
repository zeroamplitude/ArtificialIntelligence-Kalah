package algorithm;

import java.util.*;

/**
 * Player:
 * <brief description of class>
 */
public class Player {

    /**
     * An array of integers that represent the current board state
     */
    private Stack<int[]> board;

    private Stack<Integer> turn;

    private int playerID;

    private GameSimulator game;

    // An 2D array that holds game outcomes of each move
    Map<Integer, Integer[]> scores;

    public Player(int playerID) {
        this.playerID = playerID;
        this.game = new GameSimulator();
        this.board = new Stack<int[]>();
        this.turn = new Stack<Integer>();
        this.scores = new HashMap<Integer, Integer[]>();
    }

    public int makePlay(int[] board) {
        // clear the scores
        scores.clear();

        Queue<Integer> moves = getMoves(playerID, board);
        for (Integer i : moves)
           this.scores.put(i, new Integer[]{0, 0, 0, 0});

        this.board.push(board);
        this.turn.push(playerID);

        // simulate game
        simulateGame(moves);

        // calculate the best move
        int move = calcBestMove(scores);
        return move;
    }

    public void simulateGame(Queue<Integer> moves) {

        Integer move;
        while ((move = moves.poll()) != null) {

            System.out.println("Player:" + turn.peek() + " Move:" + (move + 1));
            // simulate the move
            int nextTurn = simulateMove(move, turn.peek(), this.board.peek());

            int[] b = game.getBoard();
            // Base Case: game over
            if (nextTurn == 0 || board.peek()[6] > 18 || board.peek()[13] > 18) {

                // count game
                scores.get(move)[0] += 1; // increment games

                // get score of game
                if (b[6] > b[13])
                    scores.get(move)[1] += 1; // player 1 wins
                else if (b[13] > b[6])
                    scores.get(move)[2] += 1; // player 2 wins
                else
                    scores.get(move)[3] += 1; // tie

                this.board.pop();
                this.turn.pop();

            } else {
                Queue<Integer> tmpMoves = getMoves(nextTurn, board.peek());

                board.push(b.clone());
                turn.push(nextTurn);

                // recursively call simGame until complete
                simulateGame(tmpMoves);

            }
        }
    }

    public Queue<Integer> getMoves(int player, int[] board) {
        Queue<Integer> moves = new LinkedList<Integer>();
        for (int i = 0; i < 6; i++) {
            int pieceIndex = i;
            // adjust if player 2
            if (player == 2)
                pieceIndex += (6 - i) * 2;

            // check if the piece has seeds
            if (board[pieceIndex] > 0)
                moves.add(i); // add the piece to potential moves
        }
        return moves;
    }

    public int simulateMove(int move, int turn, int[] board) {
        game.setGameState(board, turn);
        System.out.println("before");
        game.printBoard();
        int nextTurn = game.move(move, turn);
        System.out.println("after");
        game.printBoard();
        return nextTurn;
    }

    public int calcBestMove(Map<Integer, Integer[]> scores) {
        double bestRatio = 0;
        int bestMove = 0;
        for (int i : scores.keySet()) {
            double tieRatio = (((double)scores.get(i)[3]) / (double)scores.get(i)[0]);
            double pl1WinRatio = (((double)scores.get(i)[1]) / (double)scores.get(i)[0]) + tieRatio/2; // tie 50% chance
            double pl2WinRatio = (((double)scores.get(i)[2]) / (double)scores.get(i)[0]) + tieRatio/2; // tie 50% chance

            System.out.printf("P1Wins:%d, P2Wins:%d Ties:%d Total#Games:%d\n", scores.get(i)[1], scores.get(i)[2], scores.get(i)[3], scores.get(i)[0]);

            System.out.println(playerID);

            if (playerID == 1) {
                if (pl1WinRatio > bestRatio) {
                    bestRatio = pl1WinRatio;
                    bestMove = i;
                }
                System.out.println(playerID + " " + (i + 1) + " Win Ratio: " + pl1WinRatio);
            } else {
                if (pl2WinRatio > bestRatio) {
                    bestRatio = pl2WinRatio;
                    bestMove = i;
                }
                System.out.println(playerID + " " + (i + 1)  + " Win Ratio: " + pl2WinRatio);
            }
        }

        return bestMove + 1;
    }

    public void printScores(Map<Integer, Integer[]> scores) {
        for (Integer[] score : scores.values())
            System.out.printf("%d, %d, % d", score[0], score[1], score[2]);
    }
}
