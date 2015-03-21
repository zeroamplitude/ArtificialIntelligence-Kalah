package kalah;

/**
 * This class represents a object that are pieces of the board.
 */
public class Piece {

	/**
	 * A stack that holds all the transferable items.
	 */
	private Stack<Seed> seeds;
	private String type;
	private int id;

	/**
	 * The piece constructor. Take the stack of seeds and initializes the
	 * seeds in the piece.
	 * @param seeds A stack of Seeds that represent the initial piece state
	 */
	public Piece(Stack<Seed> seeds) {
		// TODO - implement Piece.Piece
		throw new UnsupportedOperationException();
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
		// TODO - implement Piece.getCount
		throw new UnsupportedOperationException();
	}

}