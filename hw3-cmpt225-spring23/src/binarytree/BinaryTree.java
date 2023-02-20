package binarytree;

import java.util.*;
import java.util.function.Function;

public class BinaryTree<T> {

	private BTNode<T> root;

	private List<BTNode<T>> path;

	public BinaryTree(BTNode<T> root) {
		this.root = root;
	}

	public BTNode<T> getRoot() {
		return root;
	}

	public int size() {
		return root.size();
	}

	public int height() {
		return root.height();
	}

	public void printInOrder() {
		root.printInOrder();
	}

	public void printPreOrder() {
		root.printPreOrder();
	}

	public void printPostOrder() {
		root.printPostOrder();
	}

	/**************** Assignment 3 *************************/


	private int leafCount(BTNode<T> node) {
		if (node.getLeftChild() == null && node.getRightChild() == null) {
			return 1;
		} else {
			return leafCount(node.getLeftChild()) + leafCount(node.getRightChild());
		}
	}
	/**
	 * returns the number of leaves in the tree
	 */
	public int numberOfLeaves() throws NullPointerException {
		if (getRoot() == null) throw new NullPointerException();
		return leafCount(getRoot());
	}

	private int depthCount(BTNode<T> node, int k) {
		if (node == null) return 0;
		if (k == 0) return 1;
		return depthCount(node.getLeftChild(), k-1) + depthCount(node.getRightChild(), k-1);
	}

	/**
	 * returns the number of vertices at depth k if k<0 throws
	 * IllegalArgumentException
	 */
	public int countDepthK(int k)  throws NullPointerException, IllegalArgumentException{
		if (getRoot() == null) throw new NullPointerException();
		if (k < 0) {throw new IllegalArgumentException();}
		return depthCount(getRoot(), k);
	}


	public void map(Function<? super T, ? extends T> mapper)  throws NullPointerException{
		if (getRoot() == null) throw new NullPointerException();

		// TODO implement me
	}

	private List<BTNode<T>> searchTree(BTNode<T> searchNode, BTNode<T> currNode, List<BTNode<T>> path) {
		if (searchNode == currNode) return null;
		searchTree(searchNode, currNode.getLeftChild(), path);
		searchTree(searchNode, currNode.getRightChild(), path);
		return null;
	}
	/**
	 * returns a list containing the path from the root to the node if node is not
	 * in the tree, throws IllegalArgumentException
	 */
	public List<BTNode<T>> pathFromRoot(BTNode<T> node)  throws NullPointerException, IllegalArgumentException{
		if (getRoot() == null || node == null) throw new NullPointerException();
		path = new ArrayList<BTNode<T>>();
		path.add(node);

		try {
			return getPath(node, path);
		} catch(IllegalArgumentException e) {
			throw new IllegalArgumentException();
		}
	}

	private List<BTNode<T>> getPath(BTNode<T> node, List<BTNode<T>> path) {
		BTNode<T> temp;
		if (node == getRoot()) {
			Collections.reverse(path);
			return path;
		} else if (node.isRoot() && node != getRoot()) {
			throw new IllegalArgumentException();
		} else {
			temp = node.getParent();
			path.add(temp);
			return getPath(temp, path);
		}
	}

	private BTNode<T> getCommonNode(BTNode<T> commonNode, BTNode<T> node1, BTNode<T> node2) {
		if (commonNode == null) return null;
		if (node1 == commonNode || node2 == commonNode) return commonNode;

		BTNode<T> leftNode = getCommonNode(commonNode.getLeftChild(), node1, node2);
		BTNode<T> rightNode = getCommonNode(commonNode.getRightChild(), node1, node2);

		if (leftNode != null && rightNode != null) return commonNode;
		if (leftNode != null) {
			return leftNode;
		} else {
			return rightNode;
		}
	}

	private int distanceFromCommonNode(BTNode<T> commonNode, BTNode<T> node, int distance) {
		if (commonNode == null) return -1;
		if (commonNode == node) return distance;

		int left = distanceFromCommonNode(commonNode.getLeftChild(), node, distance + 1);
		int right = distanceFromCommonNode(commonNode.getRightChild(), node, distance + 1);

		if (left != -1) {
			return left;
		} else {
			return right;
		}
	}

	/**
	 * returns the distance between the two nodes if on of the nodes is not in the
	 * tree, throws IllegalArgumentException
	 */
	public int distance(BTNode<T> node1, BTNode<T> node2) throws NullPointerException, IllegalArgumentException{
		if (node1 == null || node2 == null || getRoot() == null) throw new NullPointerException();
		//System.out.println(getCommonNode(getRoot(), node1, node2).getData());
		BTNode<T> commonNode = getCommonNode(getRoot(), node1, node2);
		int distance1 = distanceFromCommonNode(commonNode, node1, 0);
		int distance2 = distanceFromCommonNode(commonNode, node2, 0);

		if (distance1 == -1 || distance2 == -1) {
			throw new IllegalArgumentException();
		}
		return distance2 + distance1;
	}

	/**
	 * returns a preOrder iterator for the tree
	 */
	public Iterator<T> preOrderIterator() {
		// TODO implement me
		return null;
	}

}
