package fifteenpuzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.lang.Math;

public class Puzzle {
	public final static char UP = 'U';
	public final static char DOWN = 'D';
	public final static char LEFT = 'L';
	public final static char RIGHT = 'R';

	// size of board
	private int width;
	private int height;
	private int rowLength;						    // Row length in chars

	private int heuristic;
	private int g;

	private int hashCode;
	protected final static int SPACE_ASCII_CODE = 32;   // ASCII code for space
	public final static int NULL_TILE = -1;
	public final static int EMPTY_TILE = 0;

	private int[][] board;
	private Puzzle parent;
	private String edgeMove;

	
	/**
	 * @param fileName 
	 * @throws FileNotFoundException if file not found
	 * Reads a board from file and creates the board
	 */
	public Puzzle(String fileName, int g) throws IOException, InputMismatchException{
		String line;
		File file = new File(fileName);
		Scanner reader = new Scanner(file);
		if (reader.hasNext()) {
			line = reader.nextLine();
			height = width = Integer.parseInt(line);
			rowLength = (width * 2) + width - 1;
		} else {
			reader.close();
			throw new FileNotFoundException("File is Empty");
		}

		int rowIdx = 0;
		char character;
		int ascii;
		int[] asciiLine  = new int[rowLength];
		this.board = new int[height][width];

		while (reader.hasNext()) {					// Go row by row
			int index = 0;

			line = reader.nextLine();

			for (int i=0; i < rowLength; i++) {			// Convert row to array of ascii values
				character = line.charAt(i);
				ascii = character;
				asciiLine[i] = ascii;
			}

			for (int i=1; i < rowLength; i+=3) {
				String strNum;
				int num;
				int firstDigit = asciiLine[i-1];
				int secondDigit = asciiLine[i];
				if (firstDigit == SPACE_ASCII_CODE && secondDigit == SPACE_ASCII_CODE) {
					strNum = Character.toString('0');
				} else if (firstDigit == SPACE_ASCII_CODE) {
					strNum = Character.toString(secondDigit);
				} else {
					strNum = Character.toString(firstDigit) + Character.toString(secondDigit);
				}
				num = Integer.parseInt(strNum);

				this.board[rowIdx][index] = num;
				index += 1;

			}
			rowIdx += 1;						// Count rows
		}
		reader.close();

		this.heuristic = 0;
		this.parent = null;
		this.edgeMove = null;
		this.g = g;
		genHashCode();
	}

	public Puzzle(int[][] board, int g) {
		this.board = board;
		this.height = board[0].length;
		this.width = board.length;
		this.rowLength = (width * 2) + width - 1;
		this.heuristic = 0;
		this.parent = null;
		this.edgeMove = null;
		this.g = g;
		genHashCode();
	}

	public static Puzzle makeNextPuzzle(Puzzle currPuzzle, int tile, char direction, int[][] goal, int g) {
		String move = "" + tile + direction;
		Puzzle newPuzzle = new Puzzle(getBoardDeepCopy(currPuzzle), g);
		newPuzzle.makeMove(tile, direction);
		newPuzzle.setParent(currPuzzle, move);
		//newPuzzle.setHeuristic();
		return null;
	}

	public void setParent(Puzzle puzzle, String move) {
		parent = puzzle;
		edgeMove = move;
	}

	public Puzzle getParent() {return parent;}

	public String getEdgeMove() {return edgeMove;}

	/**
	 * Get the number of the tile, and moves it to the specified direction
	 * @param tile - tile number
	 * @param direction direction to move tile
	 */
	public void makeMove(int tile, int direction)   {
		int[] tilePos = getTilePos(tile);
		int rowIdx = tilePos[0];
		int columnIdx = tilePos[1];

		if (direction == UP) {
			this.board[rowIdx - 1][columnIdx] = this.board[rowIdx][columnIdx];
			this.board[rowIdx][columnIdx] = 0;
		} else if (direction == DOWN) {
			this.board[rowIdx + 1][columnIdx] = this.board[rowIdx][columnIdx];
			this.board[rowIdx][columnIdx] = 0;
		} else if (direction == RIGHT) {
			this.board[rowIdx][columnIdx + 1] = this.board[rowIdx][columnIdx];
			this.board[rowIdx][columnIdx] = 0;
		} else if (direction == LEFT) {
			this.board[rowIdx][columnIdx - 1] = this.board[rowIdx][columnIdx];
			this.board[rowIdx][columnIdx] = 0;
		}

		genHashCode();
	}

	private int[] getTilePos(int tile) throws NoSuchElementException {
		int row = -1;
		int column = -1;

		for (int i=0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (tile == this.board[i][j]) {
					row = i;
					column = j;
					break;
				}
			}
		}

		if (row < 0 || column < 0) throw new NoSuchElementException("Empty Tile not found");

		return new int[] {row, column};
	}

	private int[] getTileDest(int tile, int[][] goal) throws NoSuchElementException {
		int row = -1;
		int column = -1;

		for (int i=0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (tile == goal[i][j]) {
					row = i;
					column = j;
					break;
				}
			}
		}

		if (row < 0 || column < 0) throw new NoSuchElementException("Tile not found");

		return new int[] {row, column};
	}

	/**
	 * getAvailableMoves
	 * @return a list of all available moves
	 */
	protected Map<Integer, Character> getAvailableMoves() {
		Map<Integer, Character> availableMoves = new HashMap<Integer, Character>();		// (Tile, direction)
		int[] emptyTilePos = getTilePos(EMPTY_TILE);
		int row = emptyTilePos[0];
		int column = emptyTilePos[1];

		if (row < height && row > 0)									// check if DOWN is valid move
			availableMoves.put(this.board[row - 1][column], DOWN);

		if (row >= 0 && row < height - 1)											// check if UP is valid move
			availableMoves.put(this.board[row + 1][column] , UP);

		if (column < width - 1 && column >= 0)								// check if LEFT is valid move
			availableMoves.put(this.board[row][column + 1], LEFT);

		if (column > 0 && column < width)											// check if RIGHT is valid move
			availableMoves.put(this.board[row][column - 1], RIGHT);

		return availableMoves;
	}

	public int[][] getBoard() {return this.board;}

	public int getWidth() {return this.width;}

	public int getHeight() {return this.height;}

	public static int[][] getBoardDeepCopy(Puzzle puz) {
		return Arrays.stream(puz.getBoard()).map(int[]::clone).toArray(int[][]::new);
	}

	public static int[][] getArrDeepCopy(int[][] board) {
		return Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
	}

	private int getTileManhattan(int[][] goal, int tile) {
		int[] boardTilePos = getTilePos(tile);
		int boardRow = boardTilePos[0];
		int boardCol = boardTilePos[1];
		int[] goalTilePos = getTileDest(tile, goal);

		return 0;
	}

	private int getManhattanDistance(int[][] goal) {
		int manhattanDistance = 0;
		int boardWidth = board.length;
		int boardHeight = board[0].length;
		int goalWidth = goal.length;
		int goalHeight = goal[0].length;

		if (boardWidth != goalWidth || boardHeight != goalHeight) throw new IllegalArgumentException("board sizes are not equal");

		for (int i=0; i < goalHeight; i++) {
			if (i > 0 && boardWidth > 3) goalWidth = 1;
			for (int j = 0; j < goalWidth; j++)
				if (goal[i][j] != NULL_TILE) {
					int[] boardPosOfTile = getTilePos(goal[i][j]);
					int yPosofTile = boardPosOfTile[0];
					int xPosofTile = boardPosOfTile[1];
					manhattanDistance += Math.abs(i - yPosofTile) + Math.abs(j - xPosofTile);
				}
		}
		return manhattanDistance;
	}

	private int getPosInRow(int[] row, int tile) {
		int retVal = -1;
		for (int i = 0; i < row.length; i++)
			if (row[i] == tile)
				retVal = i;
		return retVal;
	}

	private int getLinearRowConflict(int[] boardRow, int[] goal) {
		int confictCounter = 0;

		for (int i = 0; i < boardRow.length; i++) {
			if (boardRow[i] == EMPTY_TILE || boardRow[i] == NULL_TILE) continue;
			int tilePos = getPosInRow(goal, boardRow[i]);
			if (tilePos == -1) continue;

			for (int k = i + 1; k < width; k++) {
				int otherTile = boardRow[k];
				if (otherTile == EMPTY_TILE || otherTile == NULL_TILE) continue;
				int otherTilePos = getPosInRow(goal, otherTile);
				if (otherTilePos == -1) continue;
				if (otherTilePos <= tilePos) confictCounter++;
			}
		}
		return confictCounter*2;
	}

	public static int[][] transposeMatrix(int[][] matrix) {
		int[][] transpose = new int[matrix.length][matrix.length];
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix.length; j++)
				transpose[i][j] = matrix[j][i];
		return transpose;
	}

	private int getLinearConflicts(int[][] goal) {
		int linearConflict = 0;
		int boardWidth = board.length;
		int boardHeight = board[0].length;
		int goalWidth = goal.length;
		int goalHeight = goal[0].length;

		if (boardWidth != goalWidth || boardHeight != goalHeight) throw new IllegalArgumentException("board sizes are not equal");

		for (int i = 0; i < board.length; i++) {
			linearConflict += getLinearRowConflict(board[i], goal[i]);
			if (boardWidth > 3) break;
		}

		int[][] transposedBoard = transposeMatrix(board);
		int[][] transposedGoal = transposeMatrix(goal);
		for (int i = 0; i < transposedBoard.length; i++) {
			linearConflict += getLinearRowConflict(transposedBoard[i], transposedGoal[i]);
			if (boardWidth > 3) break;
		}

		return linearConflict;
	}

	private int getHammingDistance(int[][] goal) {
		int misplacedTiles = 0;
		for(int i = 0; i < goal.length; i++)
			for(int j = 0; j < goal.length; j++)
				if (goal[i][j] != NULL_TILE || goal[i][j] != EMPTY_TILE)
					if (board[i][j] != goal[i][j]) misplacedTiles++;
		return misplacedTiles;
	}
	public void setHeuristic(int[][] goal) {
		int heuristic = 0;
		heuristic += getManhattanDistance(goal);
		heuristic += getLinearConflicts(goal);
		//heuristic += getHammingDistance(goal);
		this.heuristic = heuristic + g;
	}

	public int getHeuristic() {return heuristic;}

	public void setG(int g) {this.g = g;}

	public int getG() {return g;}

	public void setBoard(int[][] board) {
		this.board = board;
		width = board.length;
		height = board[0].length;
	}

	/**
	 * getSolvedPuzzle
	 * @param height - height of board
	 * @param width - width of board
	 * @return 2D array representing a solved puzzle board
	 */
	public static int[][] getSolvedPuzzle(int height, int width) {
		int[][] solvedBoard = new int[height][width];
		int startNum = 1;
		int lastXIdx = width - 1;
		int lastYIdx = height - 1;

		for (int i=0; i < height; i++)
			for (int j=0; j < width; j++) {
				if (i == lastYIdx && j == lastXIdx)
					startNum = 0;
				solvedBoard[i][j] = startNum;
				startNum++;
			}
		return solvedBoard;
	}

	/**
	 * Changes all tiles NOT in top row and leftmost column to a null tile
	 * @param board
	 * @return transformed board
	 */
	public static int[][] makeSubGoal(int[][] board) {
		int height = board.length;
		int width = board[0].length;
		if (height < 4 && width < 4) return board;
		for (int i=1; i < height; i++)
			for (int j=1; j < width; j++) {
				board[i][j] = NULL_TILE;
			}

		return board;
	}

	public static int[][] makeSubRow(int[][] board) {
		int height = board.length;
		int width = board[0].length;
		if (height < 4 && width < 4) return board;
		for (int i=1; i < height; i++)
			for (int j=0; j < width; j++) {
				board[i][j] = NULL_TILE;
			}

		return board;
	}

	/**
	 * Removes top row and leftmost column
	 * @param board - board to be pruned
	 * @return pruned board - note if board is already 3x3 then it will not be pruned further
	 */
	public static int[][] pruneBoard(int[][] board) {
		int width = board.length;
		int height = board[0].length;
		if (height < 4 && width < 4) return board;
		int [][] prunedBoard = new int[height - 1][width - 1];

		for (int i=0; i < height - 1; i++)
			for (int j=0; j < width - 1; j++) {
				prunedBoard[i][j] = board[i+ 1][j + 1];
			}
		return prunedBoard;
	}

	public static boolean areBoardsEqual(int[][] board, int[][] goal) {
		if (board == null) return false;
		int boardWidth = board.length;
		int boardHeight = board[0].length;
		int goalWidth = goal.length;
		int goalHeight = goal[0].length;

		if (boardWidth != goalWidth || boardHeight != goalHeight) throw new IllegalArgumentException("board sizes are not equal");

		for (int i=0; i < boardHeight; i++)
			for (int j=0; j < boardWidth; j++)
				if (goal[i][j] != NULL_TILE)
					if (goal[i][j] != board[i][j]) return false;

		return true;
	}

	public List<String> getMoves() {
		List<String> moves = new ArrayList<String>();
		Stack<Puzzle> s = new Stack<Puzzle>();
		s.push(this);
		while (!s.isEmpty()) {
			Puzzle currPuzzle = s.pop();
			if (currPuzzle.getParent() != null) {
				s.add(currPuzzle.getParent());
				moves.add(currPuzzle.getEdgeMove());
			}
		}
		Collections.reverse(moves);
		return moves;
	}
	
	@Override
	public String toString() {
		String formattedBoard = "";
		String strToAppend = null;
		int val = 0;
		for (int i=0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				val = this.board[i][j];
				if (val == 0) {
					strToAppend = " " + " " + " ";
				} else if (val < 10) {                // Check if 1 digit
					strToAppend = " " + val + " ";
				} else {                    		  // is 2-digit number
					strToAppend = val + " ";
				}
				formattedBoard += strToAppend;
			}
			formattedBoard = formattedBoard.substring(0, formattedBoard.length()-1);
			formattedBoard += "\n";
		}
		return formattedBoard;
	}

	private void genHashCode() {
		int hash = 0;
		for (int i=0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				//hash = (hash * height * width) + board[i][j];
				hash = ((hash ^ board[i][j]) * height);
				//hash = ((hash + board[j][i] ^ board[i][j]) * height);
				//hash = (hash ^ (board[i][j] * height)) + 1;
			}
			//hash *= i*height;
		}

		//hashCode = (int) Zobrist.computeHash(board);
		hashCode = hash + g;
	}

	private void genHashCodes() {
		int hash = 0;
		int sum = 0;
		for (int i=0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				hash = ((hash ^ board[i][j]) * height);
				sum += board[i][j];
			}
			hash = hash ^ sum;
		}

		hashCode = hash;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Puzzle)) return false;
		return this.hashCode == obj.hashCode();
	}
}
