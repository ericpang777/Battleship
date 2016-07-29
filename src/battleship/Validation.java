package battleship;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Validation class determines if Point(s) are valid to be used.
 * @author Eric Pang
 * @version 1.0
 */
public class Validation { 
	private static ArrayList<Point> compOutput; 
	
	/**
	 * Determines if a blank line was entered (only spaces)
	 * @param input - user input
	 * @return boolean if a blank line
	 */
	public static boolean isBlankLine(String input) {
		return input.replaceAll("\\s+", "").length() == 0;
	}
	
	/**
	 * Determines if the entered points were invalid (used for initUserShips)
	 * @param length - length of input array
	 * @param input - input array
	 * @param ship - ship to be tested with
	 * @param usedPoints - already used points
	 * @param returnedPoints - the returned points to be used
	 * @return boolean if invalid point
	 */
	public static boolean isInvalidPoint(int length, Point[] input, Ship ship,
			ArrayList<Point> usedPoints, ArrayList<Point> returnedPoints) {	
		
		//If user entered two points, length would be 2
		if(length != 2) {
			return true;
		}
		
		//Checks if points would fall on the grid
		if(! isLetter(input[0].getStrLet()) ||
		   ! isNumber(input[0].getStrNum()) ||
		   ! isLetter(input[1].getStrLet()) ||
		   ! isNumber(input[1].getStrNum()))
		{		
			return true;
		}
		
		//Capitalizes letters if user entered lowercase, and splits letters and numbers
		input[0] = new Point (input[0].getStrLet().toUpperCase() + input[0].getStrNum()); //A1
		input[1] = new Point (input[1].getStrLet().toUpperCase() + input[1].getStrNum()); //A5
		
		//Checks if the point would work in the game
		if(isNotWorkingPoint(input, ship)) { 
			return true;
		}
		
		//Checks if there's already a ship there
		if(usedPoint(input, ship, usedPoints, returnedPoints)) { 
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the letter would fit in the bounds of A-J
	 * @param strLet - letter to be checked
	 * @return boolean letterFits
	 */
	public static boolean isLetter(String strLet) {
		return strLet.matches("[a-jA-J]");
	}
	
	/**
	 * Checks if the number would fit in the bounds of 1-10
	 * @param strNum - number to be checked
	 * @return boolean numberFits
	 */
	public static boolean isNumber(String strNum) {
		int input;
		try { //If number can be parsed
			input = Integer.parseInt(strNum);
		}
		catch(NumberFormatException exception) {
			return false;
		}
		if(input >= 1 && input <= 10) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the points are far apart enough to be the same length as the ship.
	 * @param input - two points of start and end
	 * @param ship - ship to be used
	 * @return boolean if point doesn't work
	 */
	private static boolean isNotWorkingPoint(Point[] input, Ship ship) {
		//When letters are same, numbers will be ship length - 1 far apart
		if(input[0].getCharLet() == input[1].getCharLet()) {
			int firstInt = input[0].getIntNum();
			int secondInt = input[1].getIntNum();
			if(Math.abs(firstInt - secondInt - 1) == ship.getSize()) {
				return false;
			}
		}
		
		//When numbers are same, letters will be ship length - 1 far apart
		else if(input[0].getIntNum() == input[1].getIntNum()) {
			//Must use charAt because firstLet is a String
			if(Math.abs(input[0].getIntLet() - input[1].getIntLet() - 1) == ship.getSize()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if the points that will be used as been used before and adds those points 
	 * to an ArrayList
	 * @param input - two points used
	 * @param ship - ship used
	 * @param usedPoints - used points
	 * @param returnedPoints - returned points
	 * @return boolean if used point
	 */
	private static boolean usedPoint(Point[] input, Ship ship,
			ArrayList<Point> usedPoints, ArrayList<Point> returnedPoints) {
		
		//When letters are same, generate the other points the ship will use
		//those points will have same letter but different numbers
		if(input[0].getCharLet() == input[1].getCharLet()) {
			int min = Math.min(input[0].getIntNum(), input[1].getIntNum());
			for(int i = 0; i < ship.getSize(); i++) { 
				returnedPoints.add(new Point(input[0].getStrLet() + (min + i)));
			}
		}
		
		//When numbers are same, generate the other points the ship will use
		//Those points will have same number but different letters
		else if(input[0].getIntNum() == input[1].getIntNum()) {
			int min = Math.min(input[0].getIntLet(), input[1].getIntLet());
			for(int i = 0; i < ship.getSize(); i++) {
				returnedPoints.add(new Point((char)(min+i) + input[0].getStrNum()));
			}
		}
		
		//Check if points were used before
		for(Point currentPoint : returnedPoints) {
			if(usedPoints.contains(currentPoint)) {
				returnedPoints.clear();
				return true;
			}
		}
		
		//Adds current points to used points
		for(Point currentPoint : returnedPoints) {
			usedPoints.add(currentPoint);
		}
		return false;
	}
	
	/**
	 * Checks if the point is an invalid point (used in initCompShips)
	 * @param ship - ship used
	 * @param randPoint - random point that was used
	 * @param availablePoints - available points to be used
	 * @return boolean if invalid point
	 */
	public static boolean isInvalidPoint(Ship ship, Point randPoint, 
			ArrayList<Point> availablePoints) { 
		
		compOutput = new ArrayList<Point>();
		
		compOutput = genEndPoint(ship, randPoint, availablePoints);
		if(compOutput.isEmpty()) { //Empty output means invalid point
			compOutput.clear();
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the points that will be used by the ship
	 * @return ArrayList<Point> output
	 */
	public static ArrayList<Point> getOutput() { 
		return compOutput;
	}
	
	/**
	 * Generates possible orientations of the ship, and then chooses one to add to the final output.
	 * @param ship - ship used
	 * @param randPoint - start point
	 * @param availablePoints - available points to be used
	 * @return ArrayList<Point> points ship will use up
	 */
	private static ArrayList<Point> genEndPoint(Ship ship, Point randPoint, 
			ArrayList<Point> availablePoints) {
		
		char letter = randPoint.getCharLet();
		int number = randPoint.getIntNum();
		
		String lowerLet = "" + (char)(letter - ship.getSize() + 1);
		String higherLet = "" + (char)(letter + ship.getSize() - 1);
		String lowerNum = "" + (number - ship.getSize() + 1);
		String higherNum = "" + (number + ship.getSize() - 1);

		//Generates a lower letter, to see if it works. Example: B2 -> A2
		if(isLetter(lowerLet)) {
			compOutput.add(new Point(lowerLet + number));
		}		
		//Generates a higher letter, to see if it works. Example: B2 -> C2
		if(isLetter(higherLet)) {	
			compOutput.add(new Point(higherLet + number));	
		}		
		//Generates a lower number, to see if it works. Example: B2 -> B1
		if(isNumber(lowerNum)) {	
			compOutput.add(new Point(letter + lowerNum));	
		}		
		//Generates a higher number, to see if it works. Example: B2 -> B3
		if(isNumber(higherNum)) {
			compOutput.add(new Point(letter + higherNum));		
		}
		
		//Chooses random direction and sees if it works
		Random random = new Random();
		int size = compOutput.size();
		boolean foundPoint = false;
		for(int i = 0; i < size && !foundPoint; i++) {
			Point point = compOutput.get(random.nextInt(compOutput.size()));
			if(unusedPoint(point, randPoint, ship, availablePoints)) {	 
				foundPoint = true;
			}
			else {
				compOutput.remove(point);
			}
		}
		return compOutput;
	}
	
	/**
	 * Generates the other points the ship will use up.
	 * @param testPoint - the end point
	 * @param randPoint - the start point
	 * @param ship - ship used
	 * @param availablePoints - available points to be used
	 * @return boolean if those points were unused
	 */
	private static boolean unusedPoint(Point testPoint, Point randPoint, Ship ship,
			ArrayList<Point> availablePoints) {
		
		String firstLet = testPoint.getStrLet();
		String firstNum = testPoint.getStrNum();
		String secondLet = randPoint.getStrLet();
		String secondNum = randPoint.getStrNum();
		
		ArrayList<Point> testPoints = new ArrayList<Point>();
		//When letters are same, generate the other points the ship will use
		//Those points will have same letter but different numbers
		if(firstLet.equals(secondLet)) { 
			int min = Math.min(Integer.parseInt(firstNum), Integer.parseInt(secondNum));
			for(int i = 0; i < ship.getSize(); i++) {
				testPoints.add(new Point(firstLet + (min + i)));
			}
		}
		
		//When numbers are same, generate the other points the ship will use
		//Those points will have same number but different letters
		else if(firstNum.equals(secondNum)) {
			int min = Math.min((int)firstLet.charAt(0), (int)secondLet.charAt(0));
			for(int i = 0; i < ship.getSize(); i++) {
				testPoints.add(new Point((char)(min+i) + firstNum));
			}
		}
		
		//If available points contains current points
		for(Point currentPoint : testPoints) {
			if(!availablePoints.contains(currentPoint)) { 
				return false;
			}		
		}
		
		//Then add them to output
		compOutput.clear();
		for(Point currentPoint : testPoints) {
			compOutput.add(currentPoint);
			availablePoints.remove(currentPoint);
		}
		return true;
	}
}
