package algorithm;

import org.junit.After;
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
    }

    @Before
    public void setUp() throws Exception {
        this.player = new Player(pl);
    }

    @After
    public void tearDown() throws Exception {
        this.player = null;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        int[] b     = new int[]{3,3,3,3,3,3,0,3,3,3,3,3,3,0};
        int[] b2  = new int[]{0,0,0,0,0,8,0,0,0,0,0,0,0,0};


        return Arrays.asList(new Object[][]{
                //  board move player  expT      expB
                {   b,     0,    1,     2,       b2 },
                {   b,     3,    1,     1,       b  },
                {   b,     0,    2,     1,       b2 },
                {   b,     5,    1,     2,       b2 },
                {  b2,     5,    1,     0,       b  },
        });
    }


    @Test
    public void testASimulateMove() throws Exception {
        assertEquals(exp, player.simulateMove(move, pl, testBoard));
    }
}
