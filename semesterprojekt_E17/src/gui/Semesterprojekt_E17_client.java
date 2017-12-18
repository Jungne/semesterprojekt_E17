package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the client start class.
 *
 * @author group 12
 */
public class Semesterprojekt_E17_client extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.show();
		stage.setTitle("Outdoor Activity Portal");
	}

	public static void main(String[] args) {
		launch(args);
	}

}
