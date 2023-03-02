package binarytree;

import java.util.*;

public class PreOrderIterator<T> implements Iterator<T> {

    private BTNode<T> root;

    private ArrayList<BTNode<T>> nodes;
    private int currIdx;

    private HashSet<BTNode<T>> processedNodes;
    private boolean rightTree;

    public PreOrderIterator(BTNode<T> rootOfTree) {
        root = rootOfTree;
        nodes = new ArrayList<BTNode<T>>();
        currIdx = 0;
        processedNodes = new HashSet<BTNode<T>>();
        rightTree = false;
    }

    private void genPreOrder(BTNode<T> node) {
        if (node == null) {return;}

        nodes.add(node);
        genPreOrder(node.getLeftChild());
        genPreOrder(node.getRightChild());
    }

    private BTNode<T> gensPreOrder(BTNode<T> node) throws NoSuchElementException{
        BTNode<T> temp = null;
        if (node == null) return null;

        if (node.getParent() == null && node.getLeftChild() == null && node.getRightChild() == null &&
                processedNodes.contains(node)) {
            throw new NoSuchElementException();

        } else if (node.getParent() == null && node.getLeftChild() == null && node.getRightChild() == null) {
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
        //nodes.clear();
        //genPreOrder(root);
        //return nodes.size() > currIdx;

        try {
            //node = gensPreOrder(root);
            processedNodes.remove(gensPreOrder(root));
            rightTree = rightTreeTemp;
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    @Override
    public T next() throws NoSuchElementException{
        if (!hasNext()) {throw new NoSuchElementException();}
        //T retVal = nodes.get(currIdx).getData();
        //currIdx++;
        //return retVal;

        root = gensPreOrder(root);
        return root.getData();
    }

    public void reset() {currIdx = 0;}
}
