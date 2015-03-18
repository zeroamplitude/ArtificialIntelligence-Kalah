class Test{

	/*
	DISPLAYS ANY BOARD PASSED TO IT
	*/
	public static void displayBoard(int[][] array){
		for (int y = 0; y < 2; y ++){
			for (int x = 0; x < 8; x ++){
				System.out.print("|" + array[y][x]);
			}
			System.out.print("|");
			System.out.println("");
		}
	}

	/*
	GETS THE OPPOSITE OF THE SIDE PASSED TO IT
	*/
	public static int switchSide(int side){
		if (side == 1){side = 0;}
		else {side = 1;}

		return side;
	}


	/*
	ALLOWS SEEDS TO LOOP AROUND BOARD WHEN BEING PLANTED
	*/
	public static int[] loopfix(int cellx, int celly){
		if (cellx > 7){cellx = 6; celly = switchSide(celly);}
		if (cellx < 0){cellx = 1; celly = switchSide(celly);}
		int[] returnArray = {cellx,celly};
		return returnArray;
	}


	/**********************************************************************
	********SIMULATES A MOVE OF THE BOARD AND RETURNS THE NEW BOARD********
	**********************************************************************/
	public static int[][] move(int cell, int[][] currBoard, int whosTurn){

		/**********************************************************************
		********************INITIALIZE VARIABLES TO BE USED********************
		**********************************************************************/
		boolean moveAgain = false;
		boolean gameOver = false;
		int side;

		if (whosTurn == 1){side = 1;}
		else {side = 0;}

		//make a copy of the board that we can manipulate
		int[][] newBoard = new int[2][8];
			for (int y = 0; y < 2; y ++){
				for (int x = 0; x < 8; x ++){
					newBoard[y][x] = currBoard[y][x];
				}
			}

		int startingSide = side;
		int seeds = newBoard[side][cell];




		

		/**********************************************************************
		*******************CHANGE THE BOARD TO REFLECT MOVE********************
		**********************************************************************/
		newBoard[side][cell] = 0;
		for (int i = seeds; i > 0; i --){
			if (side == 1) {cell ++;}
			if (side == 0) {cell --;}
			int[] cellArray = loopfix(cell, side);
			cell = cellArray[0];
			side = cellArray[1];

			newBoard[side][cell] ++;
		}

		if (side == 1 && cell == 7){ moveAgain = true; System.out.println("Player 1 Move Again");}
		if (side == 0 && cell == 0){ moveAgain = true; System.out.println("Player 2 Move Again");}
	
		//take points from opponents side if last seed landed on an empty house
		if (newBoard[side][cell] == 1 && newBoard[switchSide(side)][cell] > 0 && side == startingSide){
			int switchValue = newBoard[switchSide(side)][cell];
			newBoard[switchSide(side)][cell] = 0;
			newBoard[side][cell] = 0;

			if (side == 1){newBoard[side][7] += switchValue + 1; System.out.println("PLAYER 1 STOLE POINTS");}
			if (side == 0){newBoard[side][0] += switchValue + 1; System.out.println("PLAYER 0 STOLE POINTS");}
		}

		//mark board for move again
		if (moveAgain == true && side == 1){newBoard[side][0] = 1;}
		if (moveAgain == true && side == 0){newBoard[side][7] = 1;}

		return newBoard;
	}

	/**********************************************************************
	************************GET THE VALUE OF A MOVE************************
	**********************************************************************/
	public static int getValueOfMove(int cell, int[][] currBoard, int whosTurn, int lookahead){
		int value = 0;
		boolean playedAgain = false;
		int turnAgainValue = 0;
		lookahead --;
		int newWhosTurn = 0;
		int[] againMoveArray;
		int againValue = 0;
		int max = -99;
		int side;
		boolean again = false;
		int empty = 0;
		boolean gameOver = false;
		boolean zeroCell = false;
		int addValue = 0;


		if (whosTurn == 1){newWhosTurn = 0;}
		else {newWhosTurn = 1;}

		if (currBoard[whosTurn][cell] == 0) zeroCell = true;

		//CHAGE BOARD TO REFLECT MOVE
		int[][] newBoard = move(cell, currBoard, whosTurn);

		
		/**********************************************************************
		***********DETERMINE IF GAME IS OVER AND WHIP BOARD IF IT IS***********
		**********************************************************************/
		if (whosTurn == 1){side = 1;}
		else {side = 0;}
		for (int p = 1; p < 7; p ++){
			if (newBoard[side][p] == 0){empty ++;}
		}

		if (empty == 6){
			gameOver = true;
			System.out.println("GAMEOVER");
		}


		if (gameOver){
			for (int q = 1; q < 7; q ++){
				addValue = newBoard[switchSide(side)][q];
				newBoard[switchSide(side)][q] = 0;
				if (switchSide(side) == 0){newBoard[switchSide(side)][0] += addValue;}
				else {newBoard[switchSide(side)][7] += addValue;}
			}	
		}



		/**********************************************************************
		*************************MOVE AGAIN IF WE CAN**************************
		**********************************************************************/
		if(newBoard[0][7] == 1|newBoard[1][0] == 1){again = true;}

		if(again == true && empty != 6){
			displayBoard(currBoard);
			System.out.println("GET VALUE OF PLAY AGAIN");
			newBoard[0][7] = 0;
			newBoard[1][0] = 0;
			playedAgain = true;

			if (side == 1){againMoveArray = getMoveArray(newBoard);}
			else {
				System.out.println("OPPONENT PLAY AGAIN SIM");
				System.out.println("OPPONENT PLAY AGAIN SIM");
				System.out.println("OPPONENT PLAY AGAIN SIM");
				againMoveArray = getOppMoveArray(newBoard);
		}

			System.out.print("again move array: ");
			for (int p = 0; p < 6; p ++){
				if (againMoveArray[p] > -36){
					max = againMoveArray[p];
				}

				againValue = max;
				System.out.print(againMoveArray[p] + " ");
				//System.out.println("Again Value: " + againValue);
			}
			System.out.println(" ");
			System.out.println("MAX: " + againValue);
		}
			if (again == true){
				newBoard[0][7] = 0;
				newBoard[1][0] = 0;
			}



		/**********************************************************************
		**********************CALCULATE SCORE OF THE MOVE**********************
		**********************************************************************/
		System.out.println("BEFORE:");
		displayBoard(currBoard);
		int scoreOld = currBoard[1][7] - currBoard[0][0];
		if (playedAgain == true) { System.out.println("old score: (before play again) " + scoreOld);}
		else{System.out.println("old score: " + scoreOld);}

		System.out.println("*****************");
		System.out.println("AFTER: ");
		displayBoard(newBoard);
		int scoreNew = newBoard[1][7] - newBoard[0][0];
		if (playedAgain == true) { System.out.println("new score: (before play again) " + scoreNew);}
		else{ System.out.println("new score: " + scoreNew);}
		
		if (playedAgain == true){
			scoreNew += againValue;
			System.out.println("new score: (After play again) " + scoreNew);
		}
		System.out.println("*****************");


		int scoreChange = scoreNew - scoreOld;
		value = scoreChange;
		System.out.println("Move " + cell + " Value is: " + value);


		/**********************************************************************
		*****************CHECK NEXT MOVE IF lookAhead is not 0*****************
		**********************************************************************/
		if (lookahead != 0){
			for (int i = 1; i < 7; i ++){
				System.out.println("*************************************************************");
				System.out.println("*****************  TRYING OPPONENT MOVE " + i + "  ******************");
				System.out.println("*************************************************************");


				if (zeroCell) value = -99;
				else value = value + getValueOfMove(i, newBoard, newWhosTurn, lookahead);
				System.out.println("RUNNING TOTAL OF CURRNT OPPONENT MOVE: " + value);
			}
		}
		System.out.println("RETURNING VALUE: " + value);
	return value;

	}

	public static int makePlay(int[][] board){
		int[] movesArray = new int[6];
		int bestMove = -99;
		int max = -99;
		movesArray = getMoveArray(board);

		System.out.println("*************************************************************");
		System.out.println("********************  ANALYSIS COMPLETE  ********************");
		System.out.println("*************************************************************");

		System.out.print("Moves Array: ");
		for (int p = 0; p < 6; p ++){
			System.out.print(movesArray[p] + " ");
			if (movesArray[p] > max){
				max = movesArray[p];
				bestMove = p + 1;
			}
		}	
	System.out.println("");
	System.out.println("Best Move: " + bestMove);

	return bestMove;

	}

	public static int[] getMoveArray(int[][] board){
		int[] array = new int[6];
		for (int i = 1; i < 7; i ++){
			System.out.println("*************************************************************");
			System.out.println("**********************  TRYING MOVE " + i + "  **********************");
			System.out.println("*************************************************************");

			if (board[1][i] == 0){ array[i-1] = -99;}
			else array[i - 1] = getValueOfMove(i, board, 1, 2);
			System.out.println(array[i-1] + " ADDED TO ARRAY");
		}
		return array;
	}

	public static int[] getOppMoveArray(int[][] board){
		int[] array = new int[6];
		for (int i = 1; i < 7; i ++){
			System.out.println("*************************************************************");
			System.out.println("******************  TRYING OPPONENT MOVE " + i + "  *******************");
			System.out.println("*************************************************************");

			if (board[0][i] == 0){ array[i-1] = -99;}
			else array[i - 1] = getValueOfMove(i, board, 0, 2);

		}
		return array;
	}



	public static void main(String[] args){
		int[][] board = new int[][]{
		{0,0,1,1,1,0,1,0},
		{0,1,1,0,0,0,1,0}
		};

		makePlay(board);


	}
}	