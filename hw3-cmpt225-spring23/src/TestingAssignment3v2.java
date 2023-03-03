import binarytree.*;
import java.util.*;
public class TestingAssignment3v2 {
    public static void main(String[] args) {
        BinaryTree<Integer> twoMillionTree = new BinaryTree<Integer>(new BTNode<Integer>(0));
        BTNode<Integer> thing = twoMillionTree.getRoot();
        for (int i = 0; i < 1048575; i++) {
            thing.setLeftChild(new BTNode<Integer>(i, null, null, thing));
            thing.setRightChild(new BTNode<Integer>(i, null, null, thing));
            thing = thing.getRightChild();
        }

        System.out.println("STARTING EASY TEST:");
        testNumLeaves(twoMillionTree);
        testPath(twoMillionTree, thing, false);
        //testDistance(twoMillionTree, thing);
        //testCountDepth(twoMillionTree);

        BinaryTree<Integer> twoMillionTreeHard = new BinaryTree<Integer>(new BTNode<Integer>(0));
        BTNode<Integer> thingHard = twoMillionTreeHard.getRoot();
        for (int i = 0; i < 1048575; i++) {
            thingHard.setLeftChild(new BTNode<Integer>(i, null, null, thingHard));
            thingHard.setRightChild(new BTNode<Integer>(i, null, null, thingHard));
            thingHard = thingHard.getLeftChild();
        }

        System.out.println("\nSTARTING HARD TEST:");
        testNumLeaves(twoMillionTreeHard);
        testPath(twoMillionTreeHard, thingHard, true);
        //testDistance(twoMillionTreeHard, thingHard);
        //testCountDepth(twoMillionTreeHard);

    }

    public static void testNumLeaves(BinaryTree<Integer> b) {
        if (b.numberOfLeaves() != 1048576) {
            System.out.println("Num Leaves WRONG ANS");
        } else {
            System.out.println("Num Leaves OK");
        }
    }

    public static void testPath(BinaryTree<Integer> b, BTNode<Integer> end, boolean left) {
        List<BTNode<Integer>> testPathList = b.pathFromRoot(end);
        BTNode<Integer> testPathNode = b.getRoot();
        // we're actually gonna check the path
        if (testPathList.size() != 1048576) {
            System.out.println("Path From Root WRONG ANS");
            return;
        }
        if (left) {
            for (int i = 0; i < testPathList.size(); i++) {
                if (testPathList.get(i) != testPathNode) {
                    System.out.println("Path From Root WRONG ANS");
                    return;
                }
                testPathNode = testPathNode.getLeftChild();
            }
        } else {
            for (int i = 0; i < testPathList.size(); i++) {
                if (testPathList.get(i) != testPathNode) {
                    System.out.println("Path From Root WRONG ANS");
                    return;
                }
                testPathNode = testPathNode.getRightChild();
            }
        }
        System.out.println("Path From Root OK");
    }

    public static void testDistance(BinaryTree<Integer> b, BTNode<Integer> end) {
        if (b.distance(b.getRoot(), b.getRoot().getLeftChild()) != 1
                || b.distance(b.getRoot(), b.getRoot().getRightChild()) != 1) {
            System.out.println("Distance WRONG ANS");
            return;
        }
        if (b.distance(b.getRoot(), end) != 1048575) {
            System.out.println("Distance WRONG ANS");
            return;
        }
        System.out.println("Distance OK");
    }

    public static void testCountDepth(BinaryTree<Integer> b) {
        if (b.countDepthK(1000000) != 2 || b.countDepthK(500000) != 2 || b.countDepthK(0) != 1
                || b.countDepthK(1048575) != 2) {
            System.out.println("CountDepthK WRONG ANS");
            return;
        }
        System.out.println("CountDepthK OK");
    }
}
