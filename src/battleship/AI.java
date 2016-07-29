package battleship;

import java.util.ArrayList;
import java.util.Random;

/**
 * The AI class will be how the computer targets the user's ships.
 * @author Eric Pang
 * @version 1.0
 */
public class AI { 
	private ArrayList<Point> compAtkAvlble; //Available Points to choose from  
	boolean lastHit; //If last attack hit
	private ArrayList<ArrayList<Point>> pointsAround; //Points around the last hit 
	private int direction = 0; 
	//Same as directions below, starts at North
	private final int NORTH = 0;
	private final int EAST = 1;
	private final int SOUTH = 2;
	private final int WEST = 3;
	
	/**
	 * The AI constructor initializes the available points to attack.
	 */
	public AI() { 
		compAtkAvlble = new ArrayList<Point>();
		pointsAround = new ArrayList<ArrayList<Point>>();
		for(int i = 0; i < 4; i++) { 
			//Will have 4 different directions around it
			pointsAround.add(new ArrayList<Point>());
		}

		for(int letter = (int)'A'; letter <= (int)'J'; letter++) { 
			//Adds points from A1 - J10 to available points
			for(int number = 1; number <= 10; number++) { 
				compAtkAvlble.add(new Point((char)letter + "" + number));
			}
		}
	}
	
	/**
	 * The targeting system of the AI and will print the boards
	 * @param uD - user's defense board
	 * @param cA - computer's attack board
	 * @param type - game or test
	 */
	public void targetting(Board uD, Board cA, String type) {		
		//If last attack missed, proceed with normal random targetting
		if(!lastHit) { 
			target(uD, cA);
		}
		else { 
			hittingStreak(uD, cA); 
		}
		
		if(type.equals("game")) { 
			System.out.println("Your defense board:\t\t     Computer's Turn");
			uD.print();
		}
		if(type.equals("test")) { //Test will print 2 boards
			System.out.println("Your defense board:\t\t\t  Computer's attack board:\t\t Computer's Turn");
			Game.printBoards(uD, cA);
		}
	}
	
	/**
	 * The random targeting of points on the board in effort to hit a ship.
	 * @param uD - user's defense board
	 * @param cA - computer's attack board
	 */
	private void target(Board uD, Board cA) {
		Random random = new Random(); 
		
		Point randPoint = compAtkAvlble.get(random.nextInt(compAtkAvlble.size()));
		compAtkAvlble.remove(randPoint);
		
		System.out.println("\nComputer targetted " + randPoint.getPoint());
		boolean hit = uD.hitTarget(randPoint, "comp");
		cA.setTarget(randPoint, hit);
		
		//If hit, generate points around the hit
		if(hit) { 
			genPointsAround(randPoint);
		}
	}
	
	/**
	 * Used when a ship has just been hit. Will search the points around that hit to hunt down the ship.
	 * @param uD - user's defense board
	 * @param cA - computer's attack board
	 */
	private void hittingStreak(Board uD, Board cA) {
		//Prevent using an empty direction
		while(direction < 4 && pointsAround.get(direction).isEmpty()) {
			direction++;
		}
		boolean allEmpty = true;
		for(ArrayList<Point> list : pointsAround) { 
			if(!list.isEmpty()) { 
				allEmpty = false;
			}
		}
		if(!allEmpty) { 
			//Get last point in that arraylist
			int last = pointsAround.get(direction).size() - 1;
			Point point = pointsAround.get(direction).get(last);
			compAtkAvlble.remove(point); //Removes point from available points
			
			System.out.println("\nComputer targetted " + point.getPoint());
			//Targets last point in last direction used
			boolean hit = uD.hitTarget(point, "comp"); 
			cA.setTarget(point, hit);	
			//Generates next point to attack using that point
	    	genNextPoint(hit, uD); 
		}
		else { //Would mean that cannot target any new points, revert back to random targeting
			target(uD, cA);
			direction = 0; //Resets to 0
			lastHit = false;
			for(ArrayList<Point> list : pointsAround) { 
				list.clear(); //Clears the points around
			}
		}
	}
	
	/**
	 * Generates the points around a hit.
	 * @param point - point that hit a ship.
	 */
	private void genPointsAround(Point point) {	
		char letter = point.getCharLet();
		int number = point.getIntNum();
		lastHit = true; //If this was B2
		
		if(Validation.isLetter("" + (char)(letter - 1))) { //This is A2
			if(compAtkAvlble.contains(new Point((char)(letter - 1) + "" + number))) {
				pointsAround.get(NORTH).add(point);
				pointsAround.get(NORTH).add(new Point((char)(letter - 1) + "" + number));
			}
		}

		if(Validation.isNumber("" + (number + 1))) { //This is B3
			if(compAtkAvlble.contains(new Point(letter + "" + (number + 1)))) {
				pointsAround.get(EAST).add(point);
				pointsAround.get(EAST).add(new Point(letter + "" + (number + 1)));
			}
		}
		if(Validation.isLetter("" + (char)(letter + 1))) { //This is C2
			if(compAtkAvlble.contains(new Point((char)(letter + 1) + "" + number))) {
				pointsAround.get(SOUTH).add(point);
				pointsAround.get(SOUTH).add(new Point((char)(letter + 1) + "" + number));
			}
		}
		if(Validation.isNumber("" + (number - 1))) { //This is B1
			if(compAtkAvlble.contains(new Point(letter + "" + (number - 1)))) {
				pointsAround.get(WEST).add(point);
				pointsAround.get(WEST).add(new Point(letter + "" + (number - 1)));
			}
		}
	}
	
	/**
	 * Generates the next point to attack along a direction.
	 * @param hit - if the last point hit.
	 * @param uD - user's defense board.
	 */
	private void genNextPoint(boolean hit, Board uD) {
		//Last point
		int end = pointsAround.get(direction).size() - 1;
		Point lastPoint = pointsAround.get(direction).get(end);
		Point secLastPoint = pointsAround.get(direction).get(end - 1);
		char firstLet = secLastPoint.getCharLet();
		int firstNum = secLastPoint.getIntNum();
		char secondLet = lastPoint.getCharLet();
		int secondNum = lastPoint.getIntNum();

		//If hit but ship isn't sunk, generate another point
		if(hit && !uD.sunkShip()) { 
			int numDiff = firstNum - secondNum;
			int letDiff = (int)firstLet - (int)secondLet;
			String newNum = secondNum - numDiff + "";
			String newLet = (char)(secondLet - letDiff) + "";
			
			//If A1 and A2, needs to generate A3. Checks if would work and has not been used
			if(firstLet == secondLet       && 
			   Validation.isNumber(newNum) && 
			   compAtkAvlble.contains(new Point(firstLet + "" + (secondNum - numDiff)))) 
			{	
				pointsAround.get(direction).add(new Point(firstLet + "" + (secondNum - numDiff)));	
			}
			//Same as above but if it's A1 and B1 instead
			else if(firstNum == secondNum       && 
				    Validation.isLetter(newLet) &&
					compAtkAvlble.contains(new Point((char)(secondLet - letDiff) + "" + firstNum))) 
			{
				pointsAround.get(direction).add(new Point((char)(secondLet - letDiff) + "" + firstNum));
			}
			//Otherwise means that the point would go out of bounds
			else {
				pointsAround.get(direction).clear(); 
			}
		}	
		//If hit and ship is sunk, clear everything and resume random targeting
		else if(hit && uD.sunkShip()) {
			lastHit = false;
			for(ArrayList<Point> list : pointsAround) { 
				list.clear();
			}
			direction = 0;
		}
		//Change directions
		else {
			pointsAround.get(direction).clear(); //Missed, no ships there
		}
	}
}
