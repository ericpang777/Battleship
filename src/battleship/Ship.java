package battleship;

/**
 * The Ship class stores the attributes of a specific ship.
 * @author Eric Pang
 * @version 1.0
 */
public class Ship {	
	private int size;
	private String type;
	
	/**
	 * The Ship constructors converts the ship type into its attributes.
	 * @param type - type of ship given.
	 */
	public Ship(String type) { 	
		switch(type) {
			case "Patrol":
				size = 2;
				this.type = type;
				break;
			case "Destroyer":
				size = 3;
				this.type = type;
				break;
			case "Submarine":
				size = 3;
				this.type = type;
				break;
			case "Battleship":
				size = 4;
				this.type = type;
				break;
			case "Carrier":
				size = 5;
				this.type = type;
				break;
			default:
				break;
		}
	}
	
	/**
	 * Gets the type of ship.
	 * @return String type of ship.
	 */
	public String getType() { 
		return type;
	}
	
	/**
	 * Gets the ship's first letter.
	 * @return String ship's first letter.
	 */
	public String getCharType() { 
		return type.substring(0, 1);
	}
	
	/**
	 * Gets amount of squares the ship takes up.
	 * @return int size of ship.
	 */
	public int getSize() { 
		return size;
	}
}
