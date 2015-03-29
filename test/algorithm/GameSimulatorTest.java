package algorithm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameSimulatorTest {
    private static final int[] BOARD = new int[] {3, 3, 3, 3, 3, 3, 0, 3, 3, 3, 3, 3, 3, 0};
    private static final int[] BOARD1 = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private GameSimulator game;

    @Before
    public void setUp() throws Exception {
        game = new GameSimulator();
    }

    @After
    public void tearDown() throws Exception {
        game = null;
    }

    @Test
    public void testSetGameState() throws Exception {
        assertArrayEquals(BOARD, game.getBoard());
        game.setGameState(BOARD1, 0);
        assertArrayEquals(BOARD1, game.getBoard());
        assertEquals(0, game.getTurn());
    }

    @Test
    public void testMove() throws Exception {
        System.out.println("Move Test");
        assertArrayEquals(BOARD, game.getBoard());

        game.move(4, 1, BOARD);
        int[] TEMP_BOARD = new int[] {3, 3, 3, 0, 4, 4, 1, 3, 3, 3, 3, 3, 3, 0};
        game.printBoard();
        assertArrayEquals(TEMP_BOARD, game.getBoard());
        assertEquals(1, game.getTurn());

        game.move(1, 1, game.getBoard());
        game.printBoard();
        TEMP_BOARD = new int[] {0, 4, 4, 0, 4, 4, 5, 3, 3, 0, 3, 3, 3, 0};
        assertArrayEquals(TEMP_BOARD, game.getBoard());
        assertEquals(2, game.getTurn());

        System.out.println("Test skip opponents home");
        TEMP_BOARD = new int[] {0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0};
        game.setGameState(TEMP_BOARD, 0);
        game.move(6, 1, TEMP_BOARD);
        game.printBoard();
        TEMP_BOARD = new int[] {0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 5};
        assertArrayEquals(TEMP_BOARD, game.getBoard());
        assertEquals(0, game.getTurn());

        System.out.println("Test skip opponents home p2");
        TEMP_BOARD = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0};
        game.setGameState(TEMP_BOARD, 2);
        game.move(1, 2, TEMP_BOARD);
        game.printBoard();
    }

    @Test
    public void testTransfer() throws Exception {
        int[] TEMP_BOARD = new int[] {0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0};
        game.setGameState(TEMP_BOARD, 1);
        assertEquals(0, game.transfer(5));

        TEMP_BOARD       = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0};
        game.setGameState(TEMP_BOARD, 2);
        assertEquals(7, game.transfer(12));
    }

    @Test
    public void testSpecial() throws Exception {
        System.out.println("Test skip opponents home p2");
        int[] TEMP_BOARD = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0};
        game.setGameState(TEMP_BOARD, 1);
        game.move(1, 2, TEMP_BOARD);
        game.printBoard();

    }

    @Test
    public void testSteal() throws Exception {
        System.out.println("Steal Test");
        assertArrayEquals(BOARD, game.getBoard());
        game.steal(3);
        game.printBoard();
        int[] B1 = new int[] {3, 3, 3, 0, 3, 3, 6, 3, 3, 0, 3, 3, 3, 0};
        assertArrayEquals(B1, game.getBoard());
        game.steal(11);
        game.printBoard();
        int[] B2 = new int[] {3, 0, 3, 0, 3, 3, 6, 3, 3, 0, 3, 0, 3, 6};
        assertArrayEquals(B2, game.getBoard());
    }

    @Test
    public void testAcross() throws Exception {
        assertEquals(12, game.across(0));
        assertEquals(11, game.across(1));
        assertEquals(10, game.across(2));
        assertEquals(9 , game.across(3));
        assertEquals(8 , game.across(4));
        assertEquals(7 , game.across(5));
    }

    @Test
    public void testGameover() throws Exception {
        assertEquals(false, game.gameover());

        System.out.println("Gameover1: True");
        int[] B1 = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        game.setGameState(B1, 1);
        game.printBoard();
        assertEquals(true, game.gameover());

        System.out.println("Gameover2: True");
        int[] B2 = new int[] {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        game.setGameState(B2, 1);
        game.printBoard();
        assertEquals(true, game.gameover());

        System.out.println("Gameover3: True");
        int[] B3 = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0};
        game.setGameState(B3, 1);
        game.printBoard();
        assertEquals(true, game.gameover());

        System.out.println("Gameover4: False");
        int[] B4 = new int[] {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0};
        game.setGameState(B4, 1);
        game.printBoard();
        assertEquals(false, game.gameover());
    }

    @Test
    public void testClear() throws Exception {
        assertArrayEquals(BOARD, game.getBoard());
        game.clear();
        game.printBoard();
        int[] B1 = new int[] {0, 0, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 18};
        assertArrayEquals(B1, game.getBoard());
    }
}