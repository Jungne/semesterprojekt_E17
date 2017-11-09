package client;

import interfaces.Trip;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

		if (trip.getImages().get(0).length > 0) {
			InputStream inputStream = new ByteArrayInputStream(trip.getImages().get(0));
			imageView.setImage(new Image(inputStream));
			System.out.println(trip.getImages().get(0));
		} else {
			imageView.setImage(new Image("default.jpg")); //Add default image or not.
		}

		title.setText(trip.getTitle());
		title.setPrefWidth(150);
		title.setStyle("-fx-font-weight: bold");

		description.setText(trip.getDescription());
		description.setPrefWidth(150);

		VBox vbox1 = new VBox();
		vbox1.getChildren().addAll(title, description);

		price.setText(Double.toString(trip.getPrice()) + "kr");

//		if (!trip.getCategories().isEmpty()) {
//			category.setText(trip.getCategories().get(0).getName());
//		}

		VBox vbox2 = new VBox();
		vbox2.getChildren().addAll(price, category);

		imageView.setFitWidth(100);
		imageView.setPreserveRatio(true);

		this.getChildren().addAll(imageView, vbox1, vbox2);
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
