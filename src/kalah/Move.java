package kalah;

/**
 * client.kalah.Move:
 * <brief description of class>
 */
public class Move {

    private int[] board;
    private int me;
    private int turn;

    private Player player;

    public Move(int me, int turn, int[] board) {
        super();
        this.me = me;
        this.turn = turn;
        this.board = board;
        this.player = new Player(turn);
    }

    public int makeMove(int[] board) {
        this.board = board;

    	return this.player.makePlay(board);
//    	// Implement algorithm
//    	if (this.me == 1) {
//    	    for(int i = 0; i < 6; i++){
//    	        if (this.board[i] > 0)
//    	            return i;
//    	    }
//    	} else {
//    	    for (int i = 7; i < 13; i++) {
//    	        if (this.board[i] > 0)
//    	            return i;
//    	    }
//    	}
//    	return 0;
    }

    public static void main(String[] argv) {

    }
}
