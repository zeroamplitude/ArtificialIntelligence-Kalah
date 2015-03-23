package algorithm;

import java.util.*;

/**
 * This class represents a board of the kalah game.
 */
public class Board {

    private Player algorithm;

	/**
	 * A map of type key, int, and value, piece, which represents
	 * the piece of the board. The key is the piece ID and the
	 * value is a reference to the piece in memory.
	 */
	private Map<Integer, Piece> pieces;
	/**
	 * An integer that represents the current players turn
	 */
	private int turn;

    /**
     * An array of 2 house objects that represent the player's homes
     */
    private House[] home;

	/**
	 * This is the default constructor for the Board class.This will set the
	 * board to it's initial game state.
     * @param algorithm
     */
	public Board(Player algorithm) {
        this.turn = 1;

        // create a reference to the algorithm
        this.algorithm = algorithm;

        // initialize player homes
        this.home = new House[2];

        // initialize an empty map of pieces
        this.pieces = new HashMap<Integer, Piece>();

        int count = 0;
        for (int i = 0; i < 14; i++) {
            // set the piece's owner
            int owner;
            if (i < 7)
                owner = 1;
            else
                owner = 2;

            // build an empty stack of seeds
            Stack<Seed> seeds = new Stack<Seed>();

            if (i == 6 || i == 13) {
                // add a house to the map with empty seeds
                pieces.put(i, new House(i, owner, seeds));
                this.home[i % 6] = (House) pieces.get(i);

            } else {
                // push seeds to the stack
                for (int j = 0; j < 3; j++) {
                    seeds.push(new Seed(count));
                    count++;
                }
                // add a store to the map with 3 seeds
                pieces.put(i, new Store(i, owner, seeds));
            }
        }

	}

	/**
	 * This method is responsible for checking if the game is over. It
	 * checks both sides of the board to see if either side has no seeds.
	 * @return A boolean object that represents if the game is over
	 */
	public boolean isGameOver() {
        boolean pl1 = true;
        boolean pl2 = true;
        for(Piece cur : pieces.values()) {
            if (cur.getClass() == Store.class && cur.getCount() != 0) {
                if (cur.getOwner() == 1)
                    pl1 = false;
                if (cur.getOwner() == 2)
                    pl2 = false;
            }
        }

        return pl1 && pl2;
	}

	/**
	 * This method transfers the seeds from the source store to the destination
     * pieces adjacent to it. It iterates until all seeds are
	 * removed from the source store. If the destination piece is the
	 * opponents home then skip and move to the next piece. Each
	 * iteration makes a call to isLastSeed(). If it is the last seed then
	 * it calls isHome which returns the turn of the player who made the
	 * move. Otherwise it call isOwner which checks if the player who is
	 * making the move owns the destination piece. If true it calls
	 * isEmpty to check the destination piece which if true calls isEmpty to
     * the opponents piece across from the destination piece. If the
	 * the opponents piece across from destination piece is not empty all
	 * seeds are transferred to the house of the player that made the
	 * move.
	 * @param source An integer that represents the piece of the board that
	 * the seeds will be transferred from.
	 */
	public int transfer(int source, int player, int[] board) {
        setBoard(board);


        // source to board keys
        if (player == 2)
            source += (6 - source) * 2;

        Store origin = (Store) this.pieces.get(source);
        int destIndex = source + 1;


        System.out.println("****************************************************");
        System.out.println("***********************TEST 1*************************");
        System.out.println("****************************************************");

        for (int i = 0; i < origin.getCount(); i++) {

            System.out.println("****************************************************");
            System.out.println("***********************TEST 2*************************");
            System.out.println("****************************************************");
            Seed seed = origin.getItem();

            // skip opponents home
            if ( (destIndex == 6 && player == 2) || (destIndex == 13 && player == 1) )
                destIndex++;

            // loop back to beginning
            if (destIndex > 13)
                destIndex = 0;

            // get cur piece
            Piece dest = pieces.get(destIndex);

            // put the seed into the piece
            dest.putItem(seed);


            // check if that was the last seed
            if (i == 1 - origin.getCount()) {
                // check if cur/des is a store
                if (dest.getClass() == Store.class) {
                    // check that the player owns the store
                    if (dest.getOwner() == player) {
                        // check if the dest store had 0 seeds before  the transfer
                        if (dest.getCount() == 1) {
                            // get the piece across from dest
                            Store tmp = (Store) pieces.get(destIndex + (6 - destIndex) * 2);
                            // check if the across piece is greater than 0
                            if (tmp.getCount() > 0) {
                                // transfer all seeds from across into players home
                                for (int j = 0; j < dest.getCount(); j++) {
                                    home[player - 1].putItem(tmp.getItem());
                                }
                                // transfer all seeds from dest into players home
                                for (int j = 0; j < tmp.getCount(); j++){
                                    // cast dest to a store
                                    tmp = (Store) dest;
                                    home[player - 1].putItem(tmp.getItem());
                                }
                            }
                        }
                    }
                    // switch turn
                    if (turn == 1)
                        turn = 2;
                    else
                        turn = 1;
                }
            }
            destIndex ++;
        }

        System.out.println("****************************************************");
        System.out.println("***********************TEST 3*************************");
        System.out.println("****************************************************");



        // check is game is over
        if(isGameOver())
            turn = 0;

        algorithm.setTmpBoard(convertToIntArray());
        return turn;
	}

    public int[] convertToIntArray() {
        int[] tmp = new int[14];

        int i = 0;
        int j = 12;

        for (Piece cur : pieces.values()) {
                tmp[i] = cur.getCount();
            i++;
        }

        // adjust for Players board
        for (i = 7; i < j; i++) {
            int tmpSwitch = tmp[i];
            tmp[i] = tmp[j];
            tmp[j] = tmpSwitch;
            j--;
        }

        return tmp;
    }

    public void setBoard(int[] board) {
        // adjust for Algorithms board
        int j = 12;
        for (int i = 7; i < j; i++) {
            int tmp = board[i];
            board[i] = board[j];
            board[j] = tmp;
            j--;
        }

        this.pieces.clear();
        int count = 0;
        for (int i = 0; i < 14; i++) {
            int owner;
            if (i < 7)
                owner = 1;
            else
                owner = 2;

            Stack<Seed> tmp = new Stack<Seed>();
            for (j = 0; j < board[i]; j++) {
                tmp.push(new Seed(count));
                count++;
            }

            if (i == 6 || i == 13)
                this.pieces.put(i, new House(i, owner, tmp));
            else
                this.pieces.put(i, new Store(i, owner, tmp));
        }
    }
}