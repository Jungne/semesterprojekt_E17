package client;

import interfaces.Category;
import interfaces.Location;
import interfaces.OptionalPrice;
import interfaces.Trip;
import interfaces.User;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FXMLDocumentController implements Initializable {

	private ClientController clientController;

	@FXML
	private AnchorPane mainPane;

	//toolbar
	@FXML
	private Button toolBarMessagingButton;
	@FXML
	private Button toolBarBrowseTripsButton;
	@FXML
	private Button toolBarMyTripsButton;
	@FXML
	private Button toolBarProfileButton;

	//Browse
	@FXML
	private AnchorPane browseTripsPane;
	@FXML
	private Button searchTripsButton;
	@FXML
	private ListView<HBoxCell> browseTripsListView;
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

	//My Trips
	@FXML
	private AnchorPane myTripsPane;
	@FXML
	private Button myTripsCreateTripButton;
	@FXML
	private Button myTripsModifyTripButton;

	//Create Trip
	@FXML
	private AnchorPane createTripPane1;
	@FXML
	private AnchorPane createTripPane2;
	@FXML
	private Button createTripNextButton;
	@FXML
	private Button createTripBackButton;
	@FXML
	private ComboBox<Category> createTripCategoryComboBox;
	@FXML
	private ComboBox<Location> createTripLocationComboBox;
	@FXML
	private TextField createTripTitleTextField;
	@FXML
	private TextArea createTripDescriptionTextArea;
	@FXML
	private CheckBox createTripInstructorCheckBox;
	@FXML
	private TextField createTripAddressTextField;
	@FXML
	private TextField createTripPriceTextField;
	@FXML
	private DatePicker createTripTimeStartDatePicker;
	@FXML
	private TextField createTripParticipantLimitTextField;
	@FXML
	private TextField createTripTagsTextField;

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
		myTripsPane.setVisible(false);
		createTripPane1.setVisible(false);
		createTripPane2.setVisible(false);

		//The given pane is set to visible
		pane.setVisible(true);
	}

	@FXML
	private void handleToolBarButtons(ActionEvent event) throws RemoteException {
		if (event.getSource() == toolBarMessagingButton) {
			//TODO - Go to messaging pane
		} else if (event.getSource() == toolBarBrowseTripsButton) {
			showPane(browseTripsPane);

			List<Trip> trips = clientController.getAllTrips();
			showTrips(trips, browseTripsListView);
		} else if (event.getSource() == toolBarMyTripsButton) {
			showPane(myTripsPane);
			//TODO - here should my trips be loaded
		} else if (event.getSource() == toolBarProfileButton) {
			//TODO - Go to profile pane
		}
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
	private void handleCreateTripButton(ActionEvent event) throws Exception {
		//Gets all the values and creates the trip
		String title = createTripTitleTextField.getText();
		String description = createTripDescriptionTextArea.getText();

		ArrayList<Category> categories = new ArrayList<>();
		categories.add(createTripCategoryComboBox.getValue());

		double price = Double.parseDouble(createTripPriceTextField.getText());
		LocalDate date = createTripTimeStartDatePicker.getValue();
		LocalDateTime dateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 12, 0);
		Location location = createTripLocationComboBox.getValue();
		String meetingAddress = createTripAddressTextField.getText();
		int participantLimit = Integer.parseInt(createTripParticipantLimitTextField.getText());
		//TODO - Should use the logged in user
		User organizer = new User(5, "lalun13@student.sdu.dk", "Lasse", null);
		ArrayList<Category> organizerInstructorIn = new ArrayList<>();
		if (createTripInstructorCheckBox.isSelected()) {
			//Makes the organizer instructor in all trip categories
			for (Category category : categories) {
				organizerInstructorIn.add(category.clone());
			}
		}
		//TODO - Should add optional prices to GUI
		ArrayList<OptionalPrice> optionalPrices = new ArrayList<>();
		HashSet<String> tags = new HashSet<>();
		for (String s : createTripTagsTextField.getText().split(" ")) {
			tags.add(s);
		}
		//TODO - Should be able to add images via GUI
		ArrayList<byte[]> images = new ArrayList<>();

		clientController.createTrip(title, description, categories, price, dateTime, location, meetingAddress, participantLimit, organizer, organizerInstructorIn, optionalPrices, tags, images);

		showPane(myTripsPane);
	}

	@FXML
	private void handleMyTripsButtons(ActionEvent event) {
		if (event.getSource() == myTripsCreateTripButton) {
			showPane(createTripPane1);

			//Gets all categories from the server and displays them in the comboBox
			ObservableList<Category> categories = FXCollections.observableArrayList(clientController.getCategories());
			createTripCategoryComboBox.setItems(categories);
			createTripCategoryComboBox.setPromptText("Choose");

			//Gets all locations from the server and displays them in the comboBox
			ObservableList<Location> locations = FXCollections.observableArrayList(clientController.getLocations());
			createTripLocationComboBox.setItems(locations);
			createTripLocationComboBox.setPromptText("Choose");
		}
		if (event.getSource() == myTripsModifyTripButton) {
			//TODO - Go to ModifyTripPane
		}
	}
}
