package kalah;

import java.util.*;

/**
 * This class is represents a board piece with type store
 */
public class Store extends Piece {

	/**
	 * 
	 * @param seeds
	 */
	public Store(Stack<Seed> seeds) {
		// TODO - implement Store.Store
		throw new UnsupportedOperationException();
	}

	/**
	 * This method pop an item from the stack items. If no items are left
	 * null is returned.
	 * @return An object that represents a seed in the game kalah.
	 */
	public Seed getItem() {
		if (seeds.isEmpty()) return null;
		else return seeds.pop();
		throw new UnsupportedOperationException();
	}

	/**
	 * This method is responsible for checking if the source piece specified
	 * has seeds.
	 * @return true if the source piece is empty, otherwise false
	 */
	public boolean isEmpty() {
		if (seeds.size() == 0) return true;
		else return false;
		throw new UnsupportedOperationException();
	}

}