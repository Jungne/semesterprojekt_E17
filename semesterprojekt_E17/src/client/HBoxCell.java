package client;

import interfaces.Trip;
import interfaces.User;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HBoxCell extends HBox {

	ImageView imageView = new ImageView();
	Label userName = new Label();
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
		} else {
			imageView.setImage(new Image("default.jpg")); //Add default image or not.
		}

		userName.setText(trip.getTitle());
		userName.setPrefWidth(150);
		userName.setStyle("-fx-font-weight: bold");

		description.setText(trip.getDescription());
		description.setPrefWidth(150);

		VBox vbox1 = new VBox();
		vbox1.getChildren().addAll(userName, description);

		price.setText(Double.toString(trip.getPrice()) + "kr");

		/*
		if (!trip.getCategories().isEmpty()) {
			category.setText(trip.getCategories().get(0).getName());
		}*/
		VBox vbox2 = new VBox();
		vbox2.getChildren().addAll(price, category);

		imageView.setFitWidth(100);
		imageView.setPreserveRatio(true);

		this.getChildren().addAll(imageView, vbox1, vbox2);
	}

	public HBoxCell(User user) {
		//Sets the padding and spacing.
		super();
		this.setSpacing(10);

		id = user.getId();

		InputStream inputStream = new ByteArrayInputStream(user.getImage());
		imageView.setImage(new Image(inputStream));

		userName.setText(user.getName());
		userName.setPrefWidth(150);
//		userName.setStyle("-fx-font-weight: bold");

		imageView.setFitWidth(100);
		imageView.setPreserveRatio(true);

		this.getChildren().addAll(imageView, userName);
	}

	public int getTripId() {
		return id;
	}
	
	/**
	 * Undskyld redundans
	 * @return 
	 */
	public int getUserId() {
		return id;
	}

	@Override
	public String toString() {
		return id + userName.getText();
	}

}
