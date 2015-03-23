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
        this.board = new Board(player);
    }

    @After
    public void tearDown() throws Exception {
        this.board = null;
        this.player = null;
    }

    @Test
    public void testIsGameOver() throws Exception {
        int[] b1 = new int[]{0,0,0,0,0,1,3,0,0,0,0,0,1,3};
        this.board.transfer(0, 1, b1);
    }

    @Test
    public void testTransfer() throws Exception {

    }

    @Test
    public void testClear() throws Exception {

    }

    @Test
    public void testConvertToIntArray() throws Exception {

    }

    @Test
    public void testSetBoard() throws Exception {

    }
}