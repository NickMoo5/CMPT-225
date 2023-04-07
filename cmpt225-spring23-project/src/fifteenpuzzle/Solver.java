package fifteenpuzzle;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class Solver {

	private static Puzzle solvePuzzle(Puzzle startNode, int[][] goal) throws IOException {
		int g = 0;
		PriorityQueue<Puzzle> open = new PriorityQueue<Puzzle>(Comparator.comparing(Puzzle::getHeuristic));
		Map<Integer, Integer> closed = new HashMap<Integer, Integer>();	// (visited board, g)

		//Puzzle startNode = new Puzzle(filename, g);
		//int[][] goal = Puzzle.getSolvedPuzzle(startNode.getHeight(), startNode.getWidth());
		//goal = Puzzle.makeSubGoal(goal);
		startNode.setHeuristic(goal);
		open.add(startNode);

		while (!open.isEmpty()) {
			Puzzle currPuzzle = open.poll();

			if (Puzzle.areBoardsEqual(currPuzzle.getBoard(), goal)) {
				return currPuzzle;
			}

			g = currPuzzle.getG();

			if (closed.containsKey(currPuzzle.hashCode()))
				if (closed.get(currPuzzle.hashCode()) != g) continue;

			closed.put(currPuzzle.hashCode(), g);

			Map<Integer, Character> availableMoves = currPuzzle.getAvailableMoves();
			g++;

			for (int tile: availableMoves.keySet()) {
				String move = "" + tile + availableMoves.get(tile);
				Puzzle nextPuzzle = new Puzzle(Puzzle.getBoardDeepCopy(currPuzzle), g);
				nextPuzzle.makeMove(tile, availableMoves.get(tile));
				nextPuzzle.setParent(currPuzzle, move);
				nextPuzzle.setHeuristic(goal);


				if (closed.containsKey(nextPuzzle.hashCode())) {
					if (g < closed.get(nextPuzzle.hashCode())) {
						closed.put(nextPuzzle.hashCode(), g);
						open.add(nextPuzzle);
					}
				} else if (!closed.containsKey(nextPuzzle.hashCode())){
					open.add(nextPuzzle);
				}
			}
		}
		return null;
	}

	private static List<String> bigSolver(String filename) throws IOException {
		Puzzle puz = null;
		List<String> soln = new ArrayList<String>();
		Puzzle startNode = new Puzzle(filename, 0);
		int[][] goal = Puzzle.getSolvedPuzzle(startNode.getHeight(), startNode.getWidth());
		//goal = Puzzle.makeSubGoal(goal);
		int[][] subGoal = Puzzle.makeSubGoal(Puzzle.getArrDeepCopy(goal));
		;

		//startNode.setHeuristic(subGoal);

		while (true) {
			puz = solvePuzzle(startNode, subGoal);
			soln.addAll(puz.getMoves());
			System.out.println(puz);
			if (Puzzle.areBoardsEqual(puz.getBoard(), goal)) return soln;
			puz.setBoard(Puzzle.pruneBoard(puz.getBoard()));
			startNode = puz;
			goal = Puzzle.pruneBoard(goal);
			subGoal = Puzzle.makeSubGoal(Puzzle.getArrDeepCopy(goal));
		}

		//return soln;
	}
	public static void main(String[] args) throws IOException {
//		System.out.println("number of arguments: " + args.length);
//		for (int i = 0; i < args.length; i++) {
//			System.out.println(args[i]);
//		}
		int[][] arr = {{4, 1, 2}, {5, 3, 0}, {7, 8, 6}};

		/*
		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}

		 */
		long startTime = System.currentTimeMillis();
		List<String> soln = bigSolver("testcases/board20.txt");
		long finishTime = System.currentTimeMillis();
		System.out.println("RunTime: " + (finishTime - startTime) + "ms");
		for (String move: soln) {
			System.out.println(move);
		}

		// TODO
		//File input = new File(args[0]);
		// solve...
		//File output = new File(args[1]);

	}
}
