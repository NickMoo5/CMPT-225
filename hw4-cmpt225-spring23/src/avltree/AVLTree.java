package avltree;

import java.util.*;

public class AVLTree<T extends Comparable<T>> {

	AVLNode<T> root;
	private int height;
	private int size;
	private AVLNode<T> minElementNode;
	
	/**
	 * creates an empty AVL tree
	 */
	public AVLTree() {
		root = null;
	}

	/**
	 * returns a node containing item in the AVL tree
	 * if item is not in the tree, throws NoSuchElementException
	 */
	public AVLNode<T> find(T item) {
		// TODO implement me
		return null;
	}

	private int getBalance(AVLNode<T> node) {
		if (node == null) {return 0;}

		return node.getLeftChild().getHeight() - node.getRightChild().getHeight();
	}

	private int max(int num1, int num2) {
		return (num1 > num2) ? num1 : num2;
	}

	private int height(AVLNode<T> node) {
		if (node == null) {return 0;}
		return node.getHeight();
	}

	private AVLNode<T> rightRotate(AVLNode<T> node) {
		AVLNode<T> newRoot = node.getLeftChild();
		AVLNode<T> tempNode = newRoot.getRightChild();

		newRoot.setParent(node.getParent());
		newRoot.setRightChild(node);
		node.setLeftChild(tempNode);

		tempNode.setParent(node);
		node.setParent(newRoot);

		if (newRoot.isRoot()) {root = newRoot;}

		return newRoot;
	}

	private AVLNode<T> leftRotate(AVLNode<T> node) {
		AVLNode<T> newRoot = node.getRightChild();
		AVLNode<T> tempNode = newRoot.getLeftChild();

		newRoot.setParent(node.getParent());
		newRoot.setLeftChild(node);
		node.setRightChild(tempNode);

		tempNode.setParent(node);
		node.setParent(newRoot);

		if (newRoot.isRoot()) {root = newRoot;}

		return newRoot;
	}

	/**
	 * adds a new item to the AVL tree
	 * duplicates are allowed
	 */
	public AVLNode<T> insert(T item) {
		if (item == null) {return null;}
		if (root == null) {
			root = new AVLNode<T>(item);
			root.setHeight(0);
			return root;
		}

		AVLNode<T> curNode = getRoot();
		AVLNode<T> parentNode = null;
		int nodeHeight = 0;

		while (curNode != null) {
			parentNode = curNode;
			if (curNode.getData().compareTo(item) > 0) {
				curNode = curNode.getLeftChild();
			} else if (curNode.getData().compareTo(item) < 0) {
				curNode = curNode.getRightChild();
			}
			nodeHeight++;
		}

		AVLNode<T> newNode = new AVLNode<T>(item);

		if (parentNode.getData().compareTo(item) > 0) {
			parentNode.setLeftChild(newNode);
		} else if (curNode.getData().compareTo(item) < 0) {
			parentNode.setRightChild(newNode);
		}
		newNode.setParent(parentNode);
		updateNodeHeights(getRoot());

		int balanceFactor = getBalance(newNode);


		return null;
	}

	private int updateNodeHeights(AVLNode<T> node) {
		if (node.isLeaf()) return 0;
		node.setHeight(1 + max(updateNodeHeights(node.getLeftChild()), updateNodeHeights(node.getRightChild())));
		return node.getHeight();
	}

	private AVLNode<T> balanceTree(AVLNode<T> node) {
		if ()
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
		return height;
	}

	/**
	 * returns the size of the tree in O(1) time
	 */
	public int size() {
		return size;
	}

	/**
	 * returns the minimal element of the tree in O(1) time
	 */
	public T getMin() {
		return minElementNode.getData();
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
		return root;
	}

}
