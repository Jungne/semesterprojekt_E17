package client;

import interfaces.Conversation;
import interfaces.Message;
import interfaces.Trip;
import interfaces.User;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HBoxCell extends HBox {

	ImageView imageView = new ImageView();
	Label title = new Label();
	Label description = new Label();
	Label price = new Label();
	Label category = new Label();

	private int id;
	private String type;

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

		InputStream inputStream = new ByteArrayInputStream(user.getImage());
		imageView.setImage(new Image(inputStream));

		title.setText(user.getName());
		title.setPrefWidth(150);
//		title.setStyle("-fx-font-weight: bold");

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
		//		title.setStyle("-fx-font-weight: bold");

		type = conversation.getType();

		this.getChildren().addAll(title);
	}

	public HBoxCell(Message message, User user) {
		id = message.getId();
		description.setText(message.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm")));
		title.setText(message.getSender().getName() + ": " + message.getMessage());

		VBox vbox = new VBox();
		vbox.getChildren().addAll(description, title);

		String cssLayout = "-fx-border-color: black;\n"
						+ "-fx-border-insets: 5;\n"
						+ "-fx-border-width: 3;\n";

		vbox.setStyle(cssLayout);

		if (message.getSenderId() == user.getId()) {
			Region region = new Region();
			HBox.setHgrow(region, Priority.ALWAYS);
			this.getChildren().addAll(region, vbox);
		} else {
			this.getChildren().addAll(vbox);
		}
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

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return id + title.getText();
	}

}
