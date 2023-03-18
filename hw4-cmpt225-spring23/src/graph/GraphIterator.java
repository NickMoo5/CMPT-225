package graph;

import java.util.List;
import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class GraphIterator<Integer extends Comparable<Integer>> implements Iterator<Integer> {
    private List<Integer> graphNeighbours;
    private int curIndex;
    public GraphIterator(List<Integer> graphNeighbours) {
        this.graphNeighbours = graphNeighbours;
        this.graphNeighbours.sort(Comparator.naturalOrder());
        curIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return graphNeighbours.size() > curIndex;
    }

    @Override
    public Integer next() throws NoSuchElementException {
        if (!hasNext()) throw new NoSuchElementException();
        Integer retVal = graphNeighbours.get(curIndex);
        curIndex++;
        return retVal;
    }
}
