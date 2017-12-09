package client;

import interfaces.Conversation;
import interfaces.Message;
import interfaces.Trip;
import interfaces.User;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * The HBoxCell
 *
 * @author group 12
 */
public class HBoxCell extends HBox {

	ImageView imageView = new ImageView();
	Label label1 = new Label();
	Label label2 = new Label();
	Label label3 = new Label();
	Label label4 = new Label();

	private int id;
	private String type;

	/**
	 * Creates an HBoxCell for a trip.
	 *
	 * @param trip
	 */
	public HBoxCell(Trip trip) {
		//Sets the padding and spacing.
		super();
		this.setSpacing(10);

		id = trip.getId();

		if (trip.getImages().isEmpty()) {
			imageView.setImage(new javafx.scene.image.Image("default_pictures/default_trip_picture.png"));
		} else {
			InputStream inputStream = new ByteArrayInputStream(trip.getImages().get(0).getImageFile());
			imageView.setImage(new javafx.scene.image.Image(inputStream));
		}

		label1.setText(trip.getTitle());
		label1.setPrefWidth(150);
		label1.setStyle("-fx-font-weight: bold");

		label2.setText(trip.getDescription());
		label2.setPrefWidth(150);

		VBox vbox1 = new VBox();
		vbox1.getChildren().addAll(label1, label2);

		label3.setText(Double.toString(trip.getPrice()) + "kr");

		VBox vbox2 = new VBox();
		vbox2.getChildren().addAll(label3, label4);

		imageView.setFitWidth(100);
		imageView.setPreserveRatio(true);

		this.getChildren().addAll(imageView, vbox1, vbox2);
	}

	/**
	 * Creates an HBoxCell for a user.
	 *
	 * @param user
	 */
	public HBoxCell(User user) {
		//Sets the padding and spacing.
		super();
		this.setSpacing(10);

		id = user.getId();

		if (user.getImage() == null) {
			imageView.setImage(new javafx.scene.image.Image("default_pictures/default_profile_picture.png"));
		} else {
			InputStream inputStream = new ByteArrayInputStream(user.getImage().getImageFile());
			imageView.setImage(new javafx.scene.image.Image(inputStream));
		}

		label1.setText(user.getName());
		label1.setPrefWidth(150);
		//title.setStyle("-fx-font-weight: bold");

		imageView.setFitWidth(100);
		imageView.setPreserveRatio(true);

		this.getChildren().addAll(imageView, label1);
	}

	/**
	 * Creates an HBoxCell for a conversation.
	 *
	 * @param conversation
	 * @param name
	 */
	public HBoxCell(Conversation conversation, String name) {
		//Sets the padding and spacing.
		super();
		this.setSpacing(10);

		id = conversation.getId();

		label1.setText(name);
		label1.setPrefWidth(150);

		type = conversation.getType();

		this.getChildren().addAll(label1);
	}

	public HBoxCell(Message message, User user) {
		this.setPrefWidth(50);

		id = message.getId();

		label1.setText(message.getSender().getName() + ":");
		label1.setStyle("-fx-font-weight: bold;");

		label2.setText(message.getMessage());
		label2.setWrapText(true);
		label2.setTextAlignment(TextAlignment.JUSTIFY);

		label3.setText(message.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm")));
		label3.setStyle("-fx-font-style: italic;");

		VBox vbox = new VBox();
		vbox.getChildren().addAll(label1, label2, label3);

		String cssLayout = "-fx-border-color: black;\n"
						+ "-fx-border-insets: 5;\n"
						+ "-fx-border-width: 1;\n";

		vbox.setStyle(cssLayout);
		vbox.setPadding(new Insets(3));

		if (message.getSenderId() == user.getId()) {
			Region region = new Region();
			HBox.setHgrow(region, Priority.ALWAYS);
			this.getChildren().addAll(region, vbox);
		} else {
			this.getChildren().addAll(vbox);
		}
	}

	/**
	 * HBoxCell with id and text label.
	 *
	 * @param id
	 * @param text
	 */
	public HBoxCell(int id, String text) {
		this.id = id;
		label1.setText(text);

		this.getChildren().add(label1);
	}

	/**
	 * @return the trip ID.
	 */
	public int getTripId() {
		return id;
	}

	/**
	 * @return the user ID.
	 */
	public int getUserId() {
		return id;
	}

	/**
	 * @return the conversation ID.
	 */
	public int getConversationId() {
		return id;
	}

	/**
	 * @return the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return text in the label.
	 */
	public String getLabel1Text() {
		return label1.getText();
	}

	@Override
	public String toString() {
		return id + label1.getText();
	}

}
