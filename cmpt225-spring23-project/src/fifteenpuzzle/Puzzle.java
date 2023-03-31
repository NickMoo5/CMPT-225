package fifteenpuzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Puzzle {
	public final static int UP = 0;
	public final static int DOWN = 1;
	public final static int LEFT = 2;
	public final static int RIGHT = 3;

	// size of board
	private int width;
	private int height;
	private int rowLength;						    // Row length in chars
	protected final static int SPACE_ASCII_CODE = 32;   // ASCII code for space

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
	}

	public Puzzle(int[][] board, int height, int width) {
		this.board = board;
		this.height = height;
		this.width = width;
		this.rowLength = (width * 2) + width - 1;
	}

	/**
	 * Get the number of the tile, and moves it to the specified direction
	 * 
	 */
	public void makeMove(int rowIdx, int columnIdx, int direction)   {
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
	 * getAvailableMoves
	 * @param row - row idx of current blank space
	 * @param column - column idx of current blank space
	 * @return a list of all available moves
	 */
	protected ArrayList<Integer> getAvailableMoves(int row, int column) {
		ArrayList<Integer> availableMoves = new ArrayList<Integer>();

		if (row > 0)								// check if UP is valid move
			if (this.board[row - 1][column] == 0)
				availableMoves.add(UP);

		if (row < height - 1)							// check if DOWN is valid move
			if (this.board[row + 1][column] == 0)
				availableMoves.add(DOWN);

		if (column < height - 1)						// check if RIGHT is valid move
			if (this.board[row][column + 1] == 0)
				availableMoves.add(RIGHT);

		if (column > 0)								// check if LEFT is valid move
			if (this.board[row][column - 1] == 0)
				availableMoves.add(LEFT);

		return availableMoves;
	}

	public int[][] getBoard() {return this.board;}

	public int getWidth() {return this.width;}

	public int getHeight() {return this.height;}

	public int[][] getBoardDeepCopy() {
		return Arrays.stream(this.board).map(int[]::clone).toArray(int[][]::new);
	}

	/**
	 * solvedPuzzle
	 * @param height - height of board
	 * @param width - width of board
	 * @return 2D array representing a solved puzzle board
	 */
	public static int[][] solvedPuzzle(int height, int width) {
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
