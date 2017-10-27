package client;

import interfaces.Trip;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FXMLDocumentController implements Initializable {

	private ClientController clientController;

	@FXML
	private TextField searchTripPriceTextField;
	@FXML
	private Button searchButton;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			clientController = new ClientController();
		} catch (RemoteException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void handleButtonAction(ActionEvent event) {
		double priceMax;
		if (!searchTripPriceTextField.getText().equals("")) {
			priceMax = Double.parseDouble(searchTripPriceTextField.getText());
		} else {
			priceMax = -1.0;
		}

		List<Trip> trips = clientController.searchTrips("", -1, null, -1, priceMax);

		for (Trip trip : trips) {
			System.out.println(trip.getTitle());
		}
	}
}
