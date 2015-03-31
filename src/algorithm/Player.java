package algorithm;
//	MOVES:
//	House|5|4|3|2|1|0|			player 2
//	 Turn|0|1|2|3|4|5|House		player 1

//	CELLS: yx
//	 00|01|02|03|04|05|06|07
//	 10|11|12|13|14|15|16|17

//Board[y][x]

import java.util.Arrays;
import java.util.Scanner;

class Player{

    public final int LOOKAHEAD = 1;
    public static int playerID;

    public static int convertMoveToCell(int move, int turn){
        int cell;

        if (turn == 1) cell = move + 1;
        else cell = (5 - move) + 1;

        return cell;
    }


    //GETS THE OPPOSITE SIDE OF THE SIDE PASSED TO IT
    public static int switchSide(int side){
        if (side == 1){side = 0;}
        else {side = 1;}

        return side;
    }

    //ALLOWS SEEDS TO LOOP AROUND BOARD WHEN BEING PLANTED
    public static int[] loopfix(int cellx, int celly){
        if (cellx > 7){cellx = 6; celly = switchSide(celly);}
        if (cellx < 0){cellx = 1; celly = switchSide(celly);}
        int[] returnArray = {cellx,celly};
        return returnArray;
    }

    /**********************************************************************
     ***********************CHECK IF THE GAME IS OVER***********************
     **********************************************************************/
    public static boolean isGameOver(int[][] board){

        int seedsOnside = 0;
        boolean gameover = false;

        //check side 0 empty
        for (int i = 1; i < 7; i ++){
            seedsOnside += board[0][i];
        }
        if (seedsOnside == 0) gameover = true;

        //check side 1 empty
        seedsOnside = 0;
        for (int i = 1; i < 7; i ++){
            seedsOnside += board[1][i];

        }
        if (seedsOnside == 0) gameover = true;
        return gameover;
    }


    /**********************************************************************
     ****************WHIPES THE BOARD WHEN THE GAME IS OVER*****************
     **********************************************************************/
    public static int[][] wipeBoard(int[][] board){
        int seeds = 0;
        for (int side = 0; side < 2; side ++){
            for (int cell = 1; cell < 7; cell ++){
                if (side == 0) board[side][0] += board[side][cell];
                if (side == 1) board[side][7] += board[side][cell];
                board[side][cell] = 0;
            }
        }
        return board;
    }

    public static double[] getValueOfMove (int move, int player, int[][] board){
        int[][] newBoard = move(move, board, player);
        int newturn = newBoard[1][0];
        double[] moveValueArray = new double[4];
        double[] tempArray = new double[4];
        int newPlayer = newBoard[1][0];

        //basecase
        if (newturn == 0 || newBoard[0][0] > 18 || newBoard[1][7] > 18) {
            if (playerID == 1){
                moveValueArray[0] = newBoard[1][7] - newBoard[0][0];
                if (newBoard[1][7] > newBoard[0][0]) moveValueArray[1] = 1;
                else if (newBoard[1][7] == newBoard[0][0]) moveValueArray[1] = 0.5;
                else moveValueArray[1] = 0;
                moveValueArray[2] = 1;
                moveValueArray[3] = 1;
            } else {
                moveValueArray[0] = newBoard[0][0] - newBoard[1][7];
                if (newBoard[0][0] > newBoard[1][7]) moveValueArray[1] = 1;
                else if (newBoard[0][0] == newBoard[1][7]) moveValueArray[1] = 0.5;
                else moveValueArray[1] = 0;
                moveValueArray[2] = 1;
                moveValueArray[3] = 1;

            }
        }else { // if game not over
            if (newPlayer == 1){
                for (int nextSimMove = 0; nextSimMove < 6; nextSimMove ++){
                    if (newBoard[1][convertMoveToCell(nextSimMove,1)] != 0){
                        //System.out.println("TRYING MOVE: " + nextSimMove + " PLAYER: " + newPlayer);
                        tempArray = getValueOfMove(nextSimMove,newPlayer,newBoard);
                        if (newPlayer == 1 && tempArray[1] == 1 && tempArray[2] == 1 && tempArray[3] == 1) {
                            moveValueArray[0] = tempArray[0];
                            moveValueArray[1] = 1;
                            moveValueArray[2] = 1;
                            moveValueArray[3] = 0;
//                            System.out.println("PRUNING OUTPUT");
                            return moveValueArray;
                        }else if (newPlayer == 2 && tempArray[1] == 0 && tempArray[2] == 1 && tempArray[3] == 1){
                            moveValueArray[0] = tempArray[0];
                            moveValueArray[1] = 0;
                            moveValueArray[2] = 1;
                            moveValueArray[3] = 0;
//                            System.out.println("PRUNING 2 OUTPUT");
                            return moveValueArray;
                        }else {
                            moveValueArray[0] += tempArray[0];
                            moveValueArray[1] += tempArray[1];
                            moveValueArray[2] += tempArray[2];
                            moveValueArray[3] = 0;
                        }
                    }else {
                        tempArray[0] = -999999999;
                        tempArray[1] = -999999999;
                        tempArray[2] = -999999999;
                        tempArray[3] = -999999999;

                    }
                }
            } else {
                for (int nextSimMove = 0; nextSimMove < 6; nextSimMove ++){
                    if (newBoard[0][convertMoveToCell(nextSimMove,0)] != 0){
                        //System.out.println("TRYING MOVE: " + nextSimMove + " PLAYER: " + newPlayer);
                        tempArray = getValueOfMove(nextSimMove,newPlayer,newBoard);
                        if (tempArray[1] == 1 && tempArray[2] == 1 && tempArray[3] == 1){
                            moveValueArray[0] = tempArray[0];
                            moveValueArray[1] = 1;
                            moveValueArray[2] = 1;
                            moveValueArray[3] = 0;
//                            System.out.println("PRUNING OUTPUT");
                            return  moveValueArray;

                        }else if (newPlayer == 2 && tempArray[1] == 0 && tempArray[2] == 1 && tempArray[3] == 1){
                            moveValueArray[0] = tempArray[0];
                            moveValueArray[1] = 0;
                            moveValueArray[2] = 1;
                            moveValueArray[3] = 0;
//                            System.out.println("PRUNING OPP OUTPUT");
                            return moveValueArray;
                        }else {
                            moveValueArray[0] += tempArray[0];
                            moveValueArray[1] += tempArray[1];
                            moveValueArray[2] += tempArray[2];
                            moveValueArray[3] = 0;


                        }
                    }else {
                        tempArray[0] = -999999999;
                        tempArray[1] = -999999999;
                        tempArray[2] = -999999999;
                        tempArray[3] = -999999999;
                    }
                }
            }
        }
        return moveValueArray;
    }

    public static int[] getValueOfMove2 (int move, int player, int[][] board, int lookahead){
        int[][] newBoard = move(move, board, player);
        int newturn = newBoard[1][0];
        int[] moveValue = new int[2];
        int[] tempValue = new int[2];
        int newPlayer = newBoard[1][0];
        if (newPlayer != player) lookahead --;

        //basecase
        if (newturn == 0 || newBoard[0][0] > 18 || newBoard[1][7] > 18 || lookahead == 0) {
            if (playerID == 1){
                moveValue[0] = newBoard[1][7] - newBoard[0][0];
                moveValue[1] = 1;
            } else {
                moveValue[0] = newBoard[0][0] - newBoard[1][7];
                moveValue[1] = 1;
            }
        }else { // if game not over
            if (newPlayer == 1){
                for (int nextSimMove = 0; nextSimMove < 6; nextSimMove ++){
                    if (newBoard[1][convertMoveToCell(nextSimMove,1)] != 0){
                        //System.out.println("TRYING MOVE: " + nextSimMove + " PLAYER: " + newPlayer);
                        tempValue = getValueOfMove2(nextSimMove,newPlayer,newBoard,lookahead);
                        moveValue[0] += tempValue[0];
                        moveValue[1] += tempValue[1];

                    }else {
                        tempValue[0] = -999999999;
                        tempValue[1] = -999999999;

                    }
                }
            } else {
                for (int nextSimMove = 0; nextSimMove < 6; nextSimMove ++){
                    if (newBoard[0][convertMoveToCell(nextSimMove,0)] != 0){
                        //System.out.println("TRYING MOVE: " + nextSimMove + " PLAYER: " + newPlayer);
                        tempValue = getValueOfMove2(nextSimMove,newPlayer,newBoard, lookahead);
                        moveValue[0] += tempValue[0];
                        moveValue[1] += tempValue[1];

                    } else {
                        tempValue[0] = -999999999;
                        tempValue[1] = -999999999;

                    }
                }
            }
        }
        return moveValue;
    }


    public static double[][] getMoveArray(int[][] board){
        double[][] moveArray = new double[3][6];
        double[] moveValueArray;

        if (playerID == 1){
            for (int i = 0; i < 6; i ++){
                System.out.println("*************************************************************");
                System.out.println("******************  TRYING MOVE " + i + " Player1  ******************");
                System.out.println("*************************************************************");

                if (board[1][i + 1] == 0){
                    moveArray[0][i] = -999999999;
                    moveArray[1][i] = -999999999;
                    moveArray[2][i] = -999999999;
                } else {
                    moveValueArray = getValueOfMove(i, 1, board);

                    moveArray[0][i] = moveValueArray[0];
                    moveArray[1][i] = moveValueArray[1];
                    moveArray[2][i] = moveValueArray[2];
                }
            }
        }else{
            for (int i = 0; i < 6; i ++){
                System.out.println("*************************************************************");
                System.out.println("**********************  TRYING MOVE " + i + "  ********************");
                System.out.println("*************************************************************");

                if (board[0][i + 1] == 0){
                    moveArray[0][i] = -999999999;
                    moveArray[1][i] = -999999999;
                    moveArray[2][i] = -999999999;
                } else {
                    moveValueArray = getValueOfMove(i, 2, board);

                    moveArray[0][i] = moveValueArray[0];
                    moveArray[1][i] = moveValueArray[1];
                    moveArray[2][i] = moveValueArray[2];
                }
            }
        }

        System.out.println("getMoveArray RETURNING: ");
        for (int p = 0; p < 3;p++){
            for (int q = 0; q < 6; q ++){
                if (moveArray[p][q] == -999999999) System.out.print("|X");
                else System.out.print("|" + moveArray[p][q]);
            }
            System.out.print("| \n");
        }
        return moveArray;
    }

    public static int[][] getMoveArray2(int[][] board){
        int[][] moveArray = new int[2][6];
        int[] moveValue = new int[2];
        int seedsNotInPlay = board[0][0] + board[1][7];
        int lookAhead;
        int addVal;

        if (seedsNotInPlay > 17){
            addVal = (int)Math.floor((double)(seedsNotInPlay)/3);
            lookAhead = 11 + addVal;
        }
        if (seedsNotInPlay > 14){
            addVal = (int)Math.floor((double)(seedsNotInPlay - 1)/5);
            lookAhead = 7 + addVal;
        }
        else if (seedsNotInPlay > 11){
            addVal = (int)Math.floor((double)(seedsNotInPlay - 1)/7);
            lookAhead = 8 + addVal;
        }
        else lookAhead = 8;

        if (playerID == 1){
            for (int i = 0; i < 6; i ++){
                System.out.println("*************************************************************");
                System.out.println("******************  TRYING MOVE " + i + " Player1  ******************");
                System.out.println("*************************************************************");

                if (board[1][i + 1] == 0){
                    moveArray[0][i] = -999999999;
                    moveArray[1][i] = -999999999;
                } else {


                    moveValue = getValueOfMove2(i, 1, board, lookAhead);

                    moveArray[0][i] = moveValue[0];
                    moveArray[1][i] = moveValue[1];


                }
            }
        }else{
            for (int i = 0; i < 6; i ++){
                System.out.println("*************************************************************");
                System.out.println("******************  TRYING MOVE " + i + " Player1  ******************");
                System.out.println("*************************************************************");

                if (board[1][i + 1] == 0){
                    moveArray[0][i] = -999999999;
                    moveArray[1][i] = -999999999;
                } else {
                    moveValue = getValueOfMove2(i, 2, board, lookAhead);

                    moveArray[0][i] = moveValue[0];
                    moveArray[1][i] = moveValue[1];

                }
            }
        }

        System.out.println("getMoveArray RETURNING: ");
        for (int p = 0; p < 2;p++){
            for (int q = 0; q < 6; q ++){
                if (moveArray[p][q] == -999999999) System.out.print("|X");
                else System.out.print("|" + moveArray[p][q]);
            }
            System.out.print("| \n");
        }
        return moveArray;
    }

    public static int[][] getMoveArray3(int[][] board){
        int[][] moveArray = new int[2][6];
        int[] moveValue = new int[2];
        int seedsNotInPlay = board[0][0] + board[1][7];
        int lookAhead;
        int addVal;

        if (seedsNotInPlay > 17){
            addVal = (int)Math.floor((double)(seedsNotInPlay)/3);
            lookAhead = 11 + addVal;
        }
        if (seedsNotInPlay > 14){
            addVal = (int)Math.floor((double)(seedsNotInPlay - 1)/5);
            lookAhead = 7 + addVal;
        }
        else if (seedsNotInPlay > 11){
            addVal = (int)Math.floor((double)(seedsNotInPlay - 1)/7);
            lookAhead = 8 + addVal;
        }
        else lookAhead = 8;

        if (playerID == 1){
            for (int i = 0; i < 6; i ++){
//                System.out.println("*************************************************************");
//                System.out.println("******************  TRYING MOVE " + i + " Player1  ******************");
//                System.out.println("*************************************************************");

                if (board[1][i + 1] == 0){
                    moveArray[0][i] = -999999999;
                    moveArray[1][i] = -999999999;
                } else {


                    moveValue = getValueOfMove2(i, 1, board, 2);

                    moveArray[0][i] = moveValue[0];
                    moveArray[1][i] = moveValue[1];


                }
            }
        }else{
            for (int i = 0; i < 6; i ++){
//                System.out.println("*************************************************************");
//                System.out.println("******************  TRYING MOVE " + i + " Player1  ******************");
//                System.out.println("*************************************************************");

                if (board[1][i + 1] == 0){
                    moveArray[0][i] = -999999999;
                    moveArray[1][i] = -999999999;
                } else {
                    moveValue = getValueOfMove2(i, 2, board, 2);

                    moveArray[0][i] = moveValue[0];
                    moveArray[1][i] = moveValue[1];

                }
            }
        }

        System.out.println("getMoveArray RETURNING: ");
        for (int p = 0; p < 2;p++){
            for (int q = 0; q < 6; q ++){
                if (moveArray[p][q] == -999999999) System.out.print("|X");
                else System.out.print("|" + moveArray[p][q]);
            }
            System.out.print("| \n");
        }
        return moveArray;
    }


    public static int makePlay(int[][] board){
        double bestWinRatio = -999999999;
        int bestMove = -999999999;
        int oppBestMove = -999999999;
        double bestScoreRatio = -999999999;
        double oppBestScoreRatio = -999999999;
        double oppAverageScore = 0;
        int bestScore = -999999999;
        int[][] specialBoard1 = new int[][]{
                {5,4,4,0,4,4,0,0},
                {1,3,3,0,3,3,3,0}
        };
        int[][] specialBoard2 = new int[][]{
                {5,4,4,0,4,4,0,0},
                {2,3,3,0,3,3,3,0}
        };
        int[][] specialBoard3 = new int[][]{
                {0,3,3,3,3,3,3,0},
                {1,3,3,3,3,3,3,0}
        };
        int[][] specialBoard4 = new int[][]{
                {0,3,3,3,3,3,3,0},
                {1,3,3,3,0,4,4,1}
        };
        double scoreRatio = -999999999;
        double oppScoreRatio = -999999999;
        double winRatio;
        double[] scoreArray = new double[6];



        if (Arrays.deepEquals(board, specialBoard1) || Arrays.deepEquals(board, specialBoard2)) {
            System.out.println("SPECIAL MOVE");
            bestMove = 3;
            System.out.println("Best Move: " + bestMove);
            return bestMove;
        }

        if (Arrays.deepEquals(board, specialBoard3)) {
            System.out.println("SPECIAL MOVE");
            bestMove = 3;
            System.out.println("Best Move: " + bestMove);
            return bestMove;
        }

        if (Arrays.deepEquals(board, specialBoard4)) {
            System.out.println("SPECIAL MOVE");
            bestMove = 4;
            System.out.println("Best Move: " + bestMove);
            return bestMove;
        }

        if (36 - (board[0][0] + board[1][7]) > 10){
            System.out.println("CALLING ALG 2");
            int[][] moveValues = getMoveArray2(board);
            int[][] oppMoveValues = getMoveArray3(board);
            int validOppMoves = 0;


            System.out.println("*************************************************************");
            System.out.println("********************  ANALYSIS COMPLETE  ********************");
            System.out.println("*************************************************************");

            // Caluculate the best move

            System.out.println("Score Ratio: ");
            for (int p = 0; p < 6; p++) {
                if (moveValues[1][p] == -999999999) {scoreRatio = -999999999;}
                else scoreRatio = ((double) moveValues[0][p] / (double) moveValues[1][p]);

                if (scoreRatio > bestScoreRatio){
                    bestScoreRatio = scoreRatio;
                    bestMove = p;
                }
                if (scoreRatio == -999999999) System.out.print("|X");
                else System.out.printf("|%.2f", scoreRatio);
            }

            System.out.print("\n");
            System.out.println("Optional Score Ratio: ");
            for (int p = 0; p < 6; p++) {
                if (oppMoveValues[1][p] == -999999999) {oppScoreRatio = -999999999;}
                else oppScoreRatio = ((double) oppMoveValues[0][p] / (double) oppMoveValues[1][p]);
                if (oppScoreRatio != -999999999) {
                    oppAverageScore += oppScoreRatio;
                    validOppMoves ++;
                }
                if (oppScoreRatio > oppBestScoreRatio){
                    oppBestScoreRatio = oppScoreRatio;
                    oppBestMove = p;
                }
                if (scoreRatio == -999999999) System.out.print("|X");
                else System.out.printf("|%.2f", oppScoreRatio);
            }
            System.out.print("| \n");
            oppAverageScore = oppAverageScore/validOppMoves;
            System.out.println("OPTIONAL AVERAGE: " + oppAverageScore);
            System.out.println("Best Opp Move: " + oppBestMove + " Score: " + oppMoveValues[0][oppBestMove] + " Games: " + oppMoveValues[1][oppBestMove]);
            if (((double)oppMoveValues[0][oppBestMove]/(double)oppMoveValues[1][oppBestMove]) > (oppAverageScore + 1.0)){
                bestMove = oppBestMove;
                System.out.println("USING OPTION 2 MOVE");
            }

            System.out.println("Best Move: " + bestMove);

        }else {
            System.out.println("CALLING ALG 1");
            double[][] moveValues = getMoveArray(board);



            System.out.println("*************************************************************");
            System.out.println("********************  ANALYSIS COMPLETE  ********************");
            System.out.println("*************************************************************");

            // Caluculate the best move
            System.out.println("WIN PERCENTAGE ARRAY: ");
            for (int i = 0; i < 6; i++) {
                scoreArray[i] = (int) moveValues[0][i];
                if (moveValues[2][i] == -999999999) {winRatio = -999999999; scoreRatio = -999999999;}
                else {
                    winRatio = ((double) moveValues[1][i] / (double) moveValues[2][i]) * 100;
                    scoreRatio = (double)moveValues[0][i] / (double) moveValues[2][i];
                }
                if (winRatio > bestWinRatio) {
                    bestWinRatio = winRatio;
                    bestScoreRatio = scoreRatio;
                    bestMove = i;
                } else if (winRatio == bestWinRatio && scoreRatio > bestScoreRatio) {
                    bestScoreRatio = scoreRatio;
                    bestMove = i;
                    bestWinRatio = winRatio;
                }
                if (winRatio == -999999999) System.out.print("|X");
                else System.out.print("|" + (int) winRatio);
                scoreArray[i] = scoreRatio;
            }

            System.out.print("| \n");
            System.out.println("SCORE ARRAY: ");


            //output score array
            for (int i = 0; i < 6; i++) {
                if (scoreArray[i] == -999999999) System.out.print("|X");
                else System.out.printf("|%.2f", scoreArray[i]);
            }
            System.out.print("| \n");

            System.out.println("Best Move: " + bestMove);
        }
        return bestMove;
    }




    //change board to reflect a move
    public static int[][] move(int move, int[][] currBoard, int whosturn){    //move take in 0 - 5
        // System.out.println("MOVEING MOVE: " + move + " ON SIDE: " + whosturn);
        // System.out.println("BEFORE");
        // printBoard(currBoard);


        //make a copy of the board that we can manipulate
        int[][] newBoard = new int[2][8];
        for (int y = 0; y < 2; y ++){
            for (int x = 0; x < 8; x ++){
                newBoard[y][x] = currBoard[y][x];
            }
        }
        int cell = convertMoveToCell(move, whosturn);
        int side; //the we are placing seeds on
        if (whosturn == 2) side = 0;
        else side = 1;

        int seeds = newBoard[side][cell];
        int startingSide = side;


        /**********************************************************************
         *******************CHANGE THE BOARD TO REFLECT MOVE********************
         **********************************************************************/
        newBoard[side][cell] = 0;
        for (int i = seeds; i > 0; i --){
            if (side == 1) {cell ++;}
            if (side == 0) {cell --;}
            int[] cellArray = loopfix(cell, side);

            //skip enemy home
            if (whosturn == 1 && cellArray[0] == 0 && cellArray[1] == 0) {
                cell = 1;
                side = 1;
                //skip enemy home
            }else if (whosturn == 2 && cellArray[0] == 7 && cellArray[1] == 1){
                cell = 6;
                side = 0;
            }else{
                cell = cellArray[0];
                side = cellArray[1];
            }
            newBoard[side][cell] ++;
        }


        //Stealing points from the enemy
        if (newBoard[side][cell] == 1 && newBoard[switchSide(side)][cell] > 0 && side == startingSide && cell != 0){
            int switchValue = newBoard[switchSide(side)][cell];
            newBoard[switchSide(side)][cell] = 0;
            newBoard[side][cell] = 0;

            if (whosturn == 1) newBoard[1][7] += switchValue + 1;
            if (whosturn == 2) newBoard[0][0] += switchValue + 1;
        }

        //determine whos turn it is next
        int newturn;
        if (side == startingSide && side == 1 && cell == 7) newturn = whosturn;
        else if (side == startingSide && side == 0 && cell == 0) newturn = whosturn;
        else {
            if (whosturn == 1) newturn = 2;
            else newturn = 1;
        }


        //if game is over wipe board and set turn = 0
        if (isGameOver(newBoard) == true){ newturn = 0;
            newBoard = wipeBoard(newBoard);
            //System.out.println("GAMEOVER");
        }

        //set board for new turn
        newBoard[1][0] = newturn;

        // System.out.println("AFTER");
        // printBoard(newBoard);
        // System.out.println("*********************************************************");
        return newBoard;
    }

    public static void printBoard(int[][] board) {
        System.out.printf("   P2  :   5     4      3      2       1      0\n");
        System.out.printf("+------+------+------+------+------+------+------+------+\n");
        System.out.printf("|      |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |      |\n", board[0][1], board[0][2], board[0][3], board[0][4], board[0][5], board[0][6]);
        System.out.printf("|  %02d  |------+------+------+------+------+------|  %02d  |\n", board[0][0], board[1][7]);
        System.out.printf("|      |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |  %02d  |      |\n", board[1][1], board[1][2], board[1][3], board[1][4], board[1][5], board[1][6]);
        System.out.printf("+------+------+------+------+------+------+------+------+\n");
        System.out.printf("           0     1      2      3       4      5  :  P1\n");
    }

    public static void main(String[] args){
        playerID = 1;
        int[][] board = new int[][]{
                {0,3,3,3,3,3,3,0},
                {0,3,3,3,3,3,3,0}
        };
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter who goes first 1: us, 2: them ");
        int player = reader.nextInt();
        board[1][0] = player;
        int playAgain;





        while (board[1][0] != 0) {

            if (board[1][0] == 1) {
                do {
                    System.out.println("Press Enter to Continue");
                    new Scanner(System.in).nextLine();

                    int move = makePlay(board);
                    System.out.println("BOARD BEFORE MOVE: ");
                    printBoard(board);
                    board = move(move, board, 1);
                    System.out.println("BOARD AFTER MOVE: ");
                    printBoard(board);
                    System.out.println("MOVED: " + move);

//                    System.out.println("Play Again? 1 = yes, 2 = no");
//                    playAgain = reader.nextInt();
                } while (board[1][0] == 1);

            } else if (board[1][0] == 2) {
                do {
                    System.out.println("Please Enter Opponents Move (0-5): ");
                    int oppmove = reader.nextInt();
                    System.out.println("BOARD BEFORE MOVE: ");
                    printBoard(board);
                    board = move(oppmove, board, 2);
                    System.out.println("BOARD AFTER MOVE: ");
                    printBoard(board);

//                    System.out.println("Play Again? 1 = yes, 2 = no");
//                    playAgain = reader.nextInt();

                } while (board[1][0] == 2);
            }
        }
        System.out.println("GAME OVER");


//        makePlay(board);

    }
}