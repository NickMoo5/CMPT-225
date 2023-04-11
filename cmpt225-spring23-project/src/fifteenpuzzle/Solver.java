package fifteenpuzzle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class Solver {

	public static Puzzle solvePuzzle(Puzzle startNode, Puzzle goal) throws IOException {
		PriorityQueue<Puzzle> open = new PriorityQueue<Puzzle>(Comparator.comparing(Puzzle::getHeuristic));
		HashSet<Integer> closed = new HashSet<Integer>();
		startNode.combineHeuristic(goal);
		open.add(startNode);

		while (!open.isEmpty()) {
			Puzzle currPuzzle = open.poll();
			//System.out.println(currPuzzle);

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

	public static List<String> getSolution(String filename) throws IOException {
		Puzzle puz = null;
		List<String> soln = new ArrayList<String>();
		Puzzle startNode = new Puzzle(filename);
		Puzzle goal = new Puzzle(startNode.getHeight(), startNode.getWidth());

		puz = solvePuzzle(startNode, goal);
		soln.addAll(puz.getMoves());
		//System.out.println(puz);
		return soln;
	}

	public static void outputSolution(String filename, List<String> soln) throws IOException {
		File f = new File(filename);
		FileWriter fw = new FileWriter(f);
		for (String moveStep: soln) {
			fw.write(moveStep + "\n");
		}
		fw.close();
	}

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
		System.out.println("number of arguments: " + args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}


		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}

		String inputBoard = args[0];
		String outputBoard = args[1];
		List<String> soln = getSolution(inputBoard);
		outputSolution(outputBoard, soln);

		/*
		String outputBoard = "TESTING.txt";

		long startTime = System.currentTimeMillis();
		List<String> soln = getSolution("testcases/board33.txt");

		outputSolution(outputBoard, soln);
		long finishTime = System.currentTimeMillis();
		System.out.println("RunTime: " + (finishTime - startTime) + "ms");

		 */


		//testSolns("testcases");



		// TODO
		//File input = new File(args[0]);
		// solve...
		//File output = new File(args[1]);

	}
}
