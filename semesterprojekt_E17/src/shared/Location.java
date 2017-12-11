package shared;

import java.io.Serializable;

/**
 * The Location class is used in a trip. A trip can contain one location.
 *
 * @author group 12
 */
public class Location implements Serializable {

	private int id;
	private String name;

	/**
	 * This constructor creates a location. A location has an ID and a name.
	 *
	 * @param id
	 * @param name
	 */
	public Location(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}
