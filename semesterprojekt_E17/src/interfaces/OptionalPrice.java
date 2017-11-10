package interfaces;

import java.io.Serializable;

public class OptionalPrice implements Serializable {

	private int id;
	private double price;
	private String description;

	public OptionalPrice(int id, double price, String description) {
		this.id = id;
		this.price = price;
		this.description = description;
	}

	public OptionalPrice(double price, String description) throws Exception {
		if (price <= 0) {
			throw new Exception("invalid price");
		}
		this.price = price;
		if (description == null) {
			throw new Exception("invalid description");
		}
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
