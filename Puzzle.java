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
import java.util.Iterator;

public class Puzzle {

	private int boardLength;
	private Board board; // current board being used in game
	private int moves;

	/**
	 * initializes a new puzzle. Creates a new random board using the input
	 * and sets moves made to 0
	 */
	public Puzzle(int length) {
		boardLength = length;
		board = generateBoardArray();
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

		System.out.println(result);

		solution = new Solve(result);

		// if the board generated was solvable, return it. Otherwise, return a twin of the board
		if (solution.isSolvable())
			return result;

		return result.twin();
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
}