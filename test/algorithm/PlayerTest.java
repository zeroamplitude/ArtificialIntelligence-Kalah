package algorithm;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * Player Tester.
 *
 * @author <Authors name>
 * @since <pre>03/26/2015</pre>
 * @version 1.0
 */
public class PlayerTest extends TestCase {

    private Player player;

    public PlayerTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        player = new Player(1);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        player = null;
    }

    public void testGetMoves() throws Exception {
        int[] TEMP_BOARD = new int[] {3, 3, 3, 3, 3, 3, 0, 3, 3, 3, 3, 3, 3, 0};
        int move = player.makePlay(TEMP_BOARD);
        System.out.print(move);

//        TEMP_BOARD = new int[] {3, 3, 0, 4, 4, 4, 0, 3, 3, 3, 3, 3, 3, 0};
//        move = player.makePlay(TEMP_BOARD);
//        System.out.print(move);

    }

    public void testGetScore() throws Exception {
        //TODO: Test goes here...
    }

    public static Test suite() {
        return new TestSuite(PlayerTest.class);
    }
}
