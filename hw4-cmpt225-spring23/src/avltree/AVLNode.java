package avltree;

public class AVLNode<T extends Comparable<T>> {

	public AVLNode(T data) {
	}

	public AVLNode(T data, AVLNode<T> left, AVLNode<T> right, AVLNode<T> parent) {
	}

	public T getData() {
		return null;
	}

	public AVLNode<T> getLeftChild() {
		return null;
	}

	public AVLNode<T> getRightChild() {
		return null;
	}

	public AVLNode<T> getParent() {
		return null;
	}

	public void setData(T data) {
	}

	public void setLeftChild(AVLNode<T> leftChild) {
	}

	public void setRightChild(AVLNode<T> rightChild) {
	}

	public void setParent(AVLNode<T> parent) {
	}
	
	public boolean isLeaf() {
		return (getLeftChild() == null && getRightChild() == null);
	}

	public boolean isRoot() {
		return (getParent() == null);
	}

}
