package interfaces;

import java.io.Serializable;

public class Location implements Serializable {

	private int id;
	private String name;

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
}
