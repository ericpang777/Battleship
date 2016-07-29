package battleship;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * The Game class contains the Main method and is where the game runs from.
 * @author Eric Pang
 * @version 1.0
 */
public class Game { 
	private static ArrayList<Point> userUsedPoints; 
	
	public static void main(String[] args) {
		final int SIZE = 12;
		
		Board userDef = new Board(SIZE); 
		Board userAtk = new Board(SIZE);
	    Board compDef = new Board(SIZE);
	    Board compAtk = new Board(SIZE);

	    Scanner scan = new Scanner(System.in);
	    String input;
		do {
			System.out.println("Type G for Game mode and T for Test mode");
			input = scan.nextLine();
		}
		while( !(input.equalsIgnoreCase("G") || input.equalsIgnoreCase("T"))); 
		
		if(input.equalsIgnoreCase("G")) { 
			gameRoute(userDef, userAtk, compDef, compAtk); 
		}
		else {
			testRoute(userDef, userAtk, compDef, compAtk); 
		}
	}
	
	/**
	 * Runs the game route, which doesn't allow user to see computer's boards.
	 * @param uD - User's defense board.
	 * @param uA - User's attack board.
	 * @param cD - Computer's defense board.
	 * @param cA - Computer's attack board.
	 */
	private static void gameRoute(Board uD, Board uA, Board cD, Board cA) {
		AI ai = new AI();
		userUsedPoints = new ArrayList<Point>();
		System.out.println("\nYour current defense board");
		uD.print(); 
		initUserShips(uD); 
		initCompShips(cD, "game");
		System.out.println("\nYour current attack board");
		uA.print(); 
		
		boolean continueGame = true;
		while(continueGame) { 
			//Forever loop until someone wins 
			userTargetting(uA, cD, "game");
			if(cD.allSunk()) { 
				//If all ships sunk, user wins
				System.out.println("You have won!");
				continueGame = false;
			}
			if(continueGame) {
				ai.targetting(uD, cA, "game");
				if(uD.allSunk()) { 
					//If all ships sunk, computer wins
					System.out.println("You have lost...");
					continueGame = false;
				}
			}
		}
	}
	
	/**
	 * Runs the test route which allows user to see the computer's boards, which makes testing a lot easier.
	 * @param uD - User's defense board.
	 * @param uA - User's attack board.
	 * @param cD - Computer's defense board.
	 * @param cA - Computer's attack board.
	 */
	private static void testRoute(Board uD, Board uA, Board cD, Board cA) {
		AI ai = new AI();
		userUsedPoints = new ArrayList<Point>();
		System.out.println("\nYour current defense board");
		uD.print(); 
		initUserShips(uD); 
		initCompShips(cD, "test");
		System.out.println("\nYour current attack board");
		uA.print(); 
	
		boolean continueTest = true;
		while(continueTest) { 
			//Forever loop until someone wins 
			userTargetting(uA, cD, "test");
			if(cD.allSunk()) { 
				//If all ships sunk, user wins
				System.out.println("You have won!");
				continueTest = false;
			}
			if(continueTest) {
				ai.targetting(uD, cA, "test");
				if(uD.allSunk()) { 
					//If all ships sunk, computer wins
					System.out.println("You have lost...");
					continueTest = false;
				}
			}
		}
	}
	
	/**
	 * Sets up the location of the user's ships on user's defense board.
	 * @param board - user's defense board which the ships will be placed on.
	 */
	private static void initUserShips(Board board) {
		Ship[] fleet = new Ship[] {
			new Ship("Carrier"),
			new Ship("Battleship"),
			new Ship("Submarine"),
			new Ship("Destroyer"),
			new Ship("Patrol"),			
		};		
		
		ArrayList<Point> shipUsedPoints = new ArrayList<Point>(); 
		Scanner scan = new Scanner(System.in);
		for(Ship ship : fleet) { 
			System.out.println("\nWhat starting and ending point do you want your "+ ship.getType() +" to be?");
			System.out.println("The "+ ship.getType() +" is "+ ship.getSize() +" squares long.");
			System.out.println("Enter in this format - A1 A"+ ship.getSize() +"\n");

			//Removes whitespace and separates the two words
			String[] splitInput = scan.nextLine().split("\\s+"); 
			int length = splitInput.length;
			//If user enters 1 point, will not crash but will not take more than 2 points
			Point[] input = new Point[2]; 
			
			//Will not crash if user entered nothing, or only spaces
			if(length != 0 && !Validation.isBlankLine(splitInput[0])) { 
				for(int i = 0; i < length && i < 2; i++) { 
					//Only 2 points needed	
					input[i] = new Point(splitInput[i]); 
					//Adds the points to a Point array
				}
			}
			ArrayList<Point> returnedPoints = new ArrayList<Point>(); //Points the ship will take up
			
			//Loop to make sure points are valid
			while(Validation.isInvalidPoint(length, input, ship, shipUsedPoints, returnedPoints)) {
				returnedPoints.clear(); 
				//Clears returned points because previous ones were invalid
				System.out.println("Invalid point(s), please enter again");
				splitInput = scan.nextLine().split("\\s+");
				length = splitInput.length;
				if(length != 0 && !Validation.isBlankLine(splitInput[0])) {
					for(int i = 0; i < length && i < 2; i++) { 
						input[i] = new Point(splitInput[i]);
					}
				}
			}
			
			board.addShip(ship, returnedPoints); //Adds ship
			System.out.println("Your current defense board: ");
			board.print();
		}
	}		
	
	/**
	 * Sets up location of computer's ships on computer's defense board.
	 * @param board - computer's defense board.
	 * @param type - if game or test
	 */
	private static void initCompShips(Board board, String type) {
		System.out.println("\nInitializing computer's board..."); //Just to show more info
		Ship[] fleet = new Ship[] { 
				new Ship("Carrier"),
				new Ship("Battleship"),
				new Ship("Submarine"),
				new Ship("Destroyer"),
				new Ship("Patrol"),			
		};
		
		ArrayList<Point> availablePoints = new ArrayList<Point>(); 
		//Adds all points from A1 - J10
		for(int letter = (int)'A'; letter <= (int)'J'; letter++) { 
			for(int number = 1; number <= 10; number++) {
				availablePoints.add(new Point((char)letter + "" + number));
			}
		}
		
		Random random = new Random(); 
		for(Ship ship : fleet) {
			Point randPoint;
			do { //Gets random point from available points and checks if it works
				randPoint = availablePoints.get(random.nextInt(availablePoints.size()));
			}
			while(Validation.isInvalidPoint(ship, randPoint, availablePoints));
			board.addShip(ship, Validation.getOutput()); 
		}
		System.out.println("Done\n");
		if(type.equals("test")) {
			//Prints computer's board for testing
			board.print(); 
		}
	}
	
	/**
	 * Handles where the user targets to attack.
	 * @param attack - user's attack board.
	 * @param defense - computer's defense board.
	 * @param type - game or test
	 */
	private static void userTargetting(Board attack, Board defense, String type) {
		Scanner scan = new Scanner(System.in);
		System.out.println("\nWhere do you want to target?");
		System.out.println("Enter in this format - A1");
		String nextLine = scan.nextLine();
		Point input = new Point("K0"); //Default value
		if(nextLine.length() != 0 && !Validation.isBlankLine(nextLine)) { 
			//Handles if input is an enter or only spaces
			input = new Point(nextLine.replaceAll("\\s+", ""));  
		}
		while(nextLine == null || usedPoint(input)) { //Loop to make sure point is valid 
			System.out.println("Invalid point(s), please enter again");
			nextLine = scan.nextLine();
			if(nextLine.length() != 0 && !Validation.isBlankLine(nextLine)) { 
				input = new Point(nextLine.replaceAll("\\s+", ""));
			}
		}
		
		//Capitalizing letter
		String letter = input.getStrLet().toUpperCase();
		input = new Point(letter + input.getStrNum());
		boolean hit = defense.hitTarget(input, "user"); //Attacks computer's board
		attack.setTarget(input, hit); 
		
		if(type.equals("test")) { //Testing will print both boards
			System.out.println("Computer's defense board:\t\t  Your attack board:\t\t\t Your Turn");
			printBoards(defense, attack);
		}
		else { //In a game, only user's board will be printed
			System.out.println("Your attack board:\t\t      Your Turn");
			attack.print();
		}
	}
	
	/**
	 * Determines if the user's point has been used before and if it's even a point on the board.
	 * @param input - Point to be tested
	 * @return boolean if a valid point
	 */
	private static boolean usedPoint(Point input) {
		String letter = input.getStrLet().toUpperCase();
		String number = input.getStrNum();
		input = new Point(letter + number);
		
		//If point will fall on board
		if(Validation.isLetter(letter) && Validation.isNumber(number)) 
		{
			if(userUsedPoints.contains(input)) { 
				return true;
			}
			userUsedPoints.add(input);
		}
		else {
			return true;
		}
		return false;
	}
	
	/**
	 * Prints both boards during testing.
	 * @param board1 - first board to be printed.
	 * @param board2 - second board to be printed.
	 */
	public static void printBoards(Board board1, Board board2) {	
		String[][] b1 = board1.getBoard();
		String[][] b2 = board2.getBoard();

		//Board size is 12 
		for(int i = 0; i < 12; i++) { 
			String output = "";
			for(int j = 0; j < 12; j++) { 

				output += b1[i][j] + "  ";
			}
			//First line has a 10, which has extra char, so needs one less space
			if(i == 0) { 
				output += "      ";
			}
			else {
				output += "       ";
			}
			for(int j = 0; j < 12; j++) { 
				output += b2[i][j] + "  ";
			}
			System.out.println(output);
		}
	}
}
