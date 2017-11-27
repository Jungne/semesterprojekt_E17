package client;

import interfaces.Category;

public class CategoryListItem2 extends PaneListItem {

	private FXMLDocumentController fxmlController;
	private Category category;

	public CategoryListItem2(FXMLDocumentController fxmlController, Category category) {
		super(category.getName());
		this.fxmlController = fxmlController;
		this.category = category;
		setCancelButtonAction();
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	private void setCancelButtonAction() {
		getCancelButton().setOnAction(e -> fxmlController.removeCategoryListItem2(this));
	}

}
