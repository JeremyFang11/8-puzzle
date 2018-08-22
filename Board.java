/**
 * Board.java is a class representing the state
 * of an n-puzzle.
 *
 * @author Jeremy Fang
 *
 * @version 1.0
 *
 * @date 8/21/2018
 */ 

public class Board {

	private int[][] board;
	private int length;
	private int manhattanDistance;

	/**
	 * creates new instance of Board state
	 * note : board size cannot be over 7 or under 3
	 */
	public Board(int n) {
		if (n < 3 || n > 7)
			throw new IllegalArgumentException("Error: No such thing as less than"
				+ " 8-puzzle");

		length = n;
		board = new int[n][];
		manhattanDistance = 0;

		int count = 1;

		for (int i = 0; i < n; i++) {
			board[i] = new int[n];
			for (int j = 0; j < n; j++)
				board[i][j] = count++;
		}

		board[n - 1][n - 1] = 0;
	}

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

		for (int i = 0; i < board.length; i++) {
			this.board[i] = new int[board.length];
			for (int j = 0; j < board.length; j++) {
				this.board[i][j] = board[i][j];
				manhattanDistance += distance(i, j);
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
		if (val == -1) // empty space is being looked at
			return 0;

		int val = board[x][y] - 1;
		int i = 0;

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

		for (int i = 0; i < 3; i++) {
			board[i] = new int[3];
		}

		board[0][0] = 8;
		board[0][1] = 1;
		board[0][2] = 3;
		board[1][0] = 4;
		board[1][1] = 0;
		board[1][2] = 2;
		board[2][0] = 7;
		board[2][1] = 6;
		board[2][2] = 5;

		Board test = new Board(board);

		System.out.println(test);

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				System.out.println(board[i][j] + " : " + test.distance(i, j));
	}
}