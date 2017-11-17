package client;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ImageItem extends Pane {

	private HBox parent;
	private String imageName;
	private byte[] imageFile;
	private Button button;
	private Button cancelButton;

	public ImageItem(HBox parent, String imageName, byte[] imageFile) {
		this.parent = parent;
		this.imageName = imageName;
		this.imageFile = imageFile;

		setUpButtons(imageName);
		this.getChildren().addAll(button, cancelButton);
	}

	private void setUpButtons(String imageName) {
		//Determines the width of the button
		int imageNameWidth = getTextWidth(imageName);
		int buttonWidth = imageNameWidth + 4;
		if (buttonWidth < 30) {
			buttonWidth = 30;
		}

		//The main button
		button = new Button(imageName);
		button.setMinSize(buttonWidth + 24, 22);
		button.setMaxSize(buttonWidth + 24, 22);

		//Styling
		String buttonDefaultStyle
						= "-fx-focus-traversable: false; "
						+ "-fx-font-size: 10px; "
						+ "-fx-alignment: baseline-left";
		button.setStyle(buttonDefaultStyle);

		//The small button
		cancelButton = new Button("❌");
		cancelButton.setMinSize(24, 22);
		cancelButton.setMaxSize(24, 22);
		cancelButton.setLayoutX(buttonWidth);

		//Styling
		String cancelButtonDefaultStyle
						= "-fx-background-color: transparent; "
						+ "-fx-focus-traversable: false; "
						+ "-fx-font-size: 10px; "
						+ "-fx-text-fill: gray;";

		String cancelButtonHoverStyle
						= "-fx-background-color: transparent; "
						+ "-fx-focus-traversable: false; "
						+ "-fx-font-size: 10px; "
						+ "-fx-text-fill: black;";

		cancelButton.setStyle(cancelButtonDefaultStyle);
		cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(cancelButtonHoverStyle));
		cancelButton.setOnMouseExited(e -> cancelButton.setStyle(cancelButtonDefaultStyle));

		//Handle button click
		cancelButton.setOnAction(e -> parent.getChildren().remove(this));
	}

	/**
	 * Calculates the pixel width of the given text and returnes it.
	 *
	 * @param text
	 * @return
	 */
	private int getTextWidth(String text) {
		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
		Font font = new Font("System", Font.PLAIN, 10);
		return (int) (font.getStringBounds(text, frc).getWidth());
	}

}
