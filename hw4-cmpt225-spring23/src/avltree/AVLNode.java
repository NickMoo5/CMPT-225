package avltree;

public class AVLNode<T extends Comparable<T>> {

	private T data;
	private AVLNode<T> leftChild;
	private AVLNode<T> rightChild;
	private AVLNode<T> parent;
	private int height;

	public AVLNode(T data) {
		this.data = data;
		this.leftChild = null;
		this.rightChild = null;
		this.parent = null;
		this.height = 1;
	}

	public AVLNode(T data, AVLNode<T> left, AVLNode<T> right, AVLNode<T> parent) {
		this.data = data;
		this.leftChild = left;
		this.rightChild = right;
		this.parent = parent;
		this.height = 1;
	}

	public T getData() {
		return this.data;
	}

	public AVLNode<T> getLeftChild() {
		return this.leftChild;
	}

	public AVLNode<T> getRightChild() {
		return this.rightChild;
	}

	public AVLNode<T> getParent() {
		return this.parent;
	}
	public int getHeight() {
		return this.height;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setLeftChild(AVLNode<T> leftChild) {
		this.leftChild = leftChild;
	}

	public void setRightChild(AVLNode<T> rightChild) {
		this.rightChild = rightChild;
	}

	public void setParent(AVLNode<T> parent) {
		this.parent = parent;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean isLeaf() {
		return (getLeftChild() == null && getRightChild() == null);
	}

	public boolean isRoot() {
		return (getParent() == null);
	}

	public void printPreOrder() {
		System.out.println(this.data);

		if (leftChild != null)
			leftChild.printPreOrder();

		if (rightChild != null)
			rightChild.printPreOrder();
	}
}
