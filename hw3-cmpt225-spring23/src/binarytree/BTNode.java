package binarytree;

public class BTNode<T> {
	private T data;
	private BTNode<T> leftChild;
	private BTNode<T> rightChild;
	private BTNode<T> parent;

	public BTNode(T data) {
		this.data = data;
		this.leftChild = null;
		this.rightChild = null;
		this.parent = null;
	}

	public BTNode(T data, BTNode<T> left, BTNode<T> right, BTNode<T> parent) {
		this.data = data;
		if (left!=null)
			setLeftChild(left);
		if (right!=null)
			setRightChild(right);
		this.parent = parent;
	}

	public T getData() {
		return data;
	}

	public BTNode<T> getLeftChild() {
		return leftChild;
	}

	public BTNode<T> getRightChild() {
		return rightChild;
	}

	public BTNode<T> getParent() {
		return parent;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setLeftChild(BTNode<T> leftChild) {
		this.leftChild = leftChild;
		if (leftChild != null)
			leftChild.parent = this;
	}

	public void setRightChild(BTNode<T> rightChild) {
		this.rightChild = rightChild;
		if (rightChild != null)
			rightChild.parent = this;
	}

	public void setParent(BTNode<T> parent) {
		this.parent = parent;
	}
	
	public boolean isLeaf() {
		return (leftChild == null && rightChild == null);
	}

	public boolean isRoot() {
		return (parent == null);
	}

	/**
	 * returns the size of the subtree under this
	 * @return
	 */
	public int size() {
		int ret = 1; // one for the this
		if (leftChild != null)
			ret += leftChild.size(); // recursive call
		if (rightChild != null)
			ret += rightChild.size(); // recursive call
		return ret; 
	}

	/**
	 * returns the height of the subtree under this
	 * @return
	 */
	public int height() {
		int leftHeight = -1, rightHeight = -1;

		if (leftChild != null)
			leftHeight = leftChild.height(); // recursive call
		if (rightChild != null)
			rightHeight = rightChild.height(); // recursive call

		return Math.max(leftHeight, rightHeight) + 1; 
	}

	public void printPreOrder() {
		System.out.println(this.data);

		if (leftChild != null)
			leftChild.printPreOrder();

		if (rightChild != null)
			rightChild.printPreOrder();
	}

	public void printInOrder() {
		if (leftChild != null)
			leftChild.printInOrder();

		System.out.println(this.data);

		if (rightChild != null)
			rightChild.printInOrder();
	}


	public void printPostOrder() {
		if (leftChild != null)
			leftChild.printPostOrder();

		if (rightChild != null)
			rightChild.printPostOrder();


		System.out.println(this.data);
	}

}
