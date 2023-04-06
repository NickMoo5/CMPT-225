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
	protected final static int SPACE_ASCII_CODE = 32;   // ASCII code for space
	public final static int NULL_TILE = -1;
	public final static int EMPTY_TILE = 0;

	private int[][] board;

	
	/**
	 * @param fileName 
	 * @throws FileNotFoundException if file not found
	 * Reads a board from file and creates the board
	 */
	public Puzzle(String fileName) throws IOException, InputMismatchException{
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
		int[] asciiLine  = new int[11];
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
	}

	public Puzzle(int[][] board, int height, int width) {
		this.board = board;
		this.height = height;
		this.width = width;
		this.rowLength = (width * 2) + width - 1;
		this.heuristic = 0;
	}

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

	/**
	 * getAvailableMoves
	 * @return a list of all available moves
	 */
	protected Map<Integer, Character> getAvailableMoves() {
		Map<Integer, Character> availableMoves = new HashMap<Integer, Character>();		// (Tile, direction)
		int[] emptyTilePos = getTilePos(EMPTY_TILE);
		int row = emptyTilePos[0];
		int column = emptyTilePos[1];

		if (row < height - 1)									// check if DOWN is valid move
			availableMoves.put(this.board[row - 1][column], DOWN);

		if (row > 0)											// check if UP is valid move
			availableMoves.put(this.board[row + 1][column] , UP);

		if (column < height - 1)								// check if LEFT is valid move
			availableMoves.put(this.board[row][column + 1], LEFT);

		if (column > 0)											// check if RIGHT is valid move
			availableMoves.put(this.board[row][column - 1], RIGHT);

		return availableMoves;
	}

	public int[][] getBoard() {return this.board;}

	public int getWidth() {return this.width;}

	public int getHeight() {return this.height;}

	public int[][] getBoardDeepCopy() {
		return Arrays.stream(this.board).map(int[]::clone).toArray(int[][]::new);
	}

	private int getManhattanDistance(int[][] goal) {
		int manhattanDistance = 0;
		int boardWidth = board.length;
		int boardHeight = board[0].length;
		int goalWidth = goal.length;
		int goalHeight = goal[0].length;

		if (boardWidth != goalWidth || boardHeight != goalHeight) throw new IllegalArgumentException("board sizes are not equal");

		for (int i=0; i < goalHeight; i++)
			for (int j=0; j < goalWidth; j++)
				if (goal[i][j] != NULL_TILE) {
					int[] boardPosOfTile = getTilePos(goal[i][j]);
					int yPosofTile = boardPosOfTile[0];
					int xPosofTile = boardPosOfTile[1];
					manhattanDistance += Math.abs(i - yPosofTile) + Math.abs(j - xPosofTile);
				}

		return manhattanDistance;
	}

	private int getLinearConflict(int[][] goal) {
		int linearConflictsCounter = 0;
		int linearConflict = 0;
		int boardWidth = board.length;
		int boardHeight = board[0].length;
		int goalWidth = goal.length;
		int goalHeight = goal[0].length;

		if (boardWidth != goalWidth || boardHeight != goalHeight) throw new IllegalArgumentException("board sizes are not equal");

		int[] row = board[0];
		int[] column = new int[boardHeight];

		for (int i= 0; i < goalHeight; i++)		// parse single column of board
			column[i] = board[i][0];

		for (int i: board[0]) {						// Check if there are nums in correct row
			for (int j: goal[0])
				if (i == j) linearConflictsCounter++;
		}

		if (linearConflictsCounter % 2 == 0) {
			linearConflict += linearConflictsCounter * 2;
			linearConflictsCounter = 0;
		}

		for (int i = 0; i < boardHeight; i++) {
			for (int j = 0; j < goalHeight; j++) {
				if (board[i][0] == goal[j][0]) linearConflictsCounter++;
			}
		}

		if (linearConflictsCounter % 2 == 0) {
			linearConflict += linearConflictsCounter * 2;
		}

		return linearConflict;
	}
	public void setHeuristic(int[][] goal) {
		int heuristic = 0;
		heuristic += getManhattanDistance(goal);
		heuristic += getLinearConflict(goal);
		this.heuristic = heuristic;
	}

	public int getHeuristic() {return heuristic;}

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
		if (height < 3 && width < 3) return board;
		for (int i=1; i < height; i++)
			for (int j=1; j < width; j++) {
				board[i][j] = NULL_TILE;
			}
		return board;
	}

	/**
	 * Removes top row and leftmost column
	 * @param board - board to be pruned
	 * @return pruned board - note if board is already 2x2 then it will not be pruned further
	 */
	public static int[][] pruneBoard(int[][] board) {
		int width = board.length;
		int height = board[0].length;
		if (height < 3 && width < 3) return board;
		int [][] prunedBoard = new int[height - 1][width - 1];

		for (int i=0; i < height; i++)
			for (int j=0; j < width; j++) {
				prunedBoard[i][j] = board[i+ 1][j + 1];
			}
		board = null;
		return prunedBoard;
	}

	public static boolean areBoardsEqual(int[][] board, int[][] goal) {
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
}
