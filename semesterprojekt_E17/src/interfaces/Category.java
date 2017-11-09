package interfaces;

import java.io.Serializable;

public class Category implements Serializable {

	private int id;
	private String name;

	public Category(int id, String name) {
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
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Category)) {
			return false;
		}
		Category otherCategory = (Category) other;
		if (this.id == otherCategory.id) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return name;
	}

}
