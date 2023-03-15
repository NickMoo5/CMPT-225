package graph;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Graph {
	private Map<Integer, List<Integer>> graph = new HashMap<>();
	private int edges = 0;
	/**
	 * creates an empty graph on n nodes
	 * the "names" of the vertices are 0,1,..,n-1 
	 * @param n - number of vertices in the graph
	 */
	public Graph(int n) {
		if (n < 0) throw new IllegalArgumentException();

		for (int i = 0; i < n; i++) {
			addVertex(i);
		}
	}

	private void addVertex(int n) {
		graph.put(n, new ArrayList<Integer>());
	}

	/**
	 * adds the edge (i,j) to the graph  
	 * no effect if i and j were already adjacent
	 * @param i, j - vertices in the graph
	 */
	public void addEdge(int i, int j) throws NoSuchElementException, IllegalArgumentException {
		if (!graph.containsKey(i) || !graph.containsKey(j)) {
			throw new NoSuchElementException();
		}
		if (i == j) throw new IllegalArgumentException();

		if (!graph.get(i).contains(j) && !graph.get(j).contains(i)) {
			graph.get(i).add(j);
			graph.get(j).add(i);
			edges++;
		}
	}

	/**
	 * removes the edge (i,j) from the graph
	 * no effect if i and j were not adjacent
	 * @param i, j - vertices in the graph
	 */
	public void removeEdge(int i, int j) throws NoSuchElementException {
		if (!graph.containsKey(i) || !graph.containsKey(j)) {
			throw new NoSuchElementException();
		}
		if (graph.get(i).contains(j) && graph.get(j).contains(i)) {
			graph.get(i).remove(Integer.valueOf(j));
			graph.get(j).remove(Integer.valueOf(i));
			edges--;
		}
	}

	/**
	 * @param i, j - vertices in the graph
	 * @return true if (i,j) is an edge in the graph, and false otherwise
	 */
	public boolean areAdjacent(int i, int j) {
		if (graph.get(i).contains(j) && graph.get(j).contains(i)) {
			return true;
		}
		return false;
	}


	/**
	 * @param i, j - vertices in the graph
	 * @return true if distance(i,j)<=2, and false otherwise
	 */
	public boolean distanceAtMost2(int i, int j) {
		if (areAdjacent(i, j)) {
			return true;
		}
		for (int vertex: graph.get(i)) {
			if (graph.get(j).contains(Integer.valueOf(vertex))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param i - a vertex in the graph
	 * @return the degree of i
	 */
	public int degree(int i) throws NoSuchElementException {
		if (!graph.containsKey(i)) throw new NoSuchElementException();
		return graph.get(i).size();
	}
	
	/**
	 * The iterator must output the neighbors of i in the increasing order
	 * Assumption: the graph is not modified during the use of the iterator 
	 * @param i - a vertex in the graph
	 * @return an iterator that returns the neighbors of i
	 */
	public Iterator<Integer> neighboursIterator(int i) throws NoSuchElementException {
		if (!graph.containsKey(i)) throw new NoSuchElementException();
		return new graphIterator<Integer>(graph.get(i));
	}

	/**
	 * @return number of vertices in the graph
	 */
	public int numberOfVertices() {
		return graph.size();
	}

	/**
	 * @return number of edges in the graph
	 */
	public int numberOfEdges() {
		return edges;
	}

	/**
	 * @param n - number of vertices
	 * @param p - number between 0 and 1
	 * @return a random graph on n vertices, where each edge is added to the graph with probability p
	 */
	public static Graph generateRandomGraph(int n, double p) {
		Graph randGraph = new Graph(n);

		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				if (Math.random() < p && j != i) {
					randGraph.addEdge(i, j);
				}
			}
		}
		return randGraph;
	}

}
