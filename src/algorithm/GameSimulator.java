package algorithm;

/**
 * This class represents a Game of Kalah. It's responsibilities are control game flow and updating GUI elements as necessary.
 */
public class GameSimulator {
	/**
	 * An integer that represents the game's id
	 */
	private int gameID;
	/**
	 * An object of type board that represents the game's board.
	 */
	private int[] board;

    private int turn;

    public GameSimulator() {
        this.board = new int[14];

        // create a  board with specified number of seeds
        for (int i = 0; i < 14 ; i++) {
            if (i == 6 || i == 13)
                board[i] = 0;
            else
                board[i] = 3;
        }

        this.turn = 1;
    }

    public void setGameState(int[] board, int turn) {
        this.board = board;
        this.turn = turn;
    }

    public int[] getBoard() {
        return board;
    }


    public int getTurn() {
        return turn;
    }

    /**
	 * This method calls the move method of the player whose turn it is,
	 * passes the results to board which executes the move. If the board
	 * returns an invalid move the player whose turn it is, will be signaled
	 * to move again. If board signals game over then game exits.
	 */
//	public boolean update() {
//        int move;
//        if (turn == 1)
//            move = pl1.makeMove(board);
//        else
//            move = pl2.makeMove(board);
//
//        turn = move(move, turn);
//
//        return turn != 0;
//    }

    /**
     * This method is responsible for making a move on the board. Before the move is executed within the method validation methods are
     * called on the input. If input is invalid then the move is not executed and the player is requested to move again. Otherwise the
     * seeds are transferred from the source to the adjacent stores by
     * method call transfer(). transfer returns the id of the last piece
     * which a seed was transferred to. The piece is checked to see if it
     * meets any special conditions.
     * @param store A integer from 1 to 6 that represents the store a player
     * selected to transfer seeds from.
     * @param pid An integer, either 1 or 2 that represents the id of the
     * player who is making the move.
     * @return An integer that represent the player whose turn is next.
     */
    public int move(int store, int pid) {
        // adjust the store to follow boards indexes
//        store -= 1;
        // if player 2 adjust store
        if (pid == 2)
            store = across(store);

        int last = transfer(store);

        // if the last piece is not a house
        if (last != 6 && last != 13) {
            if (special(last)) {
                steal(last);
            }
            // switch turns
            if (turn == 1)
                turn = 2;
            else
                turn = 1;
        }

        if (gameover()) {
            clear();
            turn = 0;
        }

        return turn;
    }


    /**
     * This method is responsible for transferring all the seeds from a
     * store to the board adjacent to it.
     * @param store An integer representing the store that will be transferring
     * the seeds.
     * @return An integer that represents the last store that a seed was transferred to.
     */
    public int transfer(int store) {
        int dest = store + 1;
        int seeds = board[store];
        for (int i = 1; i <= seeds; i++) {
            // if the dest if the opponents house -> skip
            if (dest == 6 && store > 6 || dest == 13 && store < 6)
                dest++;
            // if the dest is passed index range -> reset
            if (dest > 13)
                dest = 0;

            // execute transfer
            board[store] -= 1;
            board[dest] += 1;

            // if not last seed increase dest
            if (i < seeds)
                dest++;
        }
        return dest;
    }

    /**
     * This method is called to check if the last store to receive a seed
     * had 0 seeds in it prior to the transfer, is owned by the player who
     * made the move and the piece adjacent to it was not empty.
     * @param piece An integer that represents the piece of the board.
     * @return true if conditions are met, otherwise false
     */
    public boolean special(int piece) {
        // check that the piece was empty
        if (board[piece] != 1)
            return false;
        // check the the piece is owned by the player
        if ((piece < 6 && turn == 2) || (piece > 6 && turn == 1))
            return false;
        // check that the piece across from it is not empty
        if (board[across(piece)] == 0)
            return false;

        return true;
    }

    /**
     * This method is responsible for executing a steal operation in the game
     * of Kalah. It will empty the store that is specified and the store opposite to it and transfer the seeds to the home of the player who made the move.
     * @param store An integer that represents the store that was the last to
     * receive a seed on a transfer.
     */
    public void steal(int store) {
        // get total num seeds for transfer
        int seeds = board[store] + board[across(store)];

        // add seeds to the home of the player
        if (store < 6)
            board[6] += seeds;
        else
            board[13] += seeds;

        // zero the stores involved
        board[store] = 0;
        board[across(store)] = 0;
    }

    public boolean gameover() {
        boolean p1 = true;
        boolean p2 = true;

        for (int i = 0; i < 6; i++){
            if (board[i] != 0)
                p1 = false;
            if (board[across(i)] != 0)
                p2 = false;
            if (!p1 && !p2)
                return false;
        }
        return true;
    }

    public void clear() {
        for (int i = 0; i < 6; i++) {
            board[6] += board[i];
            board[i] = 0;
            board[13] += board[across(i)];
            board[across(i)] = 0;
        }
    }

    /**
     * This method calculates the piece across from the one selected.
     * across = piece + ((6 - piece) * 2)
     * @param piece A integer that represents the a piece of the board
     * @return An integer that represent the piece across from the one provided.
     */
    public int across(int piece) {
        return piece + ((6 - piece) * 2);
    }

    public void printBoard() {
        System.out.printf("   P2  :   1     2      3      4       5      6\n");
        System.out.printf("+------+------+------+------+------+------+------+------+\n");
        System.out.printf("|      |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |      |\n", board[12], board[11], board[10], board[9], board[8], board[7]);
        System.out.printf("|  %02d  |------+------+------+------+------+------|  %02d  |\n", board[13], board[6]);
        System.out.printf("|      |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |      |\n", board[0], board[1], board[2], board[3], board[4], board[5]);
        System.out.printf("+------+------+------+------+------+------+------+------+\n");
        System.out.printf("           1     2      3      4       5      6  :  P1\n");
    }

}