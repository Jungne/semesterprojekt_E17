package interfaces;

import java.io.Serializable;

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
