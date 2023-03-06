package binarytree;

import java.util.*;

public class PreOrderIterator<T> implements Iterator<T> {

    private BTNode<T> root;
    private HashSet<BTNode<T>> processedNodes;
    private boolean rightTree;

    public PreOrderIterator(BTNode<T> rootOfTree) {
        root = rootOfTree;
        processedNodes = new HashSet<BTNode<T>>();
        rightTree = false;
    }

    private BTNode<T> genPreOrder(BTNode<T> node) throws NoSuchElementException{
        if (node == null) return null;

        if (node.isLeaf() && node.isRoot() && processedNodes.contains(node)) {
            throw new NoSuchElementException();
        } else if (node.isLeaf() && node.isRoot()) {
            processedNodes.add(node);
            return node;
        }

        if (node.isRoot() && !processedNodes.contains(node)) {
            processedNodes.add(node);
            return node;
        }

        if (node.getLeftChild() != null) {
            processedNodes.add(node.getLeftChild());
            return node.getLeftChild();
        }


        if (node.isLeaf()) {
            node = node.getParent();
            if (rightTree && node.isRoot()) {
                throw new NoSuchElementException();
            }
            while (processedNodes.contains(node.getRightChild()) || node.getRightChild() == null) {
                node = node.getParent();
                if (rightTree && node.isRoot()) throw new NoSuchElementException();

                if (node.isRoot() && node.getRightChild() == null) throw new NoSuchElementException();

                if (node.isRoot() && processedNodes.contains(node)) {
                    rightTree = true;
                }
            }
            processedNodes.add(node.getRightChild());
            return node.getRightChild();
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        boolean rightTreeTemp = rightTree;

        try {
            processedNodes.remove(genPreOrder(root));
            rightTree = rightTreeTemp;
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    @Override
    public T next() throws NoSuchElementException{
        if (!hasNext()) {throw new NoSuchElementException();}

        root = genPreOrder(root);
        return root.getData();
    }

}
