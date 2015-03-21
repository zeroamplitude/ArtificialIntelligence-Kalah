
/**********************************************************************
HOW TO RUN:
- create instance of Kalah algorithm
- call makePlay and pass it the current board
**********************************************************************/
class KalahAlgorithm{
	private int[] simBoard = new int[14];



	/**********************************************************************
	Basic constuctor to set the initial sate of the sim board when a new
	instance of the kalah algorithm is created
	**********************************************************************/
	public KalahAlgorithm(int[] board){
		simBoard = board;
	}

	/**********************************************************************
	sets the simBoard, the sim board is used by the get value of move class
	to determine the score and win % of making a move
	**********************************************************************/
	public void setSimBoard(int[] board){
		simBoard = board;
	}

	/**********************************************************************
	The makePlay() method takes in the current state of the board and
	determines what is the best move
	returns best move (0-6)
	**********************************************************************/
	public int makePlay(int[] board){
		double[][] movesArray = getMoveArray(board);
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
	The getMoveArray method takes in the current state of the board and 
	returns an array of length 3 that represents the value of the move
	moveArray[1]: holds the score difference between player 
	**********************************************************************/
	public double[][] getMoveArray(int[] board){
		double[][] moveArray = new double[3][6];
		double[] moveValueArray = new double[3];
		for (int i = 0; i < 6; i ++){
			int temp = i + 1;
			System.out.println("*************************************************************");
			System.out.println("**********************  TRYING MOVE " + temp + "  **********************");
			System.out.println("*************************************************************");

			if (board[i] == 0){ 
				moveArray[0][i] = -9999;
				moveArray[1][i] = -9999;
				moveArray[2][i] = -9999;
			} else {
				moveValueArray = this.getValueOfMove(i, 1, board);

				moveArray[0][i] = moveValueArray[0];
				moveArray[1][i] = moveValueArray[1];
				moveArray[2][i] = moveValueArray[2];
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
	The getValueOfMove method takes in the current the state of the board, 
	whos turn it is and what move to try and then simulates the move and
	returns an array holding the score differnce the move will result in,
	the number of possible win that move can allow, and the number of
	possible outcomes possible.
	**********************************************************************/
	public double[] getValueOfMove(int move, int player, int[] board){
		boolean cellEmpty = false;
		boolean gameOver = false;
		double[] moveValueArray = new double[3];
		double[] tempArray;
		int newPlayer = -1;

		if (player == 2) move += 7;
		if (board[move] == 0) cellEmpty = true;

		/**********************************************************************
		CALL SIMULATION ALGORITHM HERE
		**********************************************************************/
		if (!cellEmpty) newPlayer = simulateMove(move, player, board);
		/**********************************************************************
		CALL SIMULATION ALGORITHM HERE
		**********************************************************************/
		
		else {
			moveValueArray[0] = -9999;
			moveValueArray[1] = -9999;
			moveValueArray[2] = -9999;
			return moveValueArray;
		}

		int sum = 0;
		for(int i = 0; i < 13; i ++){
			if (i != 6) sum += simBoard[i];
		}

		if (sum == 0) gameOver = true;

		// if the game is over return the score and w/l of the game
		if(gameOver){
			System.out.println("GAME IS OVER");
			moveValueArray[0] = simBoard[6] - simBoard[13];
			if (simBoard[6] > simBoard[13]) moveValueArray[1] = 1;
			else if (simBoard[6] == simBoard[13]) moveValueArray[1] = 0.5;
			else moveValueArray[1] = 0;
			moveValueArray[2] = 1;
		}else {
			System.out.println("GAME IS NOT OVER");
			if (newPlayer == 1){
				for (int simMove = 0; simMove < 6; simMove ++){
					int temp = simMove + 1;
					System.out.println("TRYING MOVE: " + temp);
					tempArray = getValueOfMove(simMove, newPlayer, simBoard);
					if (tempArray[0] == -9999) moveValueArray[0] += tempArray[0];
					if (tempArray[1] == -9999) moveValueArray[1] += tempArray[1];
					if (tempArray[2] == -9999) moveValueArray[2] += tempArray[2];
				}
			} else{
				for (int simMove = 7; simMove < 13; simMove ++){
					int temp = simMove + 1;
					System.out.println("TRYING MOVE: " + temp);
					tempArray = getValueOfMove(simMove, newPlayer, simBoard);
					if (tempArray[0] == -9999) moveValueArray[0] += tempArray[0];
					if (tempArray[1] == -9999) moveValueArray[1] += tempArray[1];
					if (tempArray[2] == -9999) moveValueArray[2] += tempArray[2];
				}
			}
		}
		return moveValueArray;
	}
}