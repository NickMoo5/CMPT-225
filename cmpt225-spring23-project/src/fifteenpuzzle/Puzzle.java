package fifteenpuzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.lang.Math;

public class Puzzle {
	public final static char UP = 'U';				// Up direction to move tile
	public final static char DOWN = 'D';			// Down direction to move tile
	public final static char LEFT = 'L';			// Left direction to move tile
	public final static char RIGHT = 'R';			// Right direction to move tile
	private final static int SPACE_ASCII_CODE = 32;   // ASCII code for space
	public final static int EMPTY_TILE = 0;			// Empty tile value
	private int width;								// Width of board
	private int height;								// Height of board
	private int heuristic;							// Heuristic value for board
	private int hashCode;							// Hashcode
	private int[][] board;							// Puzzle board
	private Puzzle parent;							// Parent Puzzle
	private String moveStrPair;						// Move used to go from Parent to this Puzzle

	
	/**
	 * Constuctor - Build Puzzle board from txt file
	 * @param fileName 
	 * @throws FileNotFoundException if file not found
	 */
	public Puzzle(String fileName) throws IOException, InputMismatchException{
		int rowLength = 0;						// row length in chars
		String line;
		File file = new File(fileName);
		Scanner reader = new Scanner(file);
		if (reader.hasNext()) {					// Read size of board and calculate dimensions
			line = reader.nextLine();
			height = width = Integer.parseInt(line);
			rowLength = (width * 2) + width - 1;
		} else {
			reader.close();
			throw new FileNotFoundException("File is Empty");
		}

		int rowOfFile = 0;
		char character;
		int ascii;
		int[] asciiLine  = new int[rowLength];
		this.board = new int[height][width];

		while (reader.hasNext()) {					// Go row by row
			int column = 0;

			line = reader.nextLine();

			for (int i=0; i < rowLength; i++) {			// Convert row to array of ascii values
				character = line.charAt(i);
				ascii = character;
				asciiLine[i] = ascii;
			}

			for (int i=1; i < rowLength; i+=3) {		// Process 2 digits and skip third which is a space
				String strNum;
				int num;
				int firstDigit = asciiLine[i-1];
				int secondDigit = asciiLine[i];
				if (firstDigit == SPACE_ASCII_CODE && secondDigit == SPACE_ASCII_CODE) {		// Check if blank tile
					strNum = Character.toString('0');
				} else if (firstDigit == SPACE_ASCII_CODE) {									// Single digit num tile
					strNum = Character.toString(secondDigit);
				} else {																		// Double digit num tile
					strNum = Character.toString(firstDigit) + Character.toString(secondDigit);
				}
				num = Integer.parseInt(strNum);

				this.board[rowOfFile][column] = num;
				column += 1;						// Increase column

			}
			rowOfFile += 1;						// Count rows
		}
		reader.close();

		this.heuristic = 0;
		this.parent = null;
		this.moveStrPair = null;
		genHashCode();
	}

	/**
	 * Constructor - goal puzzle with solution
	 * @param height
	 * @param width
	 */
	public Puzzle(int height, int width) {
		this.height = height;
		this.width = width;
		board = getSolvedPuzzle();
		genHashCode();
	}

	/**
	 * Constructor - create new object with move applied to an existing Puzzle
	 * @param parent
	 * @param goal
	 * @param tile
	 * @param direction
	 */
	public Puzzle(Puzzle parent, Puzzle goal, int tile, char direction) {
		this.board = parent.getBoardDeepCopy();
		this.height = board[0].length;
		this.width = board.length;
		this.parent = parent;
		this.moveStrPair = tile + " " + direction;
		makeMove(tile, direction);
		combineHeuristic(goal);
		genHashCode();
	}

	/**
	 * getSolvedPuzzle
	 * @return 2D array representing a solved puzzle board
	 */
	private int[][] getSolvedPuzzle() {
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
	 * Get the number of the tile, and moves it in the specified direction
	 * @param tile - tile number
	 * @param direction direction to move tile
	 */
	public void makeMove(int tile, char direction)   {
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
	}

	/**
	 * Gets position of specified tile in board
	 * @param tile
	 * @return array of the row and column values
	 * @throws NoSuchElementException
	 */
	private int[] getTilePos(int tile) throws NoSuchElementException {
		int row = -1;
		int column = -1;

		for (int i=0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (tile == board[i][j]) {
					row = i;
					column = j;
					break;
				}
			}
		}

		if (row < 0 || column < 0) throw new NoSuchElementException("Empty Tile not found");

		return new int[] {row, column};
	}

	/**
	 * getAvailableMoves
	 * @return a HashMap of all available moves
	 */
	public Map<Integer, Character> getAvailableMoves() {
		Map<Integer, Character> availableMoves = new HashMap<Integer, Character>();		// (Tile, Direction)
		int[] emptyTilePos = getTilePos(EMPTY_TILE);
		int row = emptyTilePos[0];
		int column = emptyTilePos[1];

		if (row < height && row > 0)									// check if DOWN is valid move
			availableMoves.put(this.board[row - 1][column], DOWN);

		if (row >= 0 && row < height - 1)								// check if UP is valid move
			availableMoves.put(this.board[row + 1][column] , UP);

		if (column < width - 1 && column >= 0)							// check if LEFT is valid move
			availableMoves.put(this.board[row][column + 1], LEFT);

		if (column > 0 && column < width)								// check if RIGHT is valid move
			availableMoves.put(this.board[row][column - 1], RIGHT);

		return availableMoves;
	}

	public int[][] getBoard() {return this.board;}
	public int getWidth() {return this.width;}
	public int getHeight() {return this.height;}
	public int getHeuristic() {return heuristic;}

	private int[][] getBoardDeepCopy() {
		return Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
	}

	/**
	 * Helper for getLinearRowConflict - gets position of tile in a row
	 * @param row
	 * @param tile
	 * @return Position of tile
	 */
	private int getPosInRow(int[] row, int tile) {
		int retVal = -1;						// return -1 if tile not found
		for (int i = 0; i < row.length; i++)
			if (row[i] == tile)
				retVal = i;
		return retVal;
	}

	/**
	 * Calculates Linear Conflict for a row
	 * @param boardRow
	 * @param goal
	 * @return linear conflict heuristic value for row
	 */
	private int getLinearRowConflict(int[] boardRow, int[] goal) {
		int confictCounter = 0;

		for (int i = 0; i < boardRow.length; i++) {
			if (boardRow[i] == EMPTY_TILE) continue;
			int tilePos = getPosInRow(goal, boardRow[i]);
			if (tilePos == -1) continue;

			for (int k = i + 1; k < width; k++) {
				int otherTile = boardRow[k];
				if (otherTile == EMPTY_TILE) continue;
				int otherTilePos = getPosInRow(goal, otherTile);
				if (otherTilePos == -1) continue;
				if (boardRow[i] > boardRow[k]) confictCounter++;		// compare if tiles are in correct order
			}
		}
		return confictCounter*2;
	}

	/**
	 * Creates a transposed matrix
	 * @param matrix
	 * @return new transposed matrix
	 */
	public static int[][] transposeMatrix(int[][] matrix) {
		int[][] transpose = new int[matrix.length][matrix.length];
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix.length; j++)
				transpose[i][j] = matrix[j][i];
		return transpose;
	}

	public void combineHeuristic(Puzzle goalBoard) {
		int[][] goal = goalBoard.getBoard();
		int manhattanDistance = 0;
		int euclidDist = 0;
		int misplacedTiles = 0;
		int linearConflict = 0;
		int boardWidth = board.length;
		int boardHeight = board[0].length;
		int goalWidth = goal.length;
		int goalHeight = goal[0].length;

		if (boardWidth != goalWidth || boardHeight != goalHeight) throw new IllegalArgumentException("board sizes are not equal");

		for (int i=0; i < goalHeight; i++) {
			linearConflict += getLinearRowConflict(board[i], goal[i]);

			for (int j = 0; j < goalWidth; j++)
				if (goal[i][j] != EMPTY_TILE) {
					int[] boardPosOfTile = getTilePos(goal[i][j]);
					int yPosofTile = boardPosOfTile[0];
					int xPosofTile = boardPosOfTile[1];
					manhattanDistance += Math.abs(i - yPosofTile) + Math.abs(j - xPosofTile);
					if (height >= 8)
						euclidDist += (int) Math.sqrt(Math.pow((Math.abs(i - yPosofTile)), 2) + Math.pow(Math.abs(j - xPosofTile), 2));
					if (board[i][j] != goal[i][j])
						misplacedTiles++;
				}
		}

		int[][] transposedBoard = transposeMatrix(board);
		int[][] transposedGoal = transposeMatrix(goal);
		for (int i = 0; i < transposedBoard.length; i++) {
			linearConflict += getLinearRowConflict(transposedBoard[i], transposedGoal[i]);
		}

		int heuristic = 0;
		double hammingFactor = 1.0;
		heuristic += 0.6 * manhattanDistance;
		heuristic += 0.3 * linearConflict;
		if (heuristic >= 8)
			hammingFactor = 0.8;
		heuristic += hammingFactor * misplacedTiles; // 0.8 for 8x8

		if (height == 8)
			heuristic += 0.5*euclidDist;
		else if (height == 9)
			heuristic += 0.6*euclidDist;

		this.heuristic = heuristic;
	}

	/**
	 * Generates list of all moves used to get to current Puzzle state
	 * @return List of moves
	 */
	public List<String> getMoves() {
		List<String> moves = new ArrayList<String>();
		Stack<Puzzle> s = new Stack<Puzzle>();
		s.push(this);
		while (!s.isEmpty()) {
			Puzzle currPuzzle = s.pop();
			if (currPuzzle.parent != null) {
				s.add(currPuzzle.parent);
				moves.add(currPuzzle.moveStrPair);
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

	/**
	 * Generate hashcode
	 */
	private void genHashCode() {	// 25 for 8x8
		int hash = 0;
		int scale = 13;				// Default scale factor
		for (int i=0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (height == 8) scale = 25;	// increase scale factor
				if (height == 9) scale = 27;	// increase scale factor
				hash = (hash ^ board[i][j]) * scale;
			}
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
