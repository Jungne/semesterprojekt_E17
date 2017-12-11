package client;

import shared.Category;
import javafx.scene.control.Button;

public class CategoryListItem extends PaneListItem {

	private FXMLDocumentController fxmlController;
	private Category category;
	private boolean isInstructor;

	public CategoryListItem(FXMLDocumentController fxmlController, Category category) {
		super(category.getName());
		this.fxmlController = fxmlController;
		this.category = category;
		this.isInstructor = false;
		setCancelButtonAction();
		setButtonAction();
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @return the isInstructor
	 */
	public boolean isInstructor() {
		return isInstructor;
	}

	private void setCancelButtonAction() {
		getCancelButton().setOnAction(e -> fxmlController.removeCategoryListItem(this));
	}

	private void setButtonAction() {
		Button button = getButton();

		//Styling
		String buttonDefaultStyle
						= "-fx-focus-traversable: false; "
						+ "-fx-font-size: 10px; "
						+ "-fx-alignment: baseline-left";

		String buttonPressedStyle
						= "-fx-background-color: transparent, transparent, lightblue, transparent;"
						+ "-fx-focus-traversable: false; "
						+ "-fx-font-size: 10px; "
						+ "-fx-alignment: baseline-left";

		//Handle button click
		button.setOnAction(e -> {
			if (isInstructor) {
				button.setStyle(buttonDefaultStyle);
				isInstructor = false;
			} else {
				if (!fxmlController.hasCertificate(category)) {
					return;
				}
				button.setStyle(buttonPressedStyle);
				isInstructor = true;
			}
		});
	}

}
