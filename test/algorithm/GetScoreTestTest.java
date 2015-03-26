package algorithm;

import algorithm.Player;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import static org.junit.Assert.assertEquals;

/** 
* GetScoreTest Tester. 
* 
* @author <Authors name> 
* @since <pre>Mar 24, 2015</pre> 
* @version 1.0 
*/ 
public class GetScoreTestTest { 

    private Player player;

    @After
    public void after() throws Exception {
        this.player = null;
    }

    @Test
    public void testGetScore() throws Exception {
        this.player = new Player(1);
        int[] GST1 = new int[] { 0, 0, 1, 0, 0, 1, 20,
                1, 0, 0, 2, 0, 0, 0};
        assertEquals(20, player.getScore(1, GST1));
    }

    @Test
    public void testGetScore2() throws Exception {
        this.player = new Player(2);
        int[] GST1 = new int[] { 0, 0, 1, 0, 0, 1, 20,
                1, 0, 0, 2, 0, 0, 0};
        assertEquals(-20, player.getScore(2, GST1));
    }

    @Test
    public void testGetScore3() throws Exception {
        this.player = new Player(1);
        int[] GST1 = new int[] { 0, 0, 1, 0, 0, 1, 5,
                1, 0, 0, 2, 0, 0, 5};
        assertEquals(0, player.getScore(1, GST1));
    }

    @Test
    public void testGetScore4() throws Exception {
        this.player = new Player(2);
        int[] GST1 = new int[] { 0, 0, 1, 0, 0, 1, 5,
                1, 0, 0, 2, 0, 0, 5};
        assertEquals(0, player.getScore(2, GST1));
    }
}
