import binarytree.*;
import java.util.*;

public class TestingAssignment3<T> {

	// creating the tree
	//     5
	//    / \
	//   2   6
	//  / \   
	// 1   4
	//    / \  
	//   8   -2
	public static BinaryTree<Integer> createTree() {
		BTNode<Integer> node8 = new BTNode<Integer>(8);
		BTNode<Integer> nodeNegative2 = new BTNode<Integer>(-2);
		BTNode<Integer> node4 = new BTNode<Integer>(4, node8, nodeNegative2, null);

		BTNode<Integer> node1 = new BTNode<Integer>(1);
		BTNode<Integer> node2 = new BTNode<Integer>(2, node1, node4, null);

		BTNode<Integer> node6 = new BTNode<Integer>(6);
		BTNode<Integer> node5 = new BTNode<Integer>(5, node2, node6, null);

		return new BinaryTree<Integer>(node5);
	}

	public static void testNumberOfLeaves() {
		BinaryTree<Integer> tree = createTree();
		if (tree.numberOfLeaves() == 4)
			System.out.println("numberOfLeaves OK");
		else
			System.out.println("numberOfLeaves ERROR");
	}

	public static void testCountDepthK() {
		BinaryTree<Integer> tree = createTree();
		if (tree.countDepthK(0) == 1 && tree.countDepthK(2) == 2 && tree.countDepthK(5) == 0)
			System.out.println("countDepthK OK");
		else
			System.out.println("countDepthK ERROR");
	}

	public static void testMap() {
		BinaryTree<Integer> tree = createTree();
		tree.map(x -> x * 2);
		BTNode<Integer> root = tree.getRoot();
		BTNode<Integer> ll = tree.getRoot().getLeftChild().getLeftChild();
		BTNode<Integer> lr = tree.getRoot().getLeftChild().getRightChild();
		BTNode<Integer> r = tree.getRoot().getRightChild();

		if (root.getData() == 10 && ll.getData() == 2 && lr.getData() == 8 && r.getData() == 12)
			System.out.println("map OK");
		else
			System.out.println("map ERROR");
	}

	public static void testPathFromRoot() {
		BinaryTree<Integer> tree = createTree();
		BTNode<Integer> negativeTwo = tree.getRoot().getLeftChild().getRightChild().getRightChild();
		List<BTNode<Integer>> l = tree.pathFromRoot(negativeTwo);
		if (l.size() == 4
				&& l.get(0) == tree.getRoot()
				&& l.get(1) == tree.getRoot().getLeftChild()
				&& l.get(3) == negativeTwo)
			System.out.println("pathFromRoot OK");
		else
			System.out.println("pathFromRoot ERROR");
	}

	public static void testDistance() {
		BinaryTree<Integer> tree = createTree();
		BTNode<Integer> five = tree.getRoot();
		BTNode<Integer> negativeTwo = five.getLeftChild().getRightChild().getRightChild();
		BTNode<Integer> six = five.getRightChild();

		BTNode<Integer> eleven = new BTNode<Integer>(11);

		try {
			tree.distance(five, eleven);
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			System.out.println("distance: wrong exception");
			return;
		}

		if (tree.distance(five, six) == 1
				&& tree.distance(negativeTwo, five) == 3
				&& tree.distance(negativeTwo, six) == 4)
			System.out.println("distance OK");
		else
			System.out.println("distance ERROR");
	}

	public static void testPreOrderIterator() {
		BinaryTree<Integer> tree = createTree();
		//     5
		//    / \
		//   2   6
		//  / \   
		// 1   4
		//    / \  
		//   8   -2
		Iterator<Integer> it = tree.preOrderIterator();
		// [5,2,1,4,8,-2,6]
		int firstFour[] = {5,2,1,4};
		boolean flag = true;
		
		for (int i = 0; i < 4; i++)
			if (!it.hasNext() || it.next()!=firstFour[i])
					flag = false;
		// it has [8,-2,6] left
		// adding more nodes under 6
		BTNode<Integer> six = tree.getRoot().getRightChild();
		six.setLeftChild(new BTNode<Integer>(100));
		six.setRightChild(new BTNode<Integer>(-50));
		//        5
		//      /   \
		//   2        6
		//  / \     /   \ 
		// 1   4  100   -50
		//    / \  
		//   8   -2


		// it has [8,-2,6,100,-50] left
		int last5[] = {8,-2,6,100,-50};
		for (int i = 0; i < 5; i++)
			if (!it.hasNext() || it.next()!=last5[i])
					flag = false;
		if (it.hasNext())
			flag = false;

		if (flag)
			System.out.println("preOrderIterator OK");
		else
			System.out.println("preOrderIterator ERROR");
	}

	public static void main(String[] args) {
		testNumberOfLeaves();
		testCountDepthK();
		//testMap();
		testPathFromRoot();

		BinaryTree<Integer> tree = createTree();
		BTNode<Integer> five = tree.getRoot();
		BTNode<Integer> negativeTwo = five.getLeftChild().getRightChild().getRightChild();
		BTNode<Integer> six = five.getRightChild();
		BTNode<Integer> eight = five.getLeftChild().getRightChild().getLeftChild();

		tree.distance(five, six);
		testDistance();
		//testPreOrderIterator();
	}

}
