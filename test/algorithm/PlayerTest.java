package algorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
@RunWith(Parameterized.class)
public class PlayerTest {
    private int[] testBoard;
    private int exp;
    private int pl;
    private Player player;

    public PlayerTest(int[] board, int pl, int exp) {
        this.testBoard = board;
        this.pl = pl;
        this.exp = exp;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
//        int[] b1a = new int[]{0,3,3,3,3,3,3,0,3,3,3,3,3,3,0};
        //Expected move percentage array: x,x,x,x,100,x
        //Expected score diference array: x,x,x,x, 2 ,x
        int[] b1a = new int[] { 0, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0};


        //should be player 2
        //Expected move percentage array: x,x,x,x,100,x
        //Expected score diference array: x,x,x,x, 2 ,x
        int[] b1b = new int[] { 0, 0, 0, 1, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0};


        //Expected move percentage array: x,100,100,x,x,x
        //Expected score diference array: x, 8 , 6 ,x,x,x
        int[] b2a = new int[] { 0, 3, 4, 0, 0, 0, 0,
                0, 0, 0, 0, 1, 0, 0};


        //should be player 2
        //Expected move percentage array: x,x,x,100,100,x
        //Expected score diference array: x,x,x, 6 , 8 ,x
        int[] b2b = new int[] { 0, 1, 0, 0, 0, 0, 0,
                0, 0, 0, 4, 3, 0, 0};

        //should be player 1
        //Expected move percentage array: x,x,100,x,x,100
        //Expected score diference array: x,x, 4 ,x,x, 4
        int[] b3a = new int[] { 0, 0, 1, 0, 0, 1, 0,
                1, 0, 0, 2, 0, 0, 0};



        return Arrays.asList(new Object[][] {
                //  board   player  exp
                {   b1a,    1,      4}, //[0]
                {   b1b,    2,      5}, //[1]
                {   b2a,    1,      4}, //[2]
                {   b2b,    2,      5}, //[3]
                {   b3a,    1,      3}//,
              //{ board,  pl#,    exp}
        });
    }

    @Before
    public void setUp() throws Exception {
        player = new Player(pl);
    }

    @After
    public void tearDown() throws Exception {
        player = null;
    }

    @Test
    public void testMakePlay() throws Exception {
        assertEquals(exp, player.makePlay(testBoard));
    }

//
//    @Test
//    public void testGetMoveArray() throws Exception {
//
//    }
//
//    @Test
//    public void testGetValueOfMove() throws Exception {
//
//    }
//
//    @Test
//    public void testMain() throws Exception {
//
//    }

//    @Test
//    public void testPrintBoard() throws Exception {
//        int[] testBoard = new int[] { 0, 0, 0, 1, 0, 0, 0,
//                0, 0, 0, 0, 1, 0, 0};
//        player.printBoard(testBoard);
//    }
}