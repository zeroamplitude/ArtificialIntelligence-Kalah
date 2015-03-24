import algorithm.Board;

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
        // get all the possible moves
        Queue moves = getMoves(board);

        for (Object m : moves) {

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
        return this.simBoard.transfer(move, playerID, board);
    }

    public int calcBestMove() {
        return 0;
    }
}
