package algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

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

        int[][] scores = simulateGame(playerID, board);

        // calculate the best move
        int move = calcBestMove(scores);

        return move;
    }

    public Queue<Integer> getMoves(int[] board) {
        Queue<Integer> moves = new LinkedList<Integer>();
        for (int i = 0; i < 6; i++) {
            int pieceIndex = i;
            // adjust if player 2
            if (playerID == 2)
                pieceIndex = (6 - i) * 2;

            // check if the piece has seeds
            if (board[pieceIndex] > 0)
                moves.add(i); // add the piece to potential moves
        }
        return moves;
    }

    public int[][] simulateGame(int player, int[] board) {
        Queue<Integer> moves = getMoves(board);

        Iterator iter = moves.iterator();

        // An 2D array that holds game outcomes of each move
        int[][] scores = new int[moves.size()][3];
        // initialize to 0
        for (int i = 0; i < moves.size(); i++)
            for (int j = 0; j < 3; j++)
                scores[i][j] = 0;

        // while there are moves left
        while (iter.hasNext()) {
            // get the move
            int move = moves.poll();

            // simulate the move
            int turn = simulateMove(move, playerID, board);

            int[] tmpBoard = simBoard.convertToIntArray();

            // Base Case: game over
            if (turn == 0 || tmpBoard[6] > 18 || tmpBoard[13] > 18) {

                // get score of game
                scores[move][0] += getScore(tmpBoard);

                // get winner
                if (scores[move][0] > 0) scores[move][1] += 2;
                else if (scores[move][0] == 0) scores[move][1] += 1;
                else scores[move][1] += 0;

                // count game
                scores[move][2] += 1;

            } else {
                // recursively call simGame until complete
                simulateGame(turn, simBoard.convertToIntArray());
            }
        }

        // return the fully populated scores array
        return scores;

    }

    public int simulateMove(int move, int playerID, int[] board) {
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

    public int calcBestMove(int[][] scores) {
        double bestRatio = -1;
        double winRatio;
        int bestScore = -9999;
        int bestMove = 0;

        for (int i = 0; i < scores.length; i++) {

            winRatio = ((double)scores[i][1]/(double)scores[i][2]) * 100;

            if (winRatio > bestRatio){
                bestRatio = winRatio;
                bestScore = scores[0][i];
                bestMove = i;
            }else if (winRatio == bestRatio && scores[0][i] > bestScore){
                bestScore = scores[0][i];
                bestMove = i;
                bestRatio = winRatio;
            }
        }

        return bestMove;
    }
}
