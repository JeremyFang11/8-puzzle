/**
 * Solve.java uses the A* algorithm to find the shortest
 * path from the input board to the solved board.
 *
 * @author Jeremy Fang
 * 
 * @version 1.0
 *
 * @date 9/10/2018
 */

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Stack;

public class Solve {

	/* LinkedList will contain all boards that were already checked 
	 * in the normal board and the twin
	 */ 
	private LinkedList<Board> used, tused;
	private boolean solvable;
	private Stack<Board> sequence; // sequence of moves to reach solution(null if it does not exist)

	/**
	 * inner class represents search node when looking for solution.
	 * Each search node contains a board state, the search node that 
	 * is the parent if this node, and the priority of the search node
	 * (lower is higher priority)
	 */
	private static class State implements Comparable<State> {
		private Board board; // state of board stored in node
		private State parent; // parent of this board state
		private int depth; // number of moves made already


		/**
		 * initializes the state with the input variables of the same 
		 * name
		 */
		private State(Board board, State parent, int depth) {
			this.board = board;
			this.parent = parent;
			this.depth = depth;
		}

		/**
		 * compares this state with another state. If this states
		 * priority is higher, 1 is returned and this state is 
		 * picked after other, if they are equal, 0 is returned and
		 * if other has lower priority -1 is returned
		 *
		 * @param other State other board stat
e		 * @return int compares this state and the input state
		 */
		public int compareTo(State other) {
			int tprio = this.depth + this.board.manhattan();
			int oprio = other.depth + other.board.manhattan();

			if (tprio > oprio)
				return 1;
			else if (tprio == oprio)
				return 0;
			else
				return -1;
		}
	}

	public Solve(Board board) {
		MinPQ<State> pq = new MinPQ<State>(); // priority queue used for finding solution board
		/* State variable used to find the lowest priority board in the queue.
		 * initialized to null before the loop is started
		 */
		State min = null;
		used = new LinkedList<Board>();

		MinPQ<State> tpq = new MinPQ<State>(); // pq that uses twin of input board
		State tmin = null;
		tused = new LinkedList<Board>();

		pq.add(new State(board, null, 0)); // add first board to the pq
		tpq.add(new State(board.twin(), null, 0)); // add first twin board to the pq

		sequence = new Stack<Board>();

		/**
		 * implementation of A* algorithm to find the shortest path from the input
		 * board to the solution board
		 */
		while (min == null || (!min.board.isGoal() && !tmin.board.isGoal())) {
			min = pq.removeMin();
			Iterator<Board> neighbors = min.board.findNeighbors().iterator();
			int depth = min.depth;

			tmin = tpq.removeMin();

			/**
			 * iterates through the neighbors of the min board and checks to see if they
			 * should be added to the queue
			 **/
			while (neighbors.hasNext()) {
				Board checking = neighbors.next();

				/**
				 * checks the following conditions to see if the board should be added
				 * 1. is it a repeat board from making a move and undoing it?
				 * 2. has that board already been reached before with a shorter path?
				 */ 
				if (min.parent == null || min.parent.parent == null || !checking.equals(min.parent.parent.board))
					if (!used.contains(checking))
						pq.add(new State(checking, min, depth + 1));
			}

			used.add(min.board); // adds that board that was just checked to the used boards

			/**
			 * runs algorithm on twin of the initial board as well. This is because
			 * when any two pieces(not the blank) in an 8-puzzle are swapped, the puzzle becomes
			 * solvable if it was unsolvable and vice versa
			 */
			neighbors = tmin.board.findNeighbors().iterator();
			depth = tmin.depth;

			while (neighbors.hasNext()) {
				Board checking = neighbors.next();

				if (tmin.parent == null || tmin.parent.parent == null || !checking.equals(tmin.parent.parent.board))
					if (!tused.contains(checking))
						tpq.add(new State(checking, tmin, depth + 1));
			}

			tused.add(tmin.board);
		}

		/**
		 * boolean finds whether or not the initial board was solvable
		 */
		if (min.board.isGoal())
			solvable = true;
		else
			solvable = false;

		if (solvable == false) // if the board is not solvable, sequence is null
			sequence = null;
		else // if the board is solvable then the sequence is stored in a stack
			while (min.board != board) {
				sequence.add(min.board);
				min = min.parent;
			}

		if (sequence != null) {
			Stack<Board> tmp = new Stack<Board>();

			/**
		 	* reverses the sequence of sequence stack so that
		 	* the solution is read from initial board to the goal
		 	* board
		 	*/
			while (!sequence.isEmpty())
				tmp.push(sequence.pop());

			sequence = tmp;
		}
	}

	/**
	 * returns whether or not the board was solvable
	 *
	 * @return boolean whether or not the board was solvable
	 */
	public boolean isSolvable() {
		return solvable;
	}

	/**
	 * returns an Iterable<Board> stack that contains the 
	 * sequence of moves that need to be made to solve the puzzle
	 *
	 * @return Iterable<Board> stack that contains the moves needed to
	 * 		   solve the puzzle(null if unsolvable)
	 */
	public Iterable<Board> getSequence() {
		return sequence;
	}

	public static void main(String[] args) {
		int[][] board = new int[3][];

		for (int i = 0; i < 3; i++)
			board[i] = new int[3];

		board[0][0] = 6;
		board[0][1] = 0;
		board[0][2] = 8;
		board[1][0] = 4;
		board[1][1] = 3;
		board[1][2] = 5;
		board[2][0] = 1;
		board[2][1] = 2;
		board[2][2] = 7;

		Board test = new Board(board);
		Solve t = new Solve(test);

		Iterator<Board> s = t.getSequence().iterator();

		while (s.hasNext())
			System.out.println(s.next());
	}
}