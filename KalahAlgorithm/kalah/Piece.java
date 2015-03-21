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

    private int owner;

	/**
	 * The piece constructor. Take the stack of seeds and initializes the
	 * seeds in the piece.
	 * @param seeds A stack of Seeds that represent the initial piece state
	 */
	public Piece(int id, int owner, Stack<Seed> seeds) {
		this.seeds = seeds;
        this.id = id;
        this.owner = owner;
	}

	/**
	 * This method is responsible for putting an item into the stack seeds.
	 * @param item An object that represents a seed in the game Kalah.
	 */
	public void putItem(Seed item) {
		this.seeds.push(item);
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
<<<<<<< HEAD
			return this.seeds.size();
=======
        return this.seeds.size();
>>>>>>> 3d9110f6f0d6127d015ca782a06dfac63a61e308
	}

    /**
     *
     * @return An integer representing the player that owns the piece.
     */
    public int getOwner() {
        return owner;
    }
}