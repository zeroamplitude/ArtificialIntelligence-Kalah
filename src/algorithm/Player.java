package algorithm;

/**********************************************************************
HOW TO RUN:
- create instance of Kalah algorithm
- call makePlay and pass it the current simBoard
**********************************************************************/
public class Player {

    /**
     * An array of integers representing the current board
     */
	private int[] board;

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
        this.board = new int[14];
        this.tmpBoard  = new int[14];
        this.simBoard = new Board(this);
	}

	/**********************************************************************
	sets the board, the sim simBoard is used by the get value of move class
	to determine the score and win % of making a move
	**********************************************************************/
	public void setTmpBoard(int[] simBoard){
        board = simBoard;
	}

	/**********************************************************************
	The makePlay() method takes in the current state of the simBoard and
	determines what is the best move
	returns best move (0-6)
	**********************************************************************/
	public int makePlay(int[] simBoard){
		double[][] movesArray = getMoveArray(simBoard);
		int[] scoreArray = new int[6];
		double bestWinRatio = -9999;
		int bestMove = 0;
		int bestscore = -9999;
		double winRatio;
		System.out.println("*************************************************************");
		System.out.println("********************  ANALYSIS COMPLETE  ********************");
		System.out.println("*************************************************************");

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
	public double[][] getMoveArray(int[] simBoard){
		double[][] moveArray = new double[3][6];
		double[] moveValueArray = new double[3];

		if (this.playerID == 1){
			for (int i = 0; i < 6; i ++){
				int temp = i + 1;
				System.out.println("*************************************************************");
				System.out.println("**********************  TRYING MOVE " + temp + "  **********************");
				System.out.println("*************************************************************");

				if (simBoard[i] == 0){
					moveArray[0][i] = -9999;
					moveArray[1][i] = -9999;
					moveArray[2][i] = -9999;
				} else {
					moveValueArray = this.getValueOfMove(i, 1, simBoard);

					moveArray[0][i] = moveValueArray[0];
					moveArray[1][i] = moveValueArray[1];
					moveArray[2][i] = moveValueArray[2];
				}
			}
		}else{
			for (int i = 0; i < 6; i ++){
				int temp = i + 1;
				System.out.println("*************************************************************");
				System.out.println("**********************  TRYING MOVE " + temp + "  **********************");
				System.out.println("*************************************************************");

				if (simBoard[i + 7] == 0){
					moveArray[0][i] = -9999;
					moveArray[1][i] = -9999;
					moveArray[2][i] = -9999;
				} else {
					moveValueArray = this.getValueOfMove(i, 2, simBoard);

					moveArray[0][i] = moveValueArray[0];
					moveArray[1][i] = moveValueArray[1];
					moveArray[2][i] = moveValueArray[2];
				}
			}


		}

		System.out.println("getMoveArray RETURNING: ");
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
	public double[] getValueOfMove(int move, int player, int[] simBoard){
		boolean cellEmpty = false;
		boolean gameOver = false;
		double[] moveValueArray = new double[3];
		double[] tempArray;
		int newPlayer = -1;

		if (player == 2) move += 7;
		if (simBoard[move] == 0) cellEmpty = true;


        /**********************************************************************
		**********************CALL TRANSFER ALGORITHM HERE*********************
		**********************************************************************/
		if (!cellEmpty) newPlayer = this.simBoard.transfer(move, player,simBoard);
		/**********************************************************************
		**********************CALL TRANSFER ALGORITHM HERE*********************
		**********************************************************************/


		else {
			moveValueArray[0] = -9999;
			moveValueArray[1] = -9999;
			moveValueArray[2] = -9999;
			return moveValueArray;
		}

		int sum = 0;
		for(int i = 0; i < 13; i ++){
			if (i != 6) sum += board[i];
		}

		if (sum == 0) gameOver = true;

		// if the game is over return the score and w/l of the game
		if(gameOver){
			System.out.println("GAME IS OVER");
			if (this.playerID == 1){
				moveValueArray[0] = board[6] - board[13];
				if (board[6] > board[13]) moveValueArray[1] = 1;
				else if (board[6] == board[13]) moveValueArray[1] = 0.5;
				else moveValueArray[1] = 0;
				moveValueArray[2] = 1;
			}else {
				moveValueArray[0] = board[13] - board[6];
				if (board[13] > board[6]) moveValueArray[1] = 1;
				else if (board[13] == board[6]) moveValueArray[1] = 0.5;
				else moveValueArray[1] = 0;
				moveValueArray[2] = 1;
			}
		}else {
			System.out.println("GAME IS NOT OVER");
			if (newPlayer == 1){
				for (int simMove = 0; simMove < 6; simMove ++){
					int temp = simMove + 1;
					System.out.println("TRYING MOVE: " + temp);
					tempArray = getValueOfMove(simMove, newPlayer, board);
					if (tempArray[0] == -9999) moveValueArray[0] += tempArray[0];
					if (tempArray[1] == -9999) moveValueArray[1] += tempArray[1];
					if (tempArray[2] == -9999) moveValueArray[2] += tempArray[2];
				}
			} else{
				for (int simMove = 7; simMove < 13; simMove ++){
					int temp = simMove + 1;
					System.out.println("TRYING MOVE: " + temp);
					tempArray = getValueOfMove(simMove, newPlayer, board);
					if (tempArray[0] == -9999) moveValueArray[0] += tempArray[0];
					if (tempArray[1] == -9999) moveValueArray[1] += tempArray[1];
					if (tempArray[2] == -9999) moveValueArray[2] += tempArray[2];
				}
			}
		}
		return moveValueArray;
	}

    public static void main(String[] args) {
        Player ai = new Player(1);


    }
}