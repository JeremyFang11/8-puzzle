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
			for (int j = 0; j < n; j++) {
				board[i][j] = count++;
			}
		}
		board[n - 1][n - 1] = 0;
	}

	/**
	 * creates new instance of Board state
	 * using 2-D array provided as input.
	 * note : array is assumed to be square
	 */
	public Board(int[][] board) {
		
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
		Board test = new Board(4);

		System.out.println(test);
	}
}