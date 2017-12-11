package shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The User class represents a user in the system.
 *
 * @author group 12
 */
public class User implements Serializable {

	private int id;
	private String email;
	private String name;
	private List<Category> certificates = new ArrayList<>();
	private Image image;

	/**
	 * Constructor to
	 *
	 * @param id
	 * @param email
	 * @param name
	 * @param certificates
	 * @param image
	 */
	public User(int id, String email, String name, List<Category> certificates, Image image) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.certificates = certificates;
		this.image = image;
	}

	/**
	 * Constructor to
	 *
	 * @param id
	 * @param email
	 * @param name
	 * @param image
	 */
	public User(int id, String email, String name, Image image) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.image = image;
	}

	/**
	 * Constructor to
	 *
	 * @param email
	 * @param name
	 * @param image
	 */
	public User(String email, String name, Image image) {
		this.email = email;
		this.name = name;
		this.image = image;
	}

	/**
	 * Constructor to
	 *
	 * @param id
	 * @param email
	 * @param name
	 */
	public User(int id, String email, String name) {
		this.id = id;
		this.email = email;
		this.name = name;
	}

	/**
	 * Constructor to
	 *
	 * @param id
	 * @param name
	 */
	public User(int id, String name) {
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the certificates
	 */
	public List<Category> getCertificates() {
		return certificates;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof User)) {
			return false;
		}
		return id == ((User) other).getId();
	}

}
