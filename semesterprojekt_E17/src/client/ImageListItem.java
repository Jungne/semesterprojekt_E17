package client;

import javafx.scene.layout.Pane;

public class ImageListItem extends PaneListItem {

	private FXMLDocumentController fxmlController;
	private byte[] imageByteArray;

	public ImageListItem(FXMLDocumentController fxmlController, String imageName, byte[] imageByteArray) {
		super(imageName);
		this.fxmlController = fxmlController;
		this.imageByteArray = imageByteArray;
		setCancelButtonAction();
	}

	/**
	 * @return the imageByteArray
	 */
	public byte[] getImageByteArray() {
		return imageByteArray;
	}

	private void setCancelButtonAction() {
		getCancelButton().setOnAction(e -> fxmlController.removeImageListItem(this));
	}

}
