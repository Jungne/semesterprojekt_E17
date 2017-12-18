package shared;

import java.io.Serializable;

/**
 * InstructorListItem links a user with a category making that user an
 * instructor in that category. InstructorListItem is used in a trip for each
 * instructor in that trip
 *
 * @author group 12
 */
public class InstructorListItem implements Serializable {

	private User user;
	private Category category;

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
