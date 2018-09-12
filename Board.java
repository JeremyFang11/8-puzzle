/**
 * Board.java is a class representing the state
 * of an n-puzzle board. The blank space is represented by
 * 0.
 *
 * @author Jeremy Fang
 *
 * @version 1.0
 *
 * @date 8/21/2018
 */ 

import java.util.Stack;
import java.util.Iterator;

public class Board {

	private int[][] board; // 2-D int array representing board
	private int length; // side length of board
	private int manhattanDistance; // total manhattan distance of board
	private int[] blank; // i and j values of the blank space in the board

	/**
	 * creates new instance of Board state
	 * using 2-D array provided as input.
	 * note : array is assumed to be square
	 * note : board size cannot be under 3 or over 7
	 */
	public Board(int[][] board) {
		if (board.length < 3 || board.length > 7)
			throw new IllegalArgumentException("Error: No such thing as less than"
				+ " 8-puzzle");

		this.board = new int[board.length][];
		length = board.length;
		manhattanDistance = 0;
		blank = new int[2];

		for (int i = 0; i < board.length; i++) {
			this.board[i] = new int[board.length];
			for (int j = 0; j < board.length; j++) {
				this.board[i][j] = board[i][j];
				manhattanDistance += distance(i, j);

				if (board[i][j] == 0) {
					blank[0] = i;
					blank[1] = j;
				}
			}
		}
	}

	/**
	 * helper function finds the manhattan distance from one index in the board
	 * array to where the index is supposed to be
	 * note : indexes are assumed to be in range
	 *
	 * @param x int x index
	 * @param y int y index
	 * @return int distance from where the index is supposed to be
	 */
	private int distance(int x, int y) {

		int val = board[x][y] - 1;
		int i = 0;

		if (val == -1) // empty space is being looked at
			return 0;

		while (val >= length) {
			i++;
			val -= length;
		}

		return Math.abs(i - x) + Math.abs(val - y);
	}

	/**
	 * returns the total manhattan distance of the board pieces
	 *
	 * @return int total manhattan distance of board
	 */
	public int manhattan() {
		return manhattanDistance;
	}

	/**
	 * checks if the board is in the completed state. If so, return true
	 * otherwise return false.
	 *
	 * @return boolean whether or not this board is the goal board
	 */
	public boolean isGoal() {
		int check = 1;

		for (int i = 0; i < length; i++)
			for (int j = 0; j < length; j++)
				if (board[i][j] != check++)
					if (!(i == j && i == length - 1)) // special case : it's the empty tile
						return false;

		return true;
	}

	/**
	 * checks first if the object is a a board object, if it's null
	 * and finally if both boards have the same contents.
	 *
	 * @param other Object object being checked to see if it's equal 
	 *			    to this board
	 * @return boolean whether or not they are equal
	 */
	public boolean equals(Object other) {
		if (!(other instanceof Board))
			return false;
		if (other == null)
			return false;
		else {
			Board temp = (Board) other;

			for (int i = 0; i < length; i++)
				for (int j = 0; j < length; j++)
					if (!(board[i][j] == temp.board[i][j]))
						return false;

			return true;
		}

	}

	/**
	 * individually checks all 4 possible moves to see if they are
	 * possible in the current board. After checking if the move is
	 * possible, it is added to the stack.
	 *
	 * @return Iterable<Board> stack containing all possible moves from 
	 *		   this current board
	 */
	public Iterable<Board> findNeighbors() {
		Stack<Board> result = new Stack<Board>();

		if (blank[0] != 0) {
			exch(blank[0], blank[1], blank[0] - 1, blank[1]);
			result.push(new Board(board));
			exch(blank[0], blank[1], blank[0] - 1, blank[1]);
		}

		if (blank[0] != length - 1) {
			exch(blank[0], blank[1], blank[0] + 1, blank[1]);
			result.push(new Board(board));
			exch(blank[0], blank[1], blank[0] + 1, blank[1]);
		}

		if (blank[1] != 0) {
			exch(blank[0], blank[1], blank[0], blank[1] - 1);
			result.push(new Board(board));
			exch(blank[0], blank[1], blank[0], blank[1] - 1);
		}

		if (blank[1] != length - 1) {
			exch(blank[0], blank[1], blank[0], blank[1] + 1);
			result.push(new Board(board));
			exch(blank[0], blank[1], blank[0], blank[1] + 1);
		}

		return result;
	}

	/**
	 * switches the values of board[i][j] and board[k][l]
	 *
	 * @param i x value of first index
	 * @param j y value of first index
	 * @param k x value of second index
	 * @param l y value of second index
	 */
	private void exch(int i, int j, int k, int l) {
		int temp = board[i][j];
		board[i][j] = board[k][l];
		board[k][l] = temp;
	}

	/**
	 * returns a board with two of the pieces swapped places
	 *
	 * @return Board board with two pieces swapped
	 */  
	public Board twin() {
		Board twin;

		if (blank[0] != 0) {// the blank is not of the first row
			exch(0, 0, 0, 1);
			twin = new Board(board.clone());
			exch(0, 0, 0, 1);
		}
		else {// the blank is on the first row
			exch(1, 0, 1, 1);
			twin = new Board(board.clone());
			exch(1, 0, 1, 1);
		}

		return twin;
	}

	/**
	 * changes the 2-D array of the board so that if the input
	 * x and y are 1 square distance from the blank, the piece 
	 * is moved to the blank.
	 * note: only used for the puzzle game and does not check
	 * 	     that the move is valid(doesn't go off board).
	 *
	 * @param i int x index on the board
	 * @param j int y index on the board
	 */ 
	public void move(int i, int j) {
		if (i == blank[0])
			if (j == blank[1] + 1 || j == blank[1] - 1)
				exch(blank[0], blank[1], i, j);

		if (j == blank[1])
			if (i == blank[0] + 1 || i == blank[0] - 1)
				exch(blank[0], blank[1], i, j);
	}

	/**
	 * returns the side length of the board
	 *
	 * @return int side length of board
	 */
	public int getLength() {
		return length;
	}
					
	/**
	 * returns String representation of the board state
	 *
	 * @return String string representation of the board
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++)
				if (board[i][j] < 10)
					result.append(" " + board[i][j] + " ");
				else
					result.append(board[i][j] + " ");
			result.append("\n");
		}

		return result.toString();
	}

	public static void main(String[] args) {
		int[][] board = new int[3][];

		for (int i = 0; i < 3; i++)
			board[i] = new int[3];

		board[0][0] = 6;
		board[0][1] = 10;
		board[0][2] = 8;
		board[1][0] = 4;
		board[1][1] = 3;
		board[1][2] = 5;
		board[2][0] = 1;
		board[2][1] = 2;
		board[2][2] = 7;

		Board test = new Board(board);

		System.out.println(test);
		System.out.println(test.twin());
	}
}