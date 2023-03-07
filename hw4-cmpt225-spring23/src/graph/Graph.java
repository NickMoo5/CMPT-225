package graph;

import java.util.Iterator;

public class Graph {

	/**
	 * creates an empty graph on n nodes
	 * the "names" of the vertices are 0,1,..,n-1 
	 * @param n - number of vertices in the graph
	 */
	public Graph(int n) {
		// TODO implement me
	}


	/**
	 * adds the edge (i,j) to the graph  
	 * no effect if i and j were already adjacent
	 * @param i, j - vertices in the graph
	 */
	public void addEdge(int i, int j) {
		// TODO implement me
	}

	/**
	 * removes the edge (i,j) from the graph
	 * no effect if i and j were not adjacent
	 * @param i, j - vertices in the graph
	 */
	public void removeEdge(int i, int j) {
		// TODO implement me
	}

	/**
	 * @param i, j - vertices in the graph
	 * @return true if (i,j) is an edge in the graph, and false otherwise
	 */
	public boolean areAdjacent(int i, int j) {
		// TODO implement me
		return false;
	}


	/**
	 * @param i, j - vertices in the graph
	 * @return true if distance(i,j)<=2, and false otherwise
	 */
	public boolean distanceAtMost2(int i, int j) {
		// TODO implement me
		return false;
	}

	/**
	 * @param i - a vertex in the graph
	 * @return the degree of i
	 */
	public int degree(int i) {
		// TODO implement me
		return 0;
	}
	
	/**
	 * The iterator must output the neighbors of i in the increasing order
	 * Assumption: the graph is not modified during the use of the iterator 
	 * @param i - a vertex in the graph
	 * @return an iterator that returns the neighbors of i
	 */
	public Iterator<Integer> neighboursIterator(int i) {
		// TODO implement me
		return null;
	}

	/**
	 * @return number of vertices in the graph
	 */
	public int numberOfVertices() {
		// TODO implement me
		return 0;
	}

	/**
	 * @return number of edges in the graph
	 */
	public int numberOfEdges() {
		// TODO implement me
		return 0;
	}

	/**
	 * @param n - number of vertices
	 * @param p - number between 0 and 1
	 * @return a random graph on n vertices, where each edge is added to the graph with probability p
	 */
	public static Graph generateRandomGraph(int n, double p) {
		// TODO Auto-generated method stub
		return null;
	}

}
