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

	private Scanner scan; // scanner used to take inputs
	private int boardLength;
	private Board board; // current board being used in game
	private int moves, optimal; // number of moves taken and optimal number of moves to solve

	public Puzzle(int length) {
		scan = new Scanner(System.in);
		boardLength = length;
		moves = 0;
	}

	/**
	 * resets the puzzle (resets the number of moves made and creates
	 * a new random board)
	 *
	 */
	private void reset() {
		board = generateBoardArray();
		moves = 0;
		System.out.print("[puzzle has been reset] Press enter to continue...");

		scan.nextLine();

		System.out.println("This is the new board and your moves have been reset.\n" +
						   "moves = " + moves + "\n" + board);
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
		LinkedList<Integer> movesList; // LinkedList of available moves
		Iterator<Integer> iterate; // iterator of available moves
		Iterator<Board> solutionIterator; // iterator for sequence of moves to solution
		boolean validMove = false, showSolution = false; // checks if move is valid every loop and check if the player gives up
		int[] moveIndex;
		Solve solution; // Solve class used to find solution
		String moveMade = ""; // number at index to be swapped with blank

		System.out.println("[8-Puzzle Game]");
		System.out.print("press enter to start...");

		scan.nextLine();

		System.out.print("\nA random SOLVABLE(don't be butthurt if you can't solve it) board will" + 
						 "\nnow be generated. The 0 on the board will represent the blank space in" +
						 "\nan actual 8-puzzle and the available pieces to move to that spot will be" +
						 "\ndisplayed like '[5]' if you can move 5 to the blank. In addition to those" +
						 " options,\n[0] = reset the game\n[-1] = show solution from current board\n" +
						 "[-2] = exit program.\nPress enter to continue...");

		board = generateBoardArray(); // generates new solvable board

		scan.nextLine();

		System.out.println("\nThe following board is solvable and was randomly generated :");
		System.out.println("moves = 0\n" + board);

		while (!board.isGoal() && !showSolution) {
			validMove = false;
			movesList = getMoves();

			showMoves(movesList); // displays available moves

			while (!validMove && !showSolution) { // continuous loop until a valid move is made
				moveMade = scan.nextLine();

				switch (moveMade) {
					case "1":
					case "2":
					case "3":
					case "4":
					case "5":
					case "6":
					case "7":
					case "8":
					case "9":
					case "0":
					case "-1": 
					case "-2":
						int num = Integer.parseInt(moveMade);
						if (num == -2)
							exit();
						else if (num == -1) {
							showSolution = true;
							break;
						}
						else if (num == 0) {
							reset();
							break;
						}
						else
							if (movesList.contains(num)) {
								validMove = true;
								break;
							}
					default:
						System.out.println("[INVALID INPUT] Please input a valid move.");
						showMoves(movesList);
				}
			}

			/**
			 * Code that actually makes the move in the board and increments the number
			 * of moves made thus far
			 */
			if (!showSolution) {
				moveIndex = convertNumberToIndexes(Integer.parseInt(moveMade)); // gets indexes for the piece that is being moved

				System.out.print("Moving " + moveMade + " to blank space. Press enter to continue...");
				scan.nextLine();

				board.move(moveIndex[0], moveIndex[1]); // moves the piece represented by moveIndex to the blank space
				moves++;

				System.out.println("Board following move :\nmoves = " + moves + "\n" + board);
			}
		}

		// finds the shortest path from the current board to the goal board
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

			System.out.println("Yikes, couldn't finish the puzzle :/");
		}
		else
			System.out.println("Congrats on finishing the puzzle, you took " + (moves - optimal)
							    + " moves more than the optimal solution");
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
		String brk =new String(new char[65]).replace("\0", "-");
		while (iterate.hasNext())
			result.append(" [" + iterate.next() + "]\t");

		System.out.println(brk);
		System.out.println(result.toString() + " ||\t[-2] \t [-1] \t  [0]");
		System.out.println(brk);
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

	/**
	 * exits the program
	 */
	private void exit() {
		System.out.println("Ending program.");
		System.exit(0);
	}

	public static void main(String[] args) {
		Puzzle test = new Puzzle(3);

		test.play();
	}
}