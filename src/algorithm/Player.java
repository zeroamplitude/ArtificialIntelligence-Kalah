package algorithm;

public class Player {

    /**
     * An array of integers representing the current board
     */
    private int[] originalBoard;

    /**
     * An integer that represents the players id. id can be either 1 or 2.
     */
    private int playerID;

    /**
     * A simBoard
     */
    private Board simBoard;

    /**
     * A this board is changed by the Board class on a simulated move
     */
    private int[] tmpBoard;



    /**********************************************************************
     Basic constuctor to set the initial sate of the sim simBoard when a new
     instance of the kalah algorithm is created
     **********************************************************************/
    public Player(int player){
        this.playerID = player;
        this.originalBoard = new int[14];
        this.tmpBoard  = new int[14];
        this.simBoard = new Board(this);
    }

    /**********************************************************************
     sets the board, the sim simBoard is used by the get value of move class
     to determine the score and win % of making a move
     **********************************************************************/
    public void setTmpBoard(int[] boardFromTransfer){
        this.tmpBoard = boardFromTransfer;
    }


    public int makePlay(int[] originalBoard){
        double[][] movesArray = getMoveArray(originalBoard);
        int[] scoreArray = new int[6];
        double bestWinRatio = -9999;
        int bestMove = 0;
        int bestscore = -9999;
        double winRatio;
        System.out.println("*************************************************************");
        System.out.println("********************  ANALYSIS COMPLETE  ********************");
        System.out.println("*************************************************************");
        printBoard(originalBoard);
        // Caluculate the best move given the score and the win % of each move
        System.out.println("WIN PERCENTAGE ARRAY: ");
        for (int i = 0; i < 6; i ++){
            scoreArray[i] = (int)movesArray[0][i];
            if (movesArray[2][i] == -9999){winRatio = -9999;}
            else {winRatio = ((double)movesArray[1][i]/(double)movesArray[2][i]) * 100;}

            if (winRatio > bestWinRatio){
                bestWinRatio = winRatio;
                bestscore = (int)movesArray[0][i];
                bestMove = i + 1;
            }else if (winRatio == bestWinRatio && movesArray[0][i] > bestscore){
                bestscore = (int)movesArray[0][i];
                bestMove = i + 1;
                bestWinRatio = winRatio;
            }
            if (winRatio == -9999) System.out.print("|X");
            else System.out.print("|" + (int)winRatio);
        }
        System.out.print("| \n");
        System.out.println("SCORE ARRAY: ");

        for (int i = 0;i < 6;i ++){
            if (scoreArray[i] == -9999) System.out.print("|X");
            else System.out.print("|" + scoreArray[i]);
        }
        System.out.print("| \n");

        System.out.println("Best Move: " + bestMove);
        return bestMove;
    }

    /**********************************************************************
     The getMoveArray method takes in the current state of the simBoard and
     returns an array of length 3 that represents the value of the move
     moveArray[1]: holds the score difference between player
     **********************************************************************/
    public double[][] getMoveArray(int[] originalBoard){
        double[][] moveArray = new double[3][6];
        double[] moveValueArray = new double[3];

        if (this.playerID == 1){
            for (int i = 0; i < 6; i ++){
                int temp = i + 1;
                System.out.println("IN GET MOVE ARRAY 1");
                System.out.println("*************************************************************");
                System.out.println("*****************  TRYING PLAYER: " + this.playerID + " MOVE: " + temp + "  *****************");
                System.out.println("*************************************************************");

                if (originalBoard[i] == 0){
                    System.out.println("NOT A VALID MOVE");
                    moveArray[0][i] = -9999;
                    moveArray[1][i] = -9999;
                    moveArray[2][i] = -9999;
                } else {
                    moveValueArray = this.getValueOfMove(i, 1, originalBoard);

                    moveArray[0][i] = moveValueArray[0];
                    moveArray[1][i] = moveValueArray[1];
                    moveArray[2][i] = moveValueArray[2];
                }
            }
        }else{
            for (int i = 12; i > 6; i --){
                int temp = 12 - i;
                System.out.println("IN GET MOVE ARRAY 2");
                System.out.println("*************************************************************");
                System.out.println("*****************  TRYING PLAYER: " + this.playerID + " MOVE: " + temp + "  *****************");
                System.out.println("*************************************************************");
                if (originalBoard[i] == 0){
                    moveArray[0][12 - i] = -9999;
                    moveArray[1][12 - i] = -9999;
                    moveArray[2][12 - i] = -9999;
                } else {
                    moveValueArray = this.getValueOfMove(12 - i, 2, originalBoard);

                    moveArray[0][12 - i] = moveValueArray[0];
                    moveArray[1][12 - i] = moveValueArray[1];
                    moveArray[2][12 - i] = moveValueArray[2];
                }
            }


        }

        System.out.println("PLAYER " + this.playerID + " MOVE ARRAY:");
        for (int p = 0; p < 3;p++){
            for (int q = 0; q < 6; q ++){
                System.out.print("|" + moveArray[p][q]);
            }
            System.out.print("| \n");
        }
        return moveArray;
    }

    /**********************************************************************
     The getValueOfMove method takes in the current the state of the simBoard,
     whos turn it is and what move to try and then simulates the move and
     returns an array holding the score differnce the move will result in,
     the number of possible win that move can allow, and the number of
     possible outcomes possible.
     **********************************************************************/
    public double[] getValueOfMove(int move, int player, int[] currBoard){
        boolean cellEmpty = false;
        boolean gameOver = false;
        double[] moveValueArray = new double[3];
        double[] tempArray;
        int[] newBoard;
        int newPlayer = -1;
        System.out.println("GET VALUE OF MOVE TEST 1");

        //checking if the move is invalid
        if (player == 1){
            if (currBoard[move] == 0) cellEmpty = true;
        }else{
            if (currBoard[(6 - move) * 2] == 0) cellEmpty = true;
        }

        System.out.println("CURRENT BOARD BEFORE TRANSFER");
        printBoard(currBoard);


        /**********************************************************************
         **********************CALL TRANSFER ALGORITHM HERE*********************
         **********************************************************************/
        if (!cellEmpty){ //if it is a valid move

            System.out.println("TRANSFER MOVE: " + move + " PLAYER: " + player);
            newPlayer = this.simBoard.transfer(move, player,currBoard);
            newBoard = this.tmpBoard;
        }
        /**********************************************************************
         **********************CALL TRANSFER ALGORITHM HERE*********************
         **********************************************************************/


        else { // if invalid move
            moveValueArray[0] = -9999;
            moveValueArray[1] = -9999;
            moveValueArray[2] = -9999;
            return moveValueArray;
        }


        int sum = 0; //check if game is over
        for(int i = 0; i < 13; i ++){
            if (i != 6 ) sum += newBoard[i];
        }
        if (sum == 0) gameOver = true;

        System.out.println("CURRENT BOARD AFTER TRANSFER");
        printBoard(newBoard);
        // if the game is over return the score and w/l of the game
        if(gameOver){ //if game is over return win ratio and score difference
            System.out.println("GAME IS OVER");
            if (this.playerID == 1){ //Player1
                moveValueArray[0] = newBoard[6] - newBoard[13];
                if (newBoard[6] > newBoard[13]) moveValueArray[1] = 1;
                else if (newBoard[6] == newBoard[13]) moveValueArray[1] = 0.5;
                else moveValueArray[1] = 0;
                moveValueArray[2] = 1;
            }else { //player 2
                moveValueArray[0] = newBoard[13] - newBoard[6];
                if (newBoard[13] > newBoard[6]) moveValueArray[1] = 1;
                else if (newBoard[13] == newBoard[6]) moveValueArray[1] = 0.5;
                else moveValueArray[1] = 0;
                moveValueArray[2] = 1;
            }
        }else {
            System.out.println("GAME IS NOT OVER");
            if (newPlayer == 1){
                for (int simMove = 0; simMove < 6; simMove ++){
                    int temp = simMove;
                    System.out.println("*************************************************************");
                    System.out.println("XXXXXXXXXXXXXX TRYING PLAYER: " + this.playerID + " MOVE: " + temp + " XXXXXXXXXXXXXX");
                    System.out.println("*************************************************************");
                    tempArray = getValueOfMove(simMove, newPlayer, newBoard);
                    if (tempArray[0] == -9999 && tempArray[1] == -9999 && tempArray[2] == -9999)
                        System.out.println("INVALID MOVE");
                    if (tempArray[0] != -9999) moveValueArray[0] += tempArray[0];
                    if (tempArray[1] != -9999) moveValueArray[1] += tempArray[1];
                    if (tempArray[2] != -9999) moveValueArray[2] += tempArray[2];
                }
            } else{
                for (int simMove = 12; simMove > 6; simMove --){
                    int temp = 12 - simMove;
                    System.out.println("*************************************************************");
                    System.out.println("XXXXXXXXXXXXXX TRYING PLAYER: " + this.playerID + " MOVE: " + temp + " XXXXXXXXXXXXXX");
                    System.out.println("*************************************************************");
                    tempArray = getValueOfMove(12 - simMove, newPlayer, newBoard);
                    if (tempArray[0] == -9999 && tempArray[1] == -9999 && tempArray[2] == -9999)
                        System.out.println("INVALID MOVE");
                    if (tempArray[0] != -9999) moveValueArray[0] += tempArray[0];
                    if (tempArray[1] != -9999) moveValueArray[1] += tempArray[1];
                    if (tempArray[2] != -9999) moveValueArray[2] += tempArray[2];
                }
            }
        }
        return moveValueArray;
    }


    public void printBoard(int[] board) {
        System.out.printf("+------+------+------+------+------+------+------+------+\n");
        System.out.printf("|      |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |      |\n", board[7], board[8], board[9], board[10], board[11], board[12]);
        System.out.printf("|  %02d  |------+------+------+------+------+------|  %02d  |\n", board[13], board[6]);
        System.out.printf("|      |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |      |\n", board[0], board[1], board[2], board[3], board[4], board[5]);
        System.out.printf("+------+------+------+------+------+------+------+------+\n");
    }

    public static void main(String[] args) {
        Player ai = new Player(1);


    }
}