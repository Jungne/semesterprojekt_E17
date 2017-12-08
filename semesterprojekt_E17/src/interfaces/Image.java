package interfaces;

import java.io.Serializable;

/**
 * The Image class
 *
 * @author group 12
 */
public class Image implements Serializable {

	private int id;
	private String title;
	private byte[] imageFile;

	/**
	 * This constructor
	 *
	 * @parm id
	 * @param title
	 * @param imageFile
	 */
	public Image(int id, String title, byte[] imageFile) {
		this.id = id;
		this.title = title;
		this.imageFile = imageFile;
	}

	/**
	 * This constructor
	 *
	 * @param title
	 * @param imageFile
	 */
	public Image(String title, byte[] imageFile) {
		this.title = title;
		this.imageFile = imageFile;
	}

	/**
	 * This constructor
	 *
	 * @param imageFile
	 */
	public Image(byte[] imageFile) {
		this.imageFile = imageFile;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the imageFile
	 */
	public byte[] getImageFile() {
		return imageFile;
	}

}
