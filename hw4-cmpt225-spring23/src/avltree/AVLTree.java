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

		return height(node.getLeftChild()) - height(node.getRightChild());
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

		if (!node.isRoot() && node.getData().compareTo(node.getParent().getData()) < 0) {
			node.getParent().setLeftChild(newRoot);
		} else if (!node.isRoot()){
			node.getParent().setRightChild(newRoot);
		}

		newRoot.setRightChild(node);
		node.setLeftChild(tempNode);

		if (tempNode != null) tempNode.setParent(node);
		node.setParent(newRoot);

		//node.setHeight(max(height(node.getLeftChild()), height(node.getRightChild())) + 1);
		//newRoot.setHeight(max(height(newRoot.getLeftChild()), height(newRoot.getRightChild())) + 1);

		updateNodeHeights(node);

		if (newRoot.isRoot()) {root = newRoot;}

		return newRoot;
	}

	private AVLNode<T> leftRotate(AVLNode<T> node) {
		AVLNode<T> newRoot = node.getRightChild();
		AVLNode<T> tempNode = newRoot.getLeftChild();

		newRoot.setParent(node.getParent());
		//if (!node.isRoot()) node.getParent().setLeftChild(newRoot);
		if (!node.isRoot() && node.getData().compareTo(node.getParent().getData()) < 0) {
			node.getParent().setLeftChild(newRoot);
		} else if (!node.isRoot()){
			node.getParent().setRightChild(newRoot);
		}

		newRoot.setLeftChild(node);
		node.setRightChild(tempNode);

		if (tempNode != null) tempNode.setParent(node);
		node.setParent(newRoot);

		//node.setHeight(max(height(node.getLeftChild()), height(node.getRightChild())) + 1);
		//newRoot.setHeight(max(height(newRoot.getLeftChild()), height(newRoot.getRightChild())) + 1);

		updateNodeHeights(node);

		if (newRoot.isRoot()) {root = newRoot;}

		return newRoot;
	}

	/**
	 * adds a new item to the AVL tree
	 * duplicates are allowed
	 */
	public AVLNode<T> insert(T item) {
		if (root == null) {
			root = new AVLNode<T>(item);
			root.setHeight(1);
			size++;
			updateMinElementNode();
			return root;
		}

		AVLNode<T> curNode = getRoot();
		AVLNode<T> parentNode = null;

		while (curNode != null) {
			parentNode = curNode;
			if (curNode.getData().compareTo(item) > 0) {
				curNode = curNode.getLeftChild();
			} else {
				curNode = curNode.getRightChild();
			}
		}

		AVLNode<T> newNode = new AVLNode<T>(item);
		newNode.setHeight(1);
		newNode.setParent(parentNode);

		if (parentNode.getData().compareTo(item) > 0) {
			parentNode.setLeftChild(newNode);

		} else if (parentNode.getData().compareTo(item) <= 0) {
			parentNode.setRightChild(newNode);
		}

		size++;

		//AVLNode<T> unbalancedNode = updateNodeHeights(newNode);
/*
		while (unbalancedNode != null) {
			AVLNode<T> node = balanceTree(unbalancedNode, item);
			unbalancedNode = updateNodeHeights(node);
		}
		*/

		balanceTree(newNode, item);

		updateMinElementNode();

		return null;
	}

	private AVLNode<T> balanceTree(AVLNode<T> node, T item) {
		AVLNode<T> unbalancedNode = updateNodeHeights(node);
		if (unbalancedNode != null) {
			if (getBalance(unbalancedNode) > 1 && item.compareTo(unbalancedNode.getLeftChild().getData()) < 0) {
				return rightRotate(unbalancedNode);
			}
			if (getBalance(unbalancedNode) > 1 && item.compareTo(unbalancedNode.getLeftChild().getData()) > 0) {
				//unbalancedNode.setLeftChild(leftRotate(unbalancedNode.getLeftChild()));
				//rightRotate(unbalancedNode);
				return rightRotate((leftRotate(unbalancedNode.getLeftChild())).getParent());
			}

			if (getBalance(unbalancedNode) < -1 && item.compareTo(unbalancedNode.getRightChild().getData()) > 0) {
				return leftRotate(unbalancedNode);
			}
			if (getBalance(unbalancedNode) < -1 && item.compareTo(unbalancedNode.getRightChild().getData()) < 0) {
				//unbalancedNode.setLeftChild(rightRotate(unbalancedNode.getRightChild()));
				//rightRotate(leftRotate(unbalancedNode.getLeftChild()));
				//leftRotate(unbalancedNode);
				return leftRotate(rightRotate(unbalancedNode.getRightChild()).getParent());
			}
		}
		return node;
	}

	private AVLNode<T> updateNodeHeights(AVLNode<T> node) {
		AVLNode<T> retVal = null;
		while (node != null) {
			node.setHeight(1 + (max(height(node.getLeftChild()), height(node.getRightChild()))));

			if (getBalance(node) > 1 || getBalance(node) < -1 && retVal == null) retVal = node;

			node = node.getParent();
		}
		return retVal;
	}

	private void updateMinElementNode() {
		AVLNode<T> node = getRoot();
		while(!node.isLeaf() && node.getLeftChild() != null) {
			node = node.getLeftChild();
		}
		minElementNode = node;
	}

	/**
	 * remove item from the AVL tree
	 * if item is not in the tree, throws NoSuchElementException
	 */
	public void remove(T item) {
		if (root == null) {
			root = new AVLNode<T>(item);
			root.setHeight(1);
		}

		AVLNode<T> curNode = getRoot();
		AVLNode<T> parentNode = null;

		while (curNode != null) {
			parentNode = curNode;
			if (curNode.getData().compareTo(item) > 0) {
				curNode = curNode.getLeftChild();
			} else {
				curNode = curNode.getRightChild();
			}
		}

		AVLNode<T> newNode = new AVLNode<T>(item);
		newNode.setHeight(1);
		newNode.setParent(parentNode);

		if (parentNode.getData().compareTo(item) > 0) {
			parentNode.setLeftChild(newNode);

		} else if (parentNode.getData().compareTo(item) <= 0) {
			parentNode.setRightChild(newNode);
		}

		balanceTree(newNode, item);
	}

	/**
	 * returns the height of the tree in O(1) time
	 */
	public int height() {
		return root.getHeight() - 1;
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
		ArrayList<T> ret = new ArrayList<T>();
		AVLNode<T> curNode = getRoot();
		while (curNode.getData().compareTo(k) >= 0) {
			curNode = curNode.getLeftChild();
		}

		while (curNode != null) {
			ret.add(curNode.getData());
			curNode = curNode.getParent();
		}

		return null;
	}

	public AVLNode<T> getRoot() {
		return root;
	}

}
