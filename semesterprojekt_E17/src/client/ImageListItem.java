package client;

import interfaces.Image;

public class ImageListItem extends PaneListItem {

	private FXMLDocumentController fxmlController;
	private Image image;

	public ImageListItem(FXMLDocumentController fxmlController, Image image) {
		super(image.getTitle());
		this.fxmlController = fxmlController;
		this.image = image;
		setCancelButtonAction();
	}

	/**
	 * @return the imageByteArray
	 */
	public Image getImage() {
		return image;
	}

	private void setCancelButtonAction() {
		getCancelButton().setOnAction(e -> fxmlController.removeImageListItem(this));
	}

}
