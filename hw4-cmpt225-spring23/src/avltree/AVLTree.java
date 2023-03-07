package avltree;

import java.util.*;

public class AVLTree<T extends Comparable<T>> {

	AVLNode<T> root;
	
	/**
	 * creates an empty AVL tree
	 */
	public AVLTree() {
	}

	/**
	 * returns a node containing item in the AVL tree
	 * if item is not in the tree, throws NoSuchElementException
	 */
	public AVLNode<T> find(T item) {
		// TODO implement me
		return null;
	}

	/**
	 * adds a new item to the AVL tree
	 * duplicates are allowed
	 */
	public AVLNode<T> insert(T item) {
		// TODO implement me
		return null;
	}


	/**
	 * remove item from the AVL tree
	 * if item is not in the tree, throws NoSuchElementException
	 */
	public void remove(T item) {
		// TODO implement me
	}

	/**
	 * returns the height of the tree in O(1) time
	 */
	public int height() {
		// TODO implement me
		return -1;
	}

	/**
	 * returns the size of the tree in O(1) time
	 */
	public int size() {
		// TODO implement me in O(1) time
		return -1;
	}

	/**
	 * returns the minimal element of the tree in O(1) time
	 */
	public T getMin() {
		// TODO implement me in O(1) time
		return null;
	}

	/**
	 * returns a collection of all elements in the tree for which
	 * element.compareTo(k) < 0
	 * If the list is empty, returns an empty list 
	 */
	public Collection<T> lessThanK(T k) {
		// TODO implement me
		return null;
	}

	public AVLNode<T> getRoot() {
		// TODO implement me
		return root;
	}

}
