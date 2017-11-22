package client;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class PaneListItem extends Pane {

	private String buttonText;
	private Button button;
	private Button cancelButton;

	public PaneListItem(String buttonText) {
		this.buttonText = buttonText;
		setUpButtons(buttonText);
	}

	protected Button getButton() {
		return button;
	}

	protected Button getCancelButton() {
		return cancelButton;
	}

	private void setUpButtons(String categoryName) {
		//Determines the width of the button
		int imageNameWidth = getTextWidth(categoryName);
		int buttonWidth = imageNameWidth + 4;
		if (buttonWidth < 30) {
			buttonWidth = 30;
		}

		//The main button
		button = new Button(categoryName);
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

		//Attach buttons to pane
		this.getChildren().addAll(button, cancelButton);
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
