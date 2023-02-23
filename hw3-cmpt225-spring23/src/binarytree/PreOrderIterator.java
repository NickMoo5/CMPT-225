package binarytree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PreOrderIterator<T> implements Iterator<T> {

    private BTNode<T> root;

    private ArrayList<BTNode<T>> nodes;
    private int currIdx;

    public PreOrderIterator(BTNode<T> rootOfTree) {
        root = rootOfTree;
        nodes = new ArrayList<BTNode<T>>();
        currIdx = 0;
    }

    private void genPreOrder(BTNode<T> node) {
        if (node == null) {return;}

        nodes.add(node);
        genPreOrder(node.getLeftChild());
        genPreOrder(node.getRightChild());
    }

    @Override
    public boolean hasNext() {
        nodes.clear();
        genPreOrder(root);
        return nodes.size() > currIdx;
    }

    @Override
    public T next() throws NoSuchElementException{
        if (!hasNext()) {throw new NoSuchElementException();}
        T retVal = nodes.get(currIdx).getData();
        currIdx++;
        return retVal;
    }

    public void reset() {currIdx = 0;}
}
