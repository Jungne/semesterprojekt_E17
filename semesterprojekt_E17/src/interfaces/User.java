package interfaces;

import java.io.Serializable;
import java.util.List;
import javafx.scene.image.Image;

public class User implements Serializable {

	private int id;
	private String email;
	private String name;
	private List<Category> certificates;
	private Image image;

	public User(int id, String email, String name, List<Category> certificates) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.certificates = certificates;
	}

	public void addCertificate(Category certificate) {
		getCertificates().add(certificate);
	}

	public void removeCertificate(Category certificate) {
		getCertificates().remove(certificate); //Don't know if this works
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

}
