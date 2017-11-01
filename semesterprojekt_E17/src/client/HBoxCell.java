package client;

import interfaces.Trip;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HBoxCell extends HBox {

	ImageView imageView = new ImageView();
	Label title = new Label();
	Label description = new Label();
	Label price = new Label();
	Label category = new Label();
	
	private int id;

	//Creates an HBoxCell for a trip.
	public HBoxCell(Trip trip) {
		//Sets the padding and spacing.
		super();
		this.setSpacing(10);

		id = trip.getId();

		if (!trip.getImages().isEmpty()) {
			InputStream inputStream = new ByteArrayInputStream(trip.getImages().get(0));
			imageView.setImage(new Image(inputStream));
		} else {
			imageView.setImage(new Image("")); //Add default image or not.
		}

		title.setText(trip.getTitle());
		title.setPrefWidth(150);
		price.setText(Double.toString(trip.getPrice()) + "kr");

		imageView.setFitWidth(100);
		imageView.setPreserveRatio(true);

		this.getChildren().addAll(imageView, title, price);
	}

	public int getProductId() {
		return id;
	}

	public int getImageId() {
		return id;
	}

	@Override
	public String toString() {
		return id + title.getText();
	}
}
