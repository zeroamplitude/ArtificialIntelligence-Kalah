package algorithm;

import algorithm.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.Assert.*;
//@RunWith(Parameterized.class)
public class PlayerTest {
    private int[] board;
    private Player player;

//    public PlayerTest(int[] board) {
//        this.board = board;
//    }

//    @Parameterized.Parameters
//    public static Collection<Object[]> data() {
//
//        int[] b1 = new int[]{0,3,3,3,3,3,3,0,3,3,3,3,3,3,0};
//
//        return Arrays.asList(new Object[][] {
//                {b1}
//        });
//    }

    @Before
    public void setUp() throws Exception {
        player = new Player(1);
    }

    @After
    public void tearDown() throws Exception {
        player = null;
    }

    @Test
    public void testSetTmpBoard() throws Exception {

    }

    @Test
    public void testMakePlay() throws Exception {
        int[] b1 = new int[]{3,3,3,3,3,3,0,3,3,3,3,3,3,0};
        player.makePlay(b1);
    }

    @Test
    public void testMakePlay2() throws Exception {
        int[] testBoard = new int[] { 0, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0};
        assertEquals(4,player.makePlay(testBoard));
    }

    @Test
    public void testGetMoveArray() throws Exception {

    }

    @Test
    public void testGetValueOfMove() throws Exception {

    }

    @Test
    public void testMain() throws Exception {

    }

    @Test
    public void testPrintBoard() throws Exception {
        int[] testBoard = new int[] { 0, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0};
        player.printBoard(testBoard);
    }
}