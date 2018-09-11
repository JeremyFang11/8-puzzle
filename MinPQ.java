/**
 * MinPQ.java is an object file that represents a queue
 * that removes the lowest key element everytime the min function
 * is called. Repeating values in the tree are valid with equal 
 * elements going to the left.
 *
 * @author Jeremy Fang
 *
 * @version 1.0
 *
 * @date 8/20/2018
 */

@SuppressWarnings("unchecked")

public class MinPQ<E extends Comparable<E>> {

	private Node<E> root;
	private int size;

	/**
	 * inner class represents node in the tree
	 */
	private static class Node<E> {
		private E val;
		private Node<E> left, right;

		/** creates new instance of Node **/
		private Node(E val) {
			this.val = val;
			left = right = null;
		}
	}

	/**
	 * creates new instance of MinPQ
	 */
	public MinPQ() {
		root = null;
		size = 0;
	}

	/**
	 * returns the size of the queue.
	 *
	 * @return int number of nodes left in the queue
	 */
	public int size() {
		return size;
	}

	/**
	 * returns whether or not the queue is empty
	 *
	 * @return boolean whether or not the queue is empty
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * inserts the value (if the argument is valid) into the correct positon
	 * in the tree.
	 * @param val E the value being inserted into the queue
	 */
	public void add(E val) {
		if (val == null)
			throw new IllegalArgumentException("Error: Null has been passed as arguement");
		if (root == null)
			root = new Node(val);
		else
			add(root, val);
		size++;
	}

	/**
	 * helper function inserts the value into the correct position
	 *
	 * @param current Node<E> current node being looked at
	 * @param val E value being inserted into the tree
	 */
	private void add(Node<E> current, E val) {
		int cmp = val.compareTo(current.val);

		if (cmp == 1)
			if (current.right == null)
				current.right = new Node(val);
			else
				add(current.right, val);
		else
			if (current.left == null)
				current.left = new Node(val);
			else
				add(current.left, val);
	}

	/**
	 * returns the minimum element in the queue and removes
	 * it unless the queue is empty. If it is empty then null is 
	 * returned instead
	 * 
	 * @return E minimum value in the queue
	 */
	public E removeMin() {
		if (root == null)
			return null;
		
		size--;
		if (root.left == null) {
			E val = root.val;
			if (root.right == null)
				root = null;
			else
				root = root.right;
			return val;
			}
		else
			return removeMin(root);
	}	

	/**
	 * helper function that returns the minimum value in the 
	 * queue and removes it from the queue.
	 *
	 * @param current Node<E> current node being looked at
	 * @return E minimum value in the queue
	 */
	private E removeMin(Node<E> current) {
		if (current.left.left == null) {
			E val = current.left.val;
			if (current.left.right == null)
				current.left = null;
			else
				current.left = current.left.right;
			return val;
		}
		else
			return removeMin(current.left);
	}

	/**
	 * returns the minimum element in the queue. If the
	 * queue is empty then null is returned.
	 * 
	 * @return E minimum element of the queue
	 */
	public E min() {
		if (root == null)
			return null;
		else
			return min(root);
	}

	/**
	 * helper function that returns the minimum element in the queue
	 *
	 * @param current Node<E> current node being looked at
	 * @return E minimum value in the queue
	 */
	private E min(Node<E> current) {
		if (current.left == null)
			return current.val;
		else
			return min(current.left);
	}

}