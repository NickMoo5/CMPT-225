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

	// creating the tree
	//       5
	//      /
	//     2
	//    /
	//   1
	//  /
	// 4
	public static BinaryTree<Integer> createTree2() {
		BTNode<Integer> node4 = new BTNode<Integer>(4);
		BTNode<Integer> node1 = new BTNode<Integer>(1, node4, null, null);

		BTNode<Integer> node2 = new BTNode<Integer>(2, node1, null, null);

		BTNode<Integer> node5 = new BTNode<Integer>(5, node2, null, null);

		return new BinaryTree<Integer>(node5);
	}

	// creating the tree
	//       5
	public static BinaryTree<Integer> createTree3() {
		BTNode<Integer> node5 = new BTNode<Integer>(5, null, null, null);

		return new BinaryTree<Integer>(node5);
	}

	// full tree depth 20 - approx 2,000,000 nodes
	public static BTNode<Integer> createBigChonkNode(int d) {
		BTNode<Integer> root = new BTNode<Integer>(d);
		if (d > 0) {
			root.setLeftChild(createBigChonkNode(d-1));
			root.setRightChild(createBigChonkNode(d-1));
		}
		return root;
	}

	public static BinaryTree<Integer> createBigChonkTree() {
		return new BinaryTree<Integer>(createBigChonkNode(25));
	}

	public static BinaryTree<Double> createStringTree() {
		// A string of length 1,000,000
		BTNode<Double> root = new BTNode<Double>(0.0);
		BTNode<Double> cur = root;
		for (int i = 1; i < 1000000; i++) {
  			cur.setLeftChild(new BTNode<Double>((double)i*i));
			cur = cur.getLeftChild();
	 	}
	 return new BinaryTree<Double>(root);
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
		BTNode<Integer> noExist = new BTNode<Integer>(4);
		List<BTNode<Integer>> l = tree.pathFromRoot(negativeTwo);
		if (l.size() == 4
				&& l.get(0) == tree.getRoot()
				&& l.get(1) == tree.getRoot().getLeftChild()
				&& l.get(3) == negativeTwo)
			System.out.println("pathFromRoot OK");
		else
			System.out.println("pathFromRoot ERROR");

		try {
			List<BTNode<Integer>> a = tree.pathFromRoot(noExist);
		} catch (IllegalArgumentException e) {
			System.out.println("Caught Illegal argument exception");
		}
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
			System.out.println("distance: caught node not in tree: illegal argument exception");
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

	public static void testStraightTree() {
		BinaryTree<Integer> tree = createTree2();

		if (tree.numberOfLeaves() == 1)
			System.out.println("numberOfLeaves OK");
		else
			System.out.println("numberOfLeaves ERROR");

		if (tree.countDepthK(0) == 1 && tree.countDepthK(2) == 1 && tree.countDepthK(5) == 0)
			System.out.println("countDepthK OK");
		else
			System.out.println("countDepthK ERROR");

		Iterator<Integer> it = tree.preOrderIterator();
		int firstFour[] = {5,2,1,4};
		for (int i = 0; i < 4; i++)
			if (!it.hasNext() || it.next()!=firstFour[i])
				System.out.println("Iterator ERROR 1");

		if (it.hasNext()) {
			System.out.println("Iterator ERROR");
		}
	}

	public static void testStraightTree2() {
		int sum = 0;
		BinaryTree<Integer> tree = createTree2();

		Iterator<Integer> it = tree.preOrderIterator();
		int firstFour[] = {5,2,1,4};
		for (int i = 0; i < 4; i++)
			sum = sum + it.next();
		System.out.println("SUM" + sum);


		if (it.hasNext()) {
			System.out.println("Iterator ERROR");
		}
	}

	public static void testChonkTree() {
		int sum = 0;
		BinaryTree<Integer> tree = createBigChonkTree();

		System.out.println("Number of leaves: " + tree.numberOfLeaves());
		System.out.println("Depth Count: " + tree.countDepthK(20));
		tree.map(x -> x * 2);

		BTNode<Integer> eleven = new BTNode<Integer>(11);
		BTNode<Integer> twelve = new BTNode<Integer>(12);

		try {
			System.out.println("Distance" + tree.distance(eleven, twelve));
		} catch (IllegalArgumentException e) {
			System.out.println("Caught Illegal Argument for Big Chonk Tree");
		}

		Iterator<Integer> it = tree.preOrderIterator();
		if (!it.hasNext()) System.out.println("IT ERROR 1");
		//it.next();
		for (int i=0; i < 2100000; i++) {
			if (it.hasNext()) {
				sum = sum + it.next();
			}
		}
		System.out.println(sum);
		//if (!it.hasNext()) System.out.println("IT ERROR 3");

	}

	public static void testSingleRoot() {
		BinaryTree<Integer> tree = createTree3();

		System.out.println("Number of leaves: " + tree.numberOfLeaves());
		System.out.println("Depth Count: " + tree.countDepthK(0));
		tree.map(x -> x * 2);

		BTNode<Integer> eleven = new BTNode<Integer>(11);
		try {
			System.out.println("Distance ERROR" + tree.distance(eleven, tree.getRoot()));
		} catch (IllegalArgumentException e) {
			System.out.println("Caught Illegal Argument for Tree node no exist");
		}

		System.out.println("distane between root and root: " + tree.distance(tree.getRoot(), tree.getRoot()));

		Iterator<Integer> it = tree.preOrderIterator();
		if (!it.hasNext()) System.out.println("IT ERROR 1");
		if (it.next() != 10) System.out.println("IT ERROR 2");
		if (it.hasNext()) System.out.println("IT ERROR 3");
	}

	public static void testString() {
		BinaryTree<Double> tree = createStringTree();

		double sum = 0;
		System.out.println("Number of leaves: " + tree.numberOfLeaves());
		System.out.println("Depth Count: " + tree.countDepthK(20));
		tree.map(x -> x * 2.0);

		Iterator<Double> it = tree.preOrderIterator();
		if (!it.hasNext()) System.out.println("IT ERROR 1");
		//it.next();
		for (int i=0; i < 1000000; i++) {
			if (it.hasNext()) {
				sum = sum + it.next();
			}
		}
		System.out.println(sum);
		//if (!it.hasNext()) System.out.println("IT ERROR 3");

	}

		public static void main(String[] args) {
		testNumberOfLeaves();
		testCountDepthK();
		testMap();
		testPathFromRoot();
		testDistance();
		testPreOrderIterator();

		testStraightTree();
		testChonkTree();
		testSingleRoot();
		testStraightTree2();
		testString();
	}

}
