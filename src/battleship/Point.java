package battleship;

/**
 * The Point class stores coordinates in letters and numbers.
 * @author Eric Pang
 * @version 1.0
 */
public class Point { 
	private String letter;
	private String number;
	
	/**
	 * The Point constructor takes in a point and splits it into its letter and number
	 * @param input
	 */
	public Point(String input) { 
		letter = "" + input.charAt(0);
		number = input.substring(1, input.length());
	}
	
	/**
	 * Gets the String version of the letter
	 * @return String letter
	 */
	public String getStrLet() { 
		return letter;
	}
	
	/**
	 * Gets char version of the letter
	 * @return char letter
	 */
	public char getCharLet() { 
		return letter.charAt(0);
	}
	
	/**
	 * Gets int version of the letter (ASCII)
	 * @return int letter
	 */
	public int getIntLet() { 
		return (int)letter.charAt(0);
	}
	
	/**
	 * Gets String version of the number
	 * @return String number
	 */
	public String getStrNum() { 
		return number;
	}
	
	/**
	 * Gets int version of number
	 * @return int number
	 */
	public int getIntNum() { 
		return Integer.parseInt(number);
	}
	
	/**
	 * Gets String version of Point
	 * @return String point
	 */
	public String getPoint() { 
		return letter + number;
	}
	
	/**
	 * Overrides the equals class so a Point can be compared to another Point or String
	 * @return boolean if equals the other
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Point) {
			return (letter + number).equals(((Point)obj).getPoint());
		}
		if(obj instanceof String) {
			return (letter + number).equals(obj);
		}
		return false;
	}
}
