package shared;

import java.io.Serializable;

/**
 * The InstructorListItem class
 *
 * @author group 12
 */
public class InstructorListItem implements Serializable {

	private User user;
	private Category category;

	/**
	 * This constructor
	 *
	 * @param user
	 * @param category
	 */
	public InstructorListItem(User user, Category category) {
		this.user = user;
		this.category = category;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

}
