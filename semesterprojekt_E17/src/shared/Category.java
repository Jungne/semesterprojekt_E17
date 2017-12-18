package shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Category is used by Trip and User.
 *
 * @author group 12
 */
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

	/**
	 * This method
	 *
	 * @return
	 */
	public Category clone() {
		return new Category(id, name);
	}

	/**
	 *
	 * @return
	 */
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
	 * @return a list of category Ids
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
