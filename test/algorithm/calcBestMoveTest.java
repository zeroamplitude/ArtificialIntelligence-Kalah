package algorithm;

import algorithm.Player;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * GetScoreTest Tester.
 *
 * @author <Authors name>
 * @since <pre>Mar 24, 2015</pre>
 * @version 1.0
 */
public class calcBestMoveTest {

    private Player player;

    @After
    public void after() throws Exception {
        this.player = null;
    }

    public void testcalcBestMove() throws Exception {
        System.out.println("test1");
        this.player = new Player(1);
        Map<Integer,Integer[]> scores = new HashMap<Integer,Integer[]>() {{
            put(0, new Integer[]{-10,10,10});
            put(1, new Integer[]{0,5,10});
            put(2, new Integer[]{0,1,10});
            put(3, new Integer[]{10,8,10});
            put(4, new Integer[]{5,8,10});
            put(5, new Integer[]{-20,0,10});
        }};
        assertEquals(0, player.calcBestMove(scores));
    }

    @Test
    public void testcalcBestMove2() throws Exception {
        System.out.println("test2");
        this.player = new Player(1);
        Map<Integer,Integer[]> scores = new HashMap<Integer,Integer[]>() {{
            put(0, new Integer[]{-10,4,10});
            put(1, new Integer[]{0,5,10});
            put(2, new Integer[]{0,1,10});
            put(3, new Integer[]{10,8,10});
            put(4, new Integer[]{5,8,10});
            put(5, new Integer[]{-20,0,10});
        }};
        assertEquals(3, player.calcBestMove(scores));
    }

    public void testcalcBestMove3() throws Exception {
        System.out.println("test3");
        this.player = new Player(1);
        Map<Integer,Integer[]> scores = new HashMap<Integer,Integer[]>() {{
            put(0, new Integer[]{-10,2,10});
            put(1, new Integer[]{0,5,10});
            put(2, new Integer[]{0,1,10});
            put(3, new Integer[]{5,8,10});
            put(4, new Integer[]{10,8,10});
            put(5, new Integer[]{-20,0,10});
        }};
        assertEquals(4, player.calcBestMove(scores));
    }
}
