package interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

	public Category clone() {
		return new Category(id, name);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Category)) {
			return false;
		}
		Category otherCategory = (Category) other;
		return this.id == otherCategory.id;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Takes a list of categories and return a list of cetegories ids. Returns
	 * null just one category is null.
	 *
	 * @param categories
	 * @return
	 */
	public static List<Integer> getCategoryIds(List<Category> categories) {
		ArrayList<Integer> categoryIds = new ArrayList<>();
		for (Category category : categories) {
			if (category == null) {
				return null;
			}
			categoryIds.add(category.getId());
		}
		return categoryIds;
	}

}
