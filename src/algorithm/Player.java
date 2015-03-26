package algorithm;

import java.util.*;

/**
 * Player:
 * <brief description of class>
 */
public class Player {

    /**
     * An array of integers that represent the current board state
     */
    private int[] board;

    private int playerID;

    private Board simBoard;

    // An 2D array that holds game outcomes of each move
    Map<Integer, Integer[]> scores;

    public Player(int playerID) {
        this.board = new int[14];
        this.playerID = playerID;
        this.simBoard = new Board();
        this.scores = new HashMap<Integer, Integer[]>();
    }

    public int makePlay(int[] board) {
        // set board
        this.board = board;
        System.out.println("START");

        scores = simulateGame(playerID, board, true);

        // calculate the best move
        int move = calcBestMove(scores);

        scores.clear();

        return move;
    }

    public Map<Integer, Integer[]> simulateGame(int player, int[] board, boolean first) {
        if (first)
            player = playerID;

        Queue<Integer> moves = getMoves(player, board);


//
//        // initialize to 0
//        for (Integer i : moves)
//            for (int j = 0; j < 3; j++)
//                scores.put(i, (new Integer[]{0, 0, 0}));

        Iterator iter = moves.iterator();

        // while there are moves left
        while (iter.hasNext()) {
            // get the move
            int move = moves.poll();


            // simulate the move
            int turn = simulateMove(move, player, board);

            int[] tmpBoard = simBoard.convertToIntArray();

            // Base Case: game over
            if (turn == 0 || tmpBoard[6] > 18 || tmpBoard[13] > 18) {

                // get score of game
                int moveScore = getScore(player, tmpBoard);

                // count game
                int gameCount = 2;

                int winner;
                // get winner
                if (moveScore > 0)
                    winner = 2;
                else if (moveScore == 0)
                    winner = 1;
                else
                    winner = 0;

                Integer[] array = new Integer[] {moveScore, winner, gameCount};
                scores.put(move, array);



            } else {
                // recursively call simGame until complete
                simulateGame(turn, simBoard.convertToIntArray(), false);
            }
        }

        // return the fully populated scores array
        return scores;

    }

    public Queue<Integer> getMoves(int player, int[] board) {
        Queue<Integer> moves = new LinkedList<Integer>();
        for (int i = 0; i < 6; i++) {
            int pieceIndex = i;
            // adjust if player 2
            if (player == 2)
                pieceIndex += (6 - i) * 2;

            // check if the piece has seeds
            if (board[pieceIndex] > 0)
                moves.add(i); // add the piece to potential moves
        }
        return moves;
    }

    public int simulateMove(int move, int playerID, int[] board) {
        System.out.println("TRANSFER: Player: " + playerID + "MOVE: " + move);
        System.out.println("Before");
        printBoard(board);
        int turn = this.simBoard.transfer(move, playerID, board);
        System.out.println("After piece: " + move);
        printBoard(simBoard.convertToIntArray());
//        System.out.println("*****************************************************************");


        return turn;
    }

    public int getScore(int player, int[] board) {
        int pl1Score = board[6];
        int pl2Score = board[13];
        int diff;
        if (player == 1)
            diff = pl1Score - pl2Score;
        else
            diff = pl2Score - pl1Score;
        return diff;
    }

    public int calcBestMove(Map<Integer, Integer[]> scores) {
        double bestRatio = -1;
        double winRatio;
        int bestScore = -9999;
        int bestMove = 0;

        for (int i : scores.keySet()) {
            winRatio = (((double)scores.get(i)[1]) / (double)scores.get(i)[2]) * 100;
//            System.out.println("bestScore: " + bestScore);
            System.out.println(i + " Win Ratio: " + winRatio);


            if (winRatio > bestRatio){
                bestRatio = winRatio;
                bestScore = scores.get(i)[0];
                bestMove = i;
            }else if (winRatio == bestRatio && scores.get(i)[0] > bestScore){
                bestScore = scores.get(i)[0];
                bestMove = i;
                bestRatio = winRatio;
            }
        }

        return bestMove + 1;
    }

    public void printScores(Map<Integer, Integer[]> scores) {
        for (Integer[] score : scores.values())
            System.out.printf("%d, %d, % d", score[0], score[1], score[2]);
    }
}
