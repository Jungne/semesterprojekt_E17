package client;

import interfaces.Conversation;
import interfaces.Message;
import interfaces.Trip;
import interfaces.User;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javafx.scene.control.Label;
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

		if (trip.getImages().get(0).getImageFile().length > 0) {
			InputStream inputStream = new ByteArrayInputStream(trip.getImages().get(0).getImageFile());
			imageView.setImage(new javafx.scene.image.Image(inputStream));
		} else {
			imageView.setImage(new javafx.scene.image.Image("default.jpg")); //Add default image or not.
		}

		title.setText(trip.getTitle());
		title.setPrefWidth(150);
		title.setStyle("-fx-font-weight: bold");

		description.setText(trip.getDescription());
		description.setPrefWidth(150);

		VBox vbox1 = new VBox();
		vbox1.getChildren().addAll(title, description);

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

		InputStream inputStream = new ByteArrayInputStream(user.getImage().getImageFile());
		imageView.setImage(new javafx.scene.image.Image(inputStream));

		title.setText(user.getName());
		title.setPrefWidth(150);
		//title.setStyle("-fx-font-weight: bold");

		imageView.setFitWidth(100);
		imageView.setPreserveRatio(true);

		this.getChildren().addAll(imageView, title);
	}

	public HBoxCell(Conversation conversation, String name) {
		//Sets the padding and spacing.
		super();
		this.setSpacing(10);

		id = conversation.getId();

		title.setText(name);
		title.setPrefWidth(150);
		//title.setStyle("-fx-font-weight: bold");

		this.getChildren().addAll(title);
	}

	public HBoxCell(Message message) {
		id = message.getId();
		title.setText(message.getMessage());
	}

	public int getTripId() {
		return id;
	}

	/**
	 * Undskyld redundans
	 *
	 * @return
	 */
	public int getUserId() {
		return id;
	}

	public int getConversationId() {
		return id;
	}

	@Override
	public String toString() {
		return id + title.getText();
	}

}
