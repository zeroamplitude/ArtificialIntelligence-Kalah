package kalah;

import java.util.Stack;

/**
 * This class represents a object that are pieces of the board.
 */
public class Piece {

	/**
	 * A stack that holds all the transferable items.
	 */
	private Stack<Seed> seeds;
	private int id;

	/**
	 * The piece constructor. Take the stack of seeds and initializes the
	 * seeds in the piece.
	 * @param seeds A stack of Seeds that represent the initial piece state
	 */
	public Piece(int id, Stack<Seed> seeds) {
		this.seeds = seeds;
        this.id = id;
	}

	/**
	 * This method is responsible for putting an item into the stack seeds.
	 * @param item An object that represents a seed in the game Kalah.
	 */
	public void putItem(Seed item) {
		// TODO - implement Piece.putItem
		throw new UnsupportedOperationException();
	}

	public int getId() {
		return this.id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
			return this.seeds.size();
		throw new UnsupportedOperationException();
	}

}