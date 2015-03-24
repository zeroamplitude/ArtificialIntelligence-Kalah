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
    private int[] board;

    private int playerID;

    private Board simBoard;

    public Player(int playerID) {
        this.board = new int[14];
        this.playerID = playerID;
        this.simBoard = new Board();
    }

    public int makePlay(int[] board) {
        // set board
        this.board = board;
        System.out.println("START");



        Map<Integer, Integer[]> scores = simulateGame(playerID, board);

        // calculate the best move
        int move = calcBestMove(scores);

        return move;
    }

    public Map<Integer, Integer[]> simulateGame(int player, int[] board) {
        Queue<Integer> moves = getMoves(board);

        // An 2D array that holds game outcomes of each move
        Map<Integer, Integer[]> scores = new HashMap<Integer, Integer[]>();

        // initialize to 0
        for (Integer i : moves)
            for (int j = 0; j < 3; j++)
                scores.put(i, (new Integer[]{0, 0, 0}));

        Iterator iter = moves.iterator();

        // while there are moves left
        while (iter.hasNext()) {
            // get the move
            int move = moves.poll();


            // simulate the move
            int turn = simulateMove(move, playerID, board);

            int[] tmpBoard = simBoard.convertToIntArray();
            System.out.println("After piece: " + move);
            printBoard(tmpBoard);
            System.out.println("*****************************************************************");


            // Base Case: game over
            if (turn == 0 || tmpBoard[6] > 18 || tmpBoard[13] > 18) {

                // get score of game
                scores.get(move)[0] += getScore(tmpBoard);

                // get winner
                if (scores.get(move)[0] > 0) scores.get(move)[1] += 2;
                else if (scores.get(move)[0] == 0) scores.get(move)[1] += 1;
                else scores.get(move)[1] += 0;

                // count game
                scores.get(move)[2] += 1;


            } else {
                // recursively call simGame until complete
                simulateGame(turn, simBoard.convertToIntArray());
            }
        }

        // return the fully populated scores array
        return scores;

    }

    public Queue<Integer> getMoves(int[] board) {
        Queue<Integer> moves = new LinkedList<Integer>();
        for (int i = 0; i < 6; i++) {
            int pieceIndex = i;
            // adjust if player 2
            if (playerID == 2)
                pieceIndex += (6 - i) * 2;

            // check if the piece has seeds
            if (board[pieceIndex] > 0)
                moves.add(i); // add the piece to potential moves
        }
        return moves;
    }

    public int simulateMove(int move, int playerID, int[] board) {
        System.out.println("TRANSFER: Player: " + playerID + "MOVE: " + move);
        System.out.println("Before");
        printBoard(board);
        return this.simBoard.transfer(move, playerID, board);
    }

    public int getScore(int[] board) {
        int pl1Score = board[6];
        int pl2Score = board[13];
        int diff;
        if (playerID == 1)
            diff = pl1Score - pl2Score;
        else
            diff = pl2Score - pl1Score;
        return diff;
    }

    public int calcBestMove(Map<Integer, Integer[]> scores) {
        double bestRatio = -1;
        double winRatio;
        int bestScore = -9999;
        int bestMove = 0;

        for (int i : scores.keySet()) {
            winRatio = (((double)scores.get(i)[1]) / (double)scores.get(i)[2]) * 100;
            System.out.println("bestScore: " + bestScore);

            if (winRatio > bestRatio){
                bestRatio = winRatio;
                bestScore = scores.get(i)[0];
                bestMove = i;
            }else if (winRatio == bestRatio && scores.get(i)[0] > bestScore){
                bestScore = scores.get(i)[0];
                bestMove = i;
                bestRatio = winRatio;
            }
        }
        return bestMove + 1;
    }

    public void printBoard(int[] board) {
        System.out.printf("+------+------+------+------+------+------+------+------+\n");
        System.out.printf("|      |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |      |\n", board[7], board[8], board[9], board[10], board[11], board[12]);
        System.out.printf("|  %02d  |------+------+------+------+------+------|  %02d  |\n", board[13], board[6]);
        System.out.printf("|      |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |      |\n", board[0], board[1], board[2], board[3], board[4], board[5]);
        System.out.printf("+------+------+------+------+------+------+------+------+\n");
    }

    public void printScores(Map<Integer, Integer[]> scores) {
        for (Integer[] score : scores.values())
            System.out.printf("%d, %d, % d", score[0], score[1], score[2]);
    }
}
