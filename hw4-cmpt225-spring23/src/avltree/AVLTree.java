package avltree;

import java.util.*;

public class AVLTree<T extends Comparable<T>> {

	AVLNode<T> root;
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
	public AVLNode<T> find(T item) throws NoSuchElementException {
		AVLNode<T> curNode = getRoot();
		while (curNode.getData().compareTo(item) != 0 || curNode != null) {
			if (curNode.getData().compareTo(item) > 0) {
				curNode = curNode.getLeftChild();
			} else {
				curNode = curNode.getRightChild();
			}
		}
		if (curNode == null) throw new NoSuchElementException();
		return curNode;
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

		//balanceTree(newNode, item);
		balanceTrees(newNode);

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

	private AVLNode<T> balanceTrees(AVLNode<T> node) {
		AVLNode<T> unbalancedNode = updateNodeHeights(node);
		if (unbalancedNode != null) {
			if (getBalance(unbalancedNode) > 1 && getBalance(unbalancedNode.getLeftChild()) >= 0) {
				return rightRotate(unbalancedNode);
			}
			if (getBalance(unbalancedNode) > 1 && getBalance(unbalancedNode.getLeftChild()) < 0) {
				//unbalancedNode.setLeftChild(leftRotate(unbalancedNode.getLeftChild()));
				//rightRotate(unbalancedNode);
				return rightRotate((leftRotate(unbalancedNode.getLeftChild())).getParent());
			}

			if (getBalance(unbalancedNode) < -1 && getBalance(unbalancedNode.getRightChild()) <= 0) {
				return leftRotate(unbalancedNode);
			}
			if (getBalance(unbalancedNode) < -1 && getBalance(unbalancedNode.getRightChild()) > 0) {
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

	private AVLNode<T> getMinNodeInTree(AVLNode<T> node) {
		/*
		while (node.getLeftChild() != null) {
			node = node.getLeftChild();
		}
		return node; */

		while (node.getRightChild() != null) {
			node = node.getRightChild();
		}
		return node;
	}

	/**
	 * remove item from the AVL tree
	 * if item is not in the tree, throws NoSuchElementException
	 */
	public void remove(T item) throws NullPointerException, NoSuchElementException {
		if (getRoot() == null) throw new NullPointerException();

		AVLNode<T> curNode = getRoot();
		AVLNode<T> unbalancedNode = null;

		while (curNode.getData().compareTo(item) != 0) {
			if (curNode.getData().compareTo(item) > 0) {
				curNode = curNode.getLeftChild();
			} else {
				curNode = curNode.getRightChild();
			}
			if (curNode == null) throw new NoSuchElementException();
		}


		if (curNode.getLeftChild() != null && curNode.getRightChild() != null) {
			AVLNode minNodeInTree = getMinNodeInTree(curNode.getLeftChild());
			T minData = (T) minNodeInTree.getData();

			// -------------------------
			if (minNodeInTree.getData().compareTo(minNodeInTree.getParent().getData()) < 0) {
				minNodeInTree.getParent().setLeftChild(null);
			} else {
				minNodeInTree.getParent().setRightChild(null);
			}
			balanceTrees(minNodeInTree.getParent());
			// -------------------------


			//unbalancedNode = minNodeInTree.getParent();
			//remove(minData);
			curNode.setData(minData);
			//unbalancedNode = null;
		} else {    // if leaf or 1 child
			AVLNode<T> newChildNode = null;
			unbalancedNode = curNode.getParent();

			if (curNode.getLeftChild() == null && curNode.getRightChild() != null) {
				newChildNode = curNode.getRightChild();
			} else if (curNode.getLeftChild() == null && curNode.getRightChild() != null) {
				newChildNode = curNode.getLeftChild();
			}

			if (curNode.getData().compareTo(curNode.getParent().getData()) < 0) {
				curNode.getParent().setLeftChild(newChildNode);
			} else {
				curNode.getParent().setRightChild(newChildNode);
			}

			if (newChildNode != null) newChildNode.setParent(curNode.getParent());

		}

		size--;

		if (unbalancedNode != null) {
			balanceTrees(unbalancedNode);
		}

		updateMinElementNode();
		
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
		Collection<T> ret = new ArrayList<T>();
		AVLNode<T> curNode = getRoot();
		while (curNode.getData().compareTo(k) >= 0) {
			curNode = curNode.getLeftChild();
		}

		while (curNode != null) {
			ret.add(curNode.getData());
			curNode = curNode.getLeftChild();
		}

		return ret;
	}

	public AVLNode<T> getRoot() {
		return root;
	}

}
