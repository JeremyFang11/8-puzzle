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

public class Solve {

	/**
	 * inner class represents search node when looking for solution.
	 * Each search node contains a board state, the search node that 
	 * is the parent if this node, and the priority of the search node
	 * (lower is higher priority)
	 */
	private static class State implements Comparable<State> {
		private Board board; // state of board stored in node
		private State parent; // parent of this board state
		private int priority; // manhattan priority + move

		/**
		 * compares this state with another state. If this states
		 * priority is lower, true is returned and this state is 
		 * picked before other, if they are equal, 0 is returned and
		 * if other has lower priority -1 is returned
		 *
		 * @param other State other board state
		 * @return int compares this state and the input state
		 */
		public int compareTo(State other) {
			if (this.priority < other.priority)
				return 1;
			else if (this.priority == other.priority)
				return 0;
			else
				return -1;
		}
	}
}