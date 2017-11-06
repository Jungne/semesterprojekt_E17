package client;

import interfaces.Category;
import interfaces.Trip;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FXMLDocumentController implements Initializable {

	private ClientController clientController;

	@FXML
	private Button searchTripsButton;
	@FXML
	private ListView<HBoxCell> browseTripsListView;
	@FXML
	private Button browseTripsButton;
	@FXML
	private AnchorPane browseTripsPane;
	@FXML
	private TextField searchTripsLocationTextField;
	@FXML
	private CheckBox searchTripsNormalCheckBox;
	@FXML
	private CheckBox searchTripsInstructorCheckBox;
	@FXML
	private TextField searchTripsPriceTextField;
	@FXML
	private DatePicker searchTripsDatePicker;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			clientController = new ClientController();
		} catch (RemoteException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}

		//Testing HBoxCell
//		ArrayList<Trip> trips = new ArrayList<>();
//		byte[] image = "".getBytes();
//		ArrayList<Category> categories = new ArrayList<>();
//		categories.add(new Category(0, "testcategory"));
//		trips.add(new Trip(0, "Test trip", "This a test of the HBoxCell", categories, 420.69, image));
//		showTrips(trips, browseTripsListView);

	}

	private void showTrips(List<Trip> trips, ListView listview) {
		List<HBoxCell> list = new ArrayList<>();
		for (Trip trip : trips) {
			list.add(new HBoxCell(trip));
		}
		ObservableList observableList = FXCollections.observableArrayList(list);
		listview.setItems(observableList);
	}

	@FXML
	private void handleBrowseTripsButton(ActionEvent event) throws RemoteException {
		browseTripsPane.setVisible(true);
		
		List<Trip> trips = clientController.getAllTrips();
		
		showTrips(trips, browseTripsListView);
	}
	
	@FXML
	private void handleSearchTripsButton(ActionEvent event) {
		//Only date and price works at the moment.
		
		String searchTitle; //TODO add textfield in GUI.
		int categoryID = -1; //TODO implement categoryID
		int locationID = -1; //TODO implement locationID
		double priceMax = Double.parseDouble(searchTripsPriceTextField.getText());
		
		//Date stuff
		LocalDate localDate = searchTripsDatePicker.getValue();
		Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
		Date date = Date.from(instant);
		
		List<Trip> trips = clientController.searchTrips("", -1, date, -1, priceMax);

		showTrips(trips, browseTripsListView);
	}
}
