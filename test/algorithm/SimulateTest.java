package algorithm;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * SimulateTest:
 * <brief description of class>
 */
@RunWith(Parameterized.class)
public class SimulateTest {

    private int[] testBoard;
    private int move;
    private int exp;
    private int[] bExp;
    private int pl;
    private Player player;

    public SimulateTest(int[] testBoard, int move,  int pl, int exp, int[] bExp) {
        this.testBoard = testBoard;
        this.move = move;
        this.exp = exp;
        this.bExp =  bExp;
        this.pl = pl;
        this.player = new Player(pl);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        int[] b     = new int[]{3,3,3,3,3,3,0,3,3,3,3,3,3,0};
        int[] bExp  = new int[]{0,4,4,4,3,3,0,3,3,3,3,3,3,0};


        return Arrays.asList(new Object[][]{
                //  board move player  expT      expB
                {   b,     1,    1,     2,       bExp }
        });
    }

//    @Before
//    public void setUp() {
//    }

    @Test
    public void testSimulateMove() throws Exception {
        assertEquals(exp, player.simulateMove(move, pl, testBoard));
    }

    @Test
    public void testBoardAugmentation() throws Exception {

        Field boardField = player.getClass().getDeclaredField("simBoard");
        boardField.setAccessible(true);

        Board bb = (Board) boardField.get(player);

        int[] tmp = bb.convertToIntArray();

        assertArrayEquals(bExp, tmp);
    }

//    @AfterClass
//    public void tearDown() {
//        this.player = null;
//    }
}
