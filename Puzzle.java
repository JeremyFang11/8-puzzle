/**
 * 8puzzle.java is a playable version of 8-puzzle game, at any move the player will be
 * offered the choice to make any of the possible moves, (displaying the
 * number of the tile that can be moved to the blank spot). In addition, 
 * the player will be allowed to quit at any point and they may choose to
 * see the shortest solution from the current state of the board
 *
 * @author Jeremy Fang
 *
 * @version 1.0
 *
 * @date 9/11/2018
 */

import java.lang.Math;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Iterator;

public class Puzzle {

	private int boardLength;
	private Board board; // current board being used in game
	private int moves;

	public Puzzle(int length) {
		boardLength = length;
		moves = 0;
	}

	/**
	 * resets the puzzle (resets the number of moves made and creates
	 * a new random board)
	 *
	 */
	public void reset() {
		board = generateBoardArray();
		moves = 0;
		System.out.println("[puzzle has been reset]");
	}


	/**
	 * creates array of size length^2 with numbers corresponding to their
	 * index. Knuth shuffle is used to randomize the array and it is subsequently
	 * used to make a 2-D array that is used to create a board
	 *
	 * @return Board random solvable board
	 */
	private Board generateBoardArray() {
		int[] temp = new int[(int) Math.pow(boardLength, 2)];
		int[][] board = new int[boardLength][];
		int k = 0;
		Board result;
		Solve solution;

		for (int i = 0; i < temp.length; i++)
			temp[i] = i;

		for (int i = 0; i < temp.length; i++) // Knuth shuffle used to randomize array
			exch(temp, i, (int) (Math.random() * i));

		for (int i = 0; i < boardLength; i++) {
			board[i] = new int[boardLength];

			for (int j = 0; j < boardLength; j++) // assigns the values in temp array to 2-D board
				board[i][j] = temp[k++];
		}

		result = new Board(board);

		solution = new Solve(result);

		// if the board generated was solvable, return it. Otherwise, return a twin of the board
		if (solution.isSolvable())
			return result;

		return result.twin();
	}

	/**
	 * Allows user to play game.
	 */
	public void play() {
		Scanner scan = new Scanner(System.in);
		LinkedList<Integer> movesList; // LinkedList of available moves
		Iterator<Integer> iterate; // iterator of available moves
		Iterator<Board> solutionIterator; // iterator for sequence of moves to solution
		boolean validMove = false, showSolution = false; // checks if move is valid every loop and check if the player gives up
		int[] moveIndex;
		Solve solution; // Solve class used to find solution
		int moveMade = 0; // number at index to be swapped with blank

		System.out.println("[8-Puzzle Game]");
		System.out.print("press enter to start...");

		scan.nextLine();

		System.out.print("\nA board will now be generated. It will be represented by a 3x3 " +
						   "square of numbers, with the '0' representing the blank space that " +
						   "is moved around.\nEvery move, the number of moves you've made will be " +
						   "displayed along with the number pieces that are movable with the current " +
						   "state of the board.\nTo show shortest path from current board solution, " +
						   "enter -1 as your move.\nPress enter to continue...");

		board = generateBoardArray(); // generates new solvable board

		scan.nextLine();

		System.out.println("\nThe following board is solvable and was randomly generated :");
		System.out.println("moves = 0\n" + board);

		while (!board.isGoal() && !showSolution) {
			System.out.println("The following moves are currently available, please enter the number " + 
						   	   "corresponding to the move you want to make :");

			validMove = false;
			movesList = getMoves();

			showMoves(movesList); // displays available moves

			while (!validMove && !showSolution) { // continuous loop until a valid move is made
				moveMade = Integer.parseInt(scan.nextLine());

				if (moveMade == -1) 
					showSolution = true;
				else if (movesList.contains(moveMade)) {
					System.out.print("A valid move [" + moveMade + "] has been made. Press enter to continue...");
					validMove = true;
				}
				else {
					System.out.println("[INVALID INPUT] Please enter one of the following valid moves :");
					showMoves(movesList);
				}
			}

			if (!showSolution) {
				moveIndex = convertNumberToIndexes(moveMade); // gets indexes for the piece that is being moved

				scan.nextLine();

				board.move(moveIndex[0], moveIndex[1]); // moves the piece represented by moveIndex to the blank space
				moves++;

				System.out.println("Board following move :\nmoves = " + moves + "\n" + board);
			}
		}

		if (showSolution) {
			solution = new Solve(board);
			solutionIterator = solution.getSequence().iterator();
			System.out.print("The solution from this board to the goal board is " + solution.getSolutionLength()
								+ " moves.\nPress enter to show...");

			scan.nextLine();

			while (solutionIterator.hasNext()) {
				System.out.println("moves = " + moves + "\n" + solutionIterator.next());
				moves++;
			}
		}
	}

	/**
	 * helper exch function used in Knuth shuffle
	 *
	 * @param arr int[] Integer array used for exchange
	 * @param i int first index
	 * @param j int second index
	 * @return int[] array arr with index i and j exchanged
	 */
	private int[] exch(int[] arr, int i, int j) {
		int temp = arr[i];

		arr[i] = arr[j];
		arr[j] = temp;

		return arr;
	}

	/**
	 * void method that displays the contents of input list. Is
	 * used to display the possible moves for the board at every loop
	 *
	 * @param list LinkedList<Integer> list of possible moves
	 */ 
	private void showMoves(LinkedList<Integer> list) {
		StringBuilder result = new StringBuilder();
		Iterator<Integer> iterate = list.iterator();

		while (iterate.hasNext())
			result.append(" [" + iterate.next() + "]\t");

		System.out.println(result.toString());
	}

	/**
	 * returns LinkedList containing the integer values of the piece to be swapped with
	 * the blank spot
	 *
	 * @return LinkedList<Integer> stack containing all possible moves
	 */
	private LinkedList<Integer> getMoves() {
		LinkedList<Integer> result = new LinkedList<Integer>();
		int[] blank = board.getBlankIndex(); // indexes of blank spot on board

		if (blank[0] != 0)
			result.add(board.getIndex(blank[0] - 1, blank[1]));
		if (blank[0] != boardLength - 1)
			result.add(board.getIndex(blank[0] + 1, blank[1]));
		if (blank[1] != 0)
			result.add(board.getIndex(blank[0], blank[1] - 1));
		if (blank[1] != boardLength - 1)
			result.add(board.getIndex(blank[0], blank[1] + 1));

		return result;
	}

	/**
	 * returns a 2 element array that contains the indexes of 
	 * the board piece with the value num.
	 *
	 * @param num int input number
	 * @return int[] 2 element array that contains indexes 
	 *		   of piece with value num
	 */
	private int[] convertNumberToIndexes(int num) {
		int[] indexes = new int[2], blank = board.getBlankIndex();

		if (blank[0] != 0)
			if (board.getIndex(blank[0] - 1, blank[1]) == num) {
				indexes[0] = blank[0] - 1;
				indexes[1] = blank[1];
			}
		if (blank[0] != boardLength - 1)
			if (board.getIndex(blank[0] + 1, blank[1]) == num) {
				indexes[0] = blank[0] + 1;
				indexes[1] = blank[1];
			}
		if (blank[1] != 0)
			if (board.getIndex(blank[0], blank[1] - 1) == num) {
				indexes[0] = blank[0];
				indexes[1] = blank[1] - 1;
			}
		if (blank[1] != boardLength - 1)
			if (board.getIndex(blank[0], blank[1] + 1) == num) {
				indexes[0] = blank[0];
				indexes[1] = blank[1] + 1;
			}

		return indexes;
	}

	public static void main(String[] args) {
		Puzzle test = new Puzzle(3);

		test.play();
	}
}