package client;

import interfaces.Category;
import interfaces.Trip;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class FXMLDocumentController implements Initializable {

	private ClientController clientController;

	@FXML
	private Button searchButton;
	@FXML
	private ListView<HBoxCell> browseTripsListView;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			clientController = new ClientController();
		} catch (RemoteException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}

		//Testing HBoxCell
		ArrayList<Trip> trips = new ArrayList<>();
		byte[] image = "".getBytes();
		ArrayList<Category> categories = new ArrayList<>();
		categories.add(new Category(0, "testcategory"));
		trips.add(new Trip(0, "Test trip", "This a test of the HBoxCell", categories, 420.69, image));
		showTrips(trips, browseTripsListView);

	}

	@FXML
	private void handleButtonAction(ActionEvent event) {

		List<Trip> trips = clientController.searchTrips("", -1, null, -1, -1.0);

		for (Trip trip : trips) {
			System.out.println(trip.getTitle());
		}
	}

	private void showTrips(ArrayList<Trip> trips, ListView listview) {
		List<HBoxCell> list = new ArrayList<>();
		for (Trip trip : trips) {
			list.add(new HBoxCell(trip));
		}
		ObservableList observableList = FXCollections.observableArrayList(list);
		listview.setItems(observableList);
	}
}
