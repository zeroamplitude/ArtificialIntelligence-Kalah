package kalah;

import java.util.*;

/**
 * This class is represents a board piece with type store
 */
public class Store extends Piece {

	/**
	 *
     * @param id
     * @param owner
     * @param seeds
     */
	public Store(int id, int owner, Stack<Seed> seeds) {
		super(id, owner, seeds);
	}

	/**
	 * This method pop an item from the stack items. If no items are left
	 * null is returned.
	 * @return An object that represents a seed in the game kalah.
	 */
	public Seed getItem() {
		if (seeds.isEmpty()) return null;
		else return seeds.pop();
	}
}