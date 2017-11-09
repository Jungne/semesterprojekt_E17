package client;

import interfaces.Category;
import interfaces.Location;
import interfaces.Trip;
import interfaces.User;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
	private AnchorPane mainPane;
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
	@FXML
	private AnchorPane createTripPane1;
	@FXML
	private AnchorPane createTripPane2;
	@FXML
	private Button createTripNextButton;
	@FXML
	private Button createTripBackButton;
	@FXML
	private Button createTripCreateTripButton;

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

	private void showPane(AnchorPane pane) {
		//All panes set to invisible
		browseTripsPane.setVisible(false);
		createTripPane1.setVisible(false);
		createTripPane2.setVisible(false);

		//The given pane is set to visible
		pane.setVisible(true);
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
		showPane(browseTripsPane);

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

	@FXML
	private void handleMyTripsButton(ActionEvent event) {
		showPane(createTripPane1);
	}

	@FXML
	private void handleCreateTripNextBackButton(ActionEvent event) {
		if (event.getSource() == createTripNextButton) {
			showPane(createTripPane2);
		}
		if (event.getSource() == createTripBackButton) {
			showPane(createTripPane1);
		}
	}

	@FXML
	private void handleCreateTripButtons(ActionEvent event) throws Exception {
		if (event.getSource() == createTripCreateTripButton) {

			//Uses random category
			ArrayList<Category> categories = new ArrayList<>();
			categories.add(new Category(2, "Running"));
			//Uses random location
			Location location = new Location(2, "Fyn");
			//Uses random organizer
			User organizer = new User(5, "lalun13@student.sdu.dk", "Lasse", null);
			//Organizer instructs in running
			ArrayList<Category> organizerInstructorIn = new ArrayList<>();
			organizerInstructorIn.add(new Category(2, "Running"));
			//Uses Random tags
			HashSet<String> tags = new HashSet<>();
			tags.add("running");
			tags.add("easy");
			tags.add("10km");
			//Creates the trip
			clientController.createTrip("test Title", "This is a test trip", categories, 100, new Date(2017, 12, 24), location, "Kertemindevej 47, 5540 Ullerslev", 12, organizer, organizerInstructorIn, null, tags, null);
		}
	}
}
