package fifteenpuzzle;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class Solver {

	private List<String> solvePuzzle(String filename) throws IOException {
		Queue<Puzzle> open = new PriorityQueue<Puzzle>(Comparator.comparing(Puzzle::getHeuristic));
		Map<Puzzle, Integer> closed = new HashMap<Puzzle, Integer>();
		Puzzle startNode = new Puzzle(filename);

		int[][] goal = Puzzle.getSolvedPuzzle(startNode.getHeight(), startNode.getWidth());
		startNode.setHeuristic(goal);
		open.add(new Puzzle(filename));

		while (!open.isEmpty()) {
			Puzzle next = open.poll();

			if (Puzzle.areBoardsEqual(next.getBoard(), goal)) {
			}
		}
		return new ArrayList<>();
	}
	public static void main(String[] args) {
//		System.out.println("number of arguments: " + args.length);
//		for (int i = 0; i < args.length; i++) {
//			System.out.println(args[i]);
//		}

		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}
		
		
		// TODO
		//File input = new File(args[0]);
		// solve...
		//File output = new File(args[1]);

	}
}
