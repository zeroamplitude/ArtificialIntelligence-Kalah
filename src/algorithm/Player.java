package algorithm;

import java.util.*;
import java.util.Map.Entry;

/**
 * Player:
 * <brief description of class>
 */
public class Player {

    private class GameState {
        public int[] board;
        public int turn;
        public Queue<Integer> moves;

        public GameState(int[] board, int turn, Queue<Integer> moves) {
            this.board = board;
            this.turn = turn;
            this.moves = moves;
        }
    }

    private int playerID;

    private GameSimulator game;

    private Stack<GameState> state;

    public Player(int playerID) {
        this.playerID = playerID;
        this.game = new GameSimulator();
        this.state = new Stack<GameState>();
    }

    public int makePlay(int[] board) {

        // get all the possible moves
        Queue<Integer> moves = getMoves(playerID, board);
        Map<Integer, int[]> scores = new HashMap<Integer, int[]>();
        Integer move;
        while ((move = moves.poll()) != null) {
            int newTurn = simulateMove(move, playerID, board.clone());
            int[] newBoard = game.getBoard().clone();
            scores.put(move, simulateGame(newBoard.clone(), newTurn));
        }

        // calculate the best move
        int best = calcBestMove(scores);

        return best;
    }

    public int[] simulateGame(int[] board, int turn) {

        int[] scores = new int[4];

        // Base Case: game over
        if (turn == 0 || board[6] > 18 || board[13] > 18) {
            if (playerID == 1)
                scores[0] = board[6] - board[13];
            else
                scores[0] = board[13] = board[6];

            if (scores[0] > 0)
                scores[1] = 2;
            else if (scores[0] == 0)
                scores[1] = 1;
            else
                scores[1] = 0;

            scores[2] = 2;

            scores[3] = 1;

//            state.pop();

        } else {

            // Get all moves
            Queue<Integer> moves = getMoves(turn,board);

            state.push(new GameState(board.clone(), turn, moves));

            // loop through each move
            Integer move;
            while((move = moves.poll()) != null) {

                int nextTurn = simulateMove(move, turn, board.clone()); //state.peek().turn, state.peek().board.clone());
                int[] nextBoard = game.getBoard().clone();

                int[] tmpScores = simulateGame(nextBoard.clone(), nextTurn);


                if ((tmpScores[1] == 2) && (tmpScores[2] == 2) && (tmpScores[3] == 1)) {
                    scores[0] = tmpScores[0];
                    scores[1] = 2;
                    scores[2] = 2;
                    scores[3] = 0;
                    return scores;

                } else {
                    scores[0] += tmpScores[0];
                    scores[1] += tmpScores[1];
                    scores[2] += tmpScores[2];
                    scores[3] = 0;
                }
            }
            state.pop();

        }

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

    public int simulateMove(int move, int turn, int[] board) {
        System.out.println("Player:" + turn + " move:" + (move + 1));
        game.setGameState(board.clone(), turn);
        System.out.println("before");
        game.printBoard();
        int nextTurn = game.move(move, turn, board.clone());
        System.out.println("after");
        game.printBoard();
        return nextTurn;
    }

    public int calcBestMove(Map<Integer, int[]> scores) {
        int bestMove = 0;
        double bestRatio = 0;
        for (Entry<Integer, int[]> curScores : scores.entrySet()) {
            System.out.printf("%d | %d, %d, % d \n", curScores.getKey()+1, curScores.getValue()[0], curScores.getValue()[1], curScores.getValue()[2]/2);
            double cur = (double) curScores.getValue()[1] / (double) curScores.getValue()[2];
            if (cur > bestRatio) {
                bestRatio = cur;
                bestMove = curScores.getKey();
            }
        }
        return bestMove + 1;
    }

    public void printScores(Map<Integer, Integer[]> scores) {
        for (Integer[] score : scores.values())
            System.out.printf("%d, %d, % d", score[0], score[1], score[2]);
    }


}
