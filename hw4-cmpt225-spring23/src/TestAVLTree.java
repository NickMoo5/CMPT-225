import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import avltree.AVLNode;
import avltree.AVLTree;

public class TestAVLTree {

	public static void testInsert() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		tree.insert(10);
		tree.insert(2);
		tree.insert(3);
		tree.insert(8);
		tree.insert(5);
		tree.insert(1);
		// expected tree
		//       3
		//      / \
		//     2   8
		//    /   / \
		//   1   5   10
		
		AVLNode<Integer> r = tree.getRoot(); 
		if (r != null && r.getData() == 3
				&& r.getLeftChild() != null
				&& r.getLeftChild().getData() == 2
				&& r.getRightChild() != null
				&& r.getRightChild().getData() == 8)
			System.out.println("testInsert OK");
		else
			System.out.println("testInsert ERROR");
	}


	public static void testRemove() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		tree.insert(3);
		tree.insert(2);
		tree.insert(8);
		tree.insert(7);
		tree.insert(1);
		tree.insert(10);
		tree.insert(20);
		// expected tree
		//       3
		//      / \
		//     2   8
		//    /   / \
		//   1   7   10
		//            \
		//            20
		tree.remove(1);
		// expected tree
		//       8
		//      / \
		//     3   10
		//    / \   \
		//   2   7   20
		//            
		AVLNode<Integer> r = tree.getRoot(); 
		if (r != null && r.getData() == 8
				&& r.getLeftChild() != null
				&& r.getLeftChild().getData() == 3
				&& r.getRightChild() != null
				&& r.getRightChild().getData() == 10)
			System.out.println("testRemove 1 OK");
		else
			System.out.println("testRemove 1 ERROR");

		tree.remove(10);
		// expected tree
		//       8
		//      / \
		//     3   20
		//    / \   
		//   2   7  
		//            
		//AVLNode<Integer> r = tree.getRoot();
		if (r != null && r.getData() == 8
				&& r.getLeftChild() != null
				&& r.getLeftChild().getData() == 3
				&& r.getRightChild() != null
				&& r.getRightChild().getData() == 20)
			System.out.println("testRemove 2 OK");
		else
			System.out.println("testRemove 2 ERROR");
	}

	
	public static void testHeight() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		for (int i = 0; i < 1000; i++)
			tree.insert(i);
		if (tree.height()==9)
			System.out.println("testHeight OK");
		else
			System.out.println("testHeight ERROR: " + tree.height());
	}

	
	public static void testSize() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		for (int i = 0; i < 1000; i++)
			tree.insert(i);
		for (int i = 20; i < 520; i++) {
			tree.remove(i);
		}
		if (tree.size()==500)
			System.out.println("testSize OK");
		else
			System.out.println("testSize ERROR");
	}
	

	public static void testMin() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		tree.insert(3);
		tree.insert(2);
		tree.insert(8);
		tree.insert(7);
		tree.insert(1);
		tree.insert(10);
		tree.insert(20);
		int min1 = tree.getMin();
		tree.remove(1);
		int min2 = tree.getMin();
		tree.insert(-10);
		int min3 = tree.getMin();
			
		if (min1==1 && min2==2 && min3==-10)
			System.out.println("testMin OK");
		else
			System.out.println("testSize ERROR");
	}

	
	public static void testLessThanK() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		for (int i = 0; i < 1000; i++)
			tree.insert(i);
		for (int i = 20; i < 520; i++) {
			tree.remove(i);
		}

		Collection<Integer> list1 = tree.lessThanK(30);
		if (list1.size()==20)
			System.out.println("testLessThanK 1 OK"+ tree.lessThanK(30));
		else
			System.out.println("testLessThanK 1 ERROR" + tree.lessThanK(30));
		
		Collection<Integer> list2 = tree.lessThanK(525);
		if (list2.size()==25)
			System.out.println("testLessThanK 2 OK"+ tree.lessThanK(525));
		else
			System.out.println("testLessThanK 2 ERROR"+ tree.lessThanK(525));

		Collection<Integer> list3 = tree.lessThanK(950);
		if (list3.size()==450)
			System.out.println("testLessThanK 3 OK");
		else
			System.out.println("testLessThanK 3 ERROR");

		if (tree.find(842).getData().compareTo(842) == 0) {
			System.out.println("test find in K ok");
		} else {
			System.out.println("test find in K error");
		}

		try {
			if (tree.find(449).getData().compareTo(449) != 0) {
				System.out.println("test find in K2 error");
			} else {
				System.out.println("test find in K2 error");
			}
		} catch (NoSuchElementException e) {
			System.out.println("test find in K2 ok");
		}
	}

	public static void testDoubleRemove() {
		AVLTree<Integer> tree = new AVLTree<Integer>();

		tree.insert(10);
		tree.insert(15);
		tree.insert(3);
		tree.insert(20);
		tree.insert(20);
		tree.insert(3);
		tree.insert(1);
		tree.getRoot().printPreOrder();
		System.out.println("---------------");
		tree.remove(20);
		tree.remove(10);
		tree.remove(3);
		tree.insert(15);
		tree.remove(15);
		tree.remove(20);
		tree.insert(10);
		tree.insert(15);
		tree.remove(15);

		tree.getRoot().printPreOrder();
		try {
			if (tree.find(20).getData().compareTo(20) != 0) {
				System.out.println("test find 1 error");
			} else {
				System.out.println("test find 1 error");
			}
		} catch (NoSuchElementException e) {
			System.out.println("test find 1 ok");
		}
		if (tree.find(3).getData().compareTo(3) == 0) {
			System.out.println("test find 2 ok");
		} else {
			System.out.println("test find 2 error");
		}
	}

	public static void testRemoveMore() {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		tree.insert(35);
		tree.insert(25);
		tree.insert(40);
		tree.insert(39);
		tree.insert(41);
		tree.insert(23);
		tree.insert(37);
		tree.insert(26);
		tree.insert(27);
		tree.remove(25);
	}

	public static void main(String[] args) {
		testInsert();
		testRemove();
		testHeight();
		testSize();
		testMin();
		testLessThanK();
		testDoubleRemove();
	}
}
