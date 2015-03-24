import algorithm.Board;

import javax.swing.text.html.HTMLDocument;
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

        // get all the possible moves
        Queue<Integer> moves = getMoves(board);

        Iterator iter = moves.iterator();
        while (iter.hasNext()) {
            int turn = simulateMove(moves.poll(), playerID, board);
            // if the game is over
            if (turn == 0) {
                // calculate score
                score = getScore(simBoard.convertToIntArray());
                if (score > 0) win = 1;
                else if (score == 0) win = 0.5;
                else win = 0;

            } else {
                // simulate the next moves
                Queue<Integer> nextMoves = getMoves(simBoard.convertToIntArray());
                Iterator iterNext = nextMoves.iterator();
                while (iterNext.hasNext())
                    simulateMove(nextMoves.poll(), turn, simBoard.convertToIntArray());
            }
        }


        // calculate the best move
        int move = calcBestMove();

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

    public int simulateMove(int move, int playerID, int[] board) {
        int turn = this.simBoard.transfer(move, playerID, board);
        int score = getScore(simBoard.convertToIntArray());

        return turn;
    }

    public int getScore(int[] board) {
        int pl1Score = board[6];
        int pl2Score = board[13];
        int diff = 0;
        if (playerID == 1)
            diff = pl1Score - pl2Score;
        else
            diff = pl2Score - pl1Score;
        return diff;
    }

    public int calcBestMove() {
        return 0;
    }
}
