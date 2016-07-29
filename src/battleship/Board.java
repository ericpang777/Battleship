package battleship;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Board class stores the values of the boards used in the game.
 * @author Eric Pang
 * @version 1.0
 */
public class Board { 
	private String[][] board; 
	private int size;
	private String lastShipHit;
	
	/**
	 * The Board constructor stores the size of the board.
	 * @param size - size of board.
	 */
	public Board(int size) { 
		setSize(size); 
		this.size = size;
		setUp(); 
	}
	
	/**
	 * Sets the size of board and initializes the 2D array.
	 * @param size - size of board.
	 */
	private void setSize(int size) { 
		board = new String[size][size];
	}
	
	/**
	 * Gets 2D array used by the board.
	 * @return String[][] board.
	 */
	public String[][] getBoard() {
		return board;
	}
	
	/**
	 * Adds the starting values of each point on the board.
	 */
	private void setUp() {
		for(String[] row : board) { 
			//Fills board with "empty" slots
			Arrays.fill(row, "~"); 
		}
		board[0][0] = " ";
		
		//Clear rows between coordinate identifiers
		for(int i = 0; i < size; i++) { 
			board[i][1] = " ";
		}
		for(int i = 0; i < size; i++) { 
			board[1][i] = " ";
		}
		
		//To make an empty line and row between scale	
		for(int i = 1; i < size-1; i++) { 
			//Letters to assign to rows, 64 is 'A'-1
			board[i+1][0] = "" + (char)(64 + i); 
		}
		for(int i = 1; i < size-1; i++) {
			//Numbers to assign to columns
			board[0][i+1] = "" + i; 
		}
	}
	
	/**
	 * Changes values of spots that would be used up by ship to the ship's char identifier.
	 * @param ship - ship that will be added.
	 * @param input - points used up by the ship.
	 */
	public void addShip(Ship ship, ArrayList<Point> input) {
		for(Point point : input) { 
			//Convert capital letter to number used internally
			int letter = point.getIntLet() - 63; 
			int number = point.getIntNum() + 1;
			board[letter][number] = ship.getCharType();
		}
	}
	
	/**
	 * Used for attack boards.
	 * Changes value of a target to "x" or "o" depending on if the shot hit or not.
	 * @param input - point targetted.
	 * @param hitTarget - if the shot hit.
	 */
	public void setTarget(Point input, boolean hitTarget) { 
		//Convert capital letter to number used internally
		int letter = input.getIntLet() - 63; 
		int number = input.getIntNum() + 1;
		
		if(hitTarget) { 
			board[letter][number] = "x";
		}
		else {
			board[letter][number] = "o";
		}
	}
	
	/**
	 * Used for defense boards.
	 * Changes value of a target to "x" or "o" depending on if the shot hit or not.
	 * Prints the accompanying statement if the shot hit or missed, and if hit, if sunk.
	 * Returns boolean if the shot hit.
	 * @param input - point targetted.
	 * @param user - user or computer's shot.
	 * @return boolean if the shot hit.
	 */
	public boolean hitTarget(Point input, String user) {
		//Convert capital letter to number used internally
		int letter = input.getIntLet() - 63; 
		int number = input.getIntNum() + 1;
		String targetSpot = board[letter][number];
		lastShipHit = targetSpot;
		
		//Anything but "~" means it hit a ship
		if(!targetSpot.equals("~")) { 
			if(user.equals("user")) { 
				System.out.println("\nYou hit a ship!");
			}
			else {
				System.out.println("\nYour "+ identifyShip(targetSpot) +" was hit!");
			}
			//Changes to hit value
			board[letter][number] = "x"; 
			
			if(sunkShip()) { 
				if(user.equals("user")) {
					System.out.println("You sunk their " + identifyShip(targetSpot) +"!");
				}
				if(user.equals("comp")) { 
					System.out.println("Your "+ identifyShip(targetSpot) +" was sunk!");
				}
			}
			return true;
		}
		//"~" means it hit the "sea" and missed.
		else if(targetSpot.equals("~")) { 
			board[letter][number] = "o";
			if(user.equals("user")) {
				System.out.println("\nYou did not hit a ship.");
			}
			else {
				System.out.println("\nComputer missed");
			}		
			return false;
		}
		return false;
	}
	
	/**
	 * Returns if the last ship hit has been sunk.
	 * @return boolean if last ship hit has been sunk.
	 */
	public boolean sunkShip() { 
		for (int i = 2; i < size; i++) { 
		    for (int j = 2; j < size; j++) { 
		        if (board[i][j].equals(lastShipHit)) { 
		        	return false;
		        }
		    }
		}
		return true;
	}
	
	/**
	 * Identifies the ship based on the first letter.
	 * @param targetSpot - location of ship.
	 * @return String type of ship.
	 */
	private String identifyShip(String targetSpot) {
		switch(targetSpot) {
			case "C":
				return "carrier";
			case "B":
				return "battleship";
			case "D":
				return "destroyer";
			case "S":
				return "submarine";
			case "P":
				return "patrol";
			default:
				return " ";
		}
	}
	
	/**
	 * Returns if all the ships on the board have been sunk.
	 * @return boolean if all ships are sunk.
	 */
	public boolean allSunk() { 
		for (int i = 2; i < size; i++) { 
		    for (int j = 2; j < size; j++) { 
		        if (board[i][j].equals("C") || 
		        	board[i][j].equals("B") ||
		        	board[i][j].equals("S") ||
		        	board[i][j].equals("D") ||
		        	board[i][j].equals("P"))
		        {
		        	return false;
		        }
		    }
		}
		return true;
	}
	
	/**
	 * Prints the board.
	 */
	public void print() {
		for(String[] row : board) { 
			String output = "";
			for(int i = 0; i < row.length; i++) { 
				output += row[i] + "  ";
			}
			System.out.println(output);
		}
	}
}
