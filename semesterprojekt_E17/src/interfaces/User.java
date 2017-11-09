package interfaces;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

	private int id;
	private String email;
	private String name;
	private List<Category> certificates;
	private byte[] image;

	public User(int id, String email, String name, byte[] image) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.image = image;
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
	public byte[] getImage() {
		return image;
	}

}
