package algorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board;
    private Player player;

    @Before
    public void setUp() throws Exception {
        this.player = new Player(1);
        this.board = new Board();
    }

    @After
    public void tearDown() throws Exception {
        this.board = null;
        this.player = null;
    }

    @Test
    public void testIsGameOver() throws Exception {
        int[] b1 = new int[]{0,0,0,0,0,0,3,0,0,0,0,0,0,3};
        board.setBoard(b1);
        assertEquals(true, this.board.isGameOver());
        int[] b2 = new int[]{0,0,0,0,0,1,3,0,0,0,0,0,0,3};
        board.setBoard(b2);
        assertEquals(true, this.board.isGameOver());
        int[] b3 = new int[]{0,0,0,0,0,0,3,0,0,0,0,0,1,3};
        board.setBoard(b3);
        assertEquals(true, this.board.isGameOver());
        int[] b4 = new int[]{0,0,0,1,0,0,3,0,0,0,0,0,1,3};
        board.setBoard(b4);
        assertEquals(false, this.board.isGameOver());
    }

    @Test
    public void testTransfer() throws Exception {
        int[] b2 = new int[]{0,0,0,0,0,1,1,0,0,1,1,0,0,1};
        assertEquals(0, board.transfer(5, 1, b2));
//        player.printBoard(board.convertToIntArray());
        int[] b1 = new int[]{0,0,0,0,1,0,1,0,0,0,0,0,1,1};
//        player.printBoard(b1);
        assertEquals(0, board.transfer(4, 1, b1));
//        player.printBoard(board.convertToIntArray());
    }

    @Test
    public void testClear() throws Exception {
        int[] exp = new int[]{0,0,0,0,0,0,2,0,0,0,0,0,0,3};
        int[] b2  = new int[]{0,0,0,0,0,1,1,0,0,1,1,0,0,1};
        board.clear();
        System.out.println(board.convertToIntArray()[6]);
        assertArrayEquals(exp, board.convertToIntArray());
    }

    @Test
    public void testConvertToIntArray() throws Exception {

    }

    @Test
    public void testSetBoard() throws Exception {

    }
}