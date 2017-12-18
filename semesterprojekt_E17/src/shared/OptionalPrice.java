package shared;

import java.io.Serializable;

/**
 * The OptionalPrice class
 *
 * @author group 12
 */
public class OptionalPrice implements Serializable {

	private int id;
	private double price;
	private String description;

	public OptionalPrice(int id, double price, String description) {
		this.id = id;
		this.price = price;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
