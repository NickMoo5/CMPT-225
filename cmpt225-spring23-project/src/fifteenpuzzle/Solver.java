package fifteenpuzzle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class Solver {

	/**
	 * PHS algorithm to solve a given puzzle from size 3x3 up to 9x9
	 * @param startNode
	 * @param goal
	 * @return
	 */
	public static Puzzle solvePuzzle(Puzzle startNode, Puzzle goal) {
		PriorityQueue<Puzzle> open = new PriorityQueue<Puzzle>(Comparator.comparing(Puzzle::getHeuristic));
		HashSet<Integer> closed = new HashSet<Integer>();
		startNode.calcHeuristic(goal);
		open.add(startNode);

		while (!open.isEmpty()) {
			Puzzle currPuzzle = open.poll();

			closed.add(currPuzzle.hashCode());

			Map<Integer, Character> availableMoves = currPuzzle.getAvailableMoves();

			for (int tile: availableMoves.keySet()) {
				Puzzle nextPuzzle = new Puzzle(currPuzzle, goal, tile, availableMoves.get(tile));

				if (nextPuzzle.equals(goal)) return nextPuzzle;;

				if (!closed.contains(nextPuzzle.hashCode())) {
					open.add(nextPuzzle);
				}
			}

		}
		return null;
	}

	/**
	 * Obtains solution in a list
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static List<String> getSolution(String filename) throws IOException {
		Puzzle puz = null;
		List<String> soln = new ArrayList<String>();
		Puzzle startNode = new Puzzle(filename);
		Puzzle goal = new Puzzle(startNode.getHeight(), startNode.getWidth());

		puz = solvePuzzle(startNode, goal);
		soln.addAll(puz.getMoves());
		return soln;
	}

	/**
	 * Outputs solution to file
	 * @param filename
	 * @param soln
	 * @throws IOException
	 */
	public static void outputSolution(String filename, List<String> soln) throws IOException {
		File f = new File(filename);
		FileWriter fw = new FileWriter(f);
		for (String moveStep: soln) {
			fw.write(moveStep + "\n");
		}
		fw.close();
	}

	/**
	 * Test function that runs all boards and outputs time to console
	 * @param folderPath - path of output
	 * @throws IOException
	 */
	public static void testSolns(String folderPath) throws IOException {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		for (File file: listOfFiles) {
			String outputBoard = "TESTING";

			long startTime = System.currentTimeMillis();
			List<String> soln = getSolution("testcases/" + file.getName());
			outputSolution(outputBoard, soln);
			long finishTime = System.currentTimeMillis();
			System.out.println("-----------------------------------------------------------");
			System.out.println("Puzzle Board: " + file.getName() + "  |  RunTime: " + (finishTime - startTime) + "ms");
			System.out.println();
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}

		String inputBoard = args[0];
		String outputBoard = args[1];
		List<String> soln = getSolution(inputBoard);
		outputSolution(outputBoard, soln);
	}
}
