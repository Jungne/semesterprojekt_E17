package client;

import java.io.IOException;
import interfaces.Category;
import interfaces.Location;
import interfaces.OptionalPrice;
import interfaces.Trip;
import interfaces.User;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;

public class FXMLDocumentController implements Initializable {

	private ClientController clientController;
	private Window stage;
	private File tripImageFile;

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
	@FXML
	private Button myTripsViewTripButton;

	//Create Trip
	@FXML
	private Text createTripInvalidCertificateText;
	@FXML
	private Text createTripInvalidPictureText;
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
	@FXML
	private ComboBox<Category> createTripCategoryComboBox;
	@FXML
	private Text createTripInvalidCategoryText;
	@FXML
	private ComboBox<Location> createTripLocationComboBox;
	@FXML
	private TextField createTripTitleTextField;
	@FXML
	private Text createTripInvalidTitleText;
	@FXML
	private TextArea createTripDescriptionTextArea;
	@FXML
	private CheckBox createTripInstructorCheckBox;
	@FXML
	private TextField createTripAddressTextField;
	@FXML
	private TextField createTripPriceTextField;
	@FXML
	private Text createTripInvalidPriceText;
	@FXML
	private DatePicker createTripTimeStartDatePicker;
	@FXML
	private Text createTripInvalidDateText;
	@FXML
	private TextField createTripParticipantLimitTextField;
	@FXML
	private TextField createTripTagsTextField;
	@FXML
	private Text createTripInvalidParticipantLimitText;
	@FXML
	private Button createTripCancelButton1;
	@FXML
	private Button createTripCancelButton2;
	@FXML
	private Text createTripInvalidLocationText;
	@FXML
	private Button createTripAddPictureButton;
	@FXML
	private HBox createTripPictureListHBox;

	//View Trip
	@FXML
	private AnchorPane viewTripPane;
	@FXML
	private Text viewTripTitleLabel;
	@FXML
	private Text viewTripDescriptionLabel;
	@FXML
	private Text viewTripPriceLabel;
	@FXML
	private Button viewTripButton;
	@FXML
	private AnchorPane logInOutPane;
	@FXML
	private TextField logInEmailTextField;
	@FXML
	private TextField logInPasswordTextField;
	@FXML
	private Hyperlink newAccountButton;
	@FXML
	private Button logInButton;
	@FXML
	private AnchorPane newAccountPane;
	@FXML
	private TextField newAccountNameTextField;
	@FXML
	private TextField newAccountEmailTextField;
	@FXML
	private PasswordField newAccountPasswordTextField;
	@FXML
	private PasswordField newAccountRepeatPasswordTextField;
	@FXML
	private ImageView newAccountImageView;
	@FXML
	private Button newAccountProfilePictureButton;
	@FXML
	private Button newAccountCreateButton;
	@FXML
	private Button toolbarLogInLogOutButton;
	@FXML
	private Button newAccountBackButton;
	@FXML
	private ToolBar toolBar;
	@FXML
	private AnchorPane profilePane;
	@FXML
	private ImageView profilePictureImageView;
	@FXML
	private Label profileNameLabel;
	@FXML
	private Label profileEmailLabel;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			clientController = new ClientController();

			newAccountImageView.setImage(new Image("default_profile_picture.png"));
		} catch (RemoteException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
		stage = null;
		tripImageFile = null;
	}

	private void showPane(AnchorPane pane) {
		//All panes set to invisible
		for (Node node : mainPane.getChildren()) {
			node.setVisible(false);
		}

		//Sets the toolbar to visible again.
		toolBar.setVisible(true);

		//The given pane is set to visible
		pane.setVisible(true);
	}

	@FXML
	private void handleToolBarButtons(ActionEvent event) throws RemoteException {
		if (event.getSource() == toolBarMessagingButton) {
			//TODO - Go to messaging pane
		} else if (event.getSource() == toolBarBrowseTripsButton) {
			showPane(browseTripsPane);

			//List<Trip> trips = clientController.getAllTrips();
			//showTrips(trips, browseTripsListView);
		} else if (event.getSource() == toolBarMyTripsButton) {
			showPane(myTripsPane);
			//TODO - here should my trips be loaded
		} else if (event.getSource() == toolBarProfileButton) {
			showPane(profilePane);
			loadProfileInfo();
		} else if (event.getSource() == toolbarLogInLogOutButton) {
			showPane(logInOutPane);
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
		double priceMax = -1;
		if (!searchTripsPriceTextField.getText().equals("")) {
			priceMax = Double.parseDouble(searchTripsPriceTextField.getText());
		}

		//Date stuff
		LocalDate localDate = searchTripsDatePicker.getValue();
		Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
		Date date = Date.from(instant);

		List<Trip> trips = clientController.searchTrips("", -1, date, -1, priceMax);

		showTrips(trips, browseTripsListView);
	}

	@FXML
	private void handleLogInButton(ActionEvent event) {
		String username = logInEmailTextField.getText();
		String password = hashPassword(logInPasswordTextField.getText());

		try {
			clientController.signIn(username, password);
			showPane(profilePane);
			loadProfileInfo();
		} catch (RemoteException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void handleNewAccountButton(ActionEvent event) {
		showPane(newAccountPane);
	}

	@FXML
	private void handleNewAccountBackButton(ActionEvent event) {
		showPane(logInOutPane);
	}

	@FXML
	private void handleChooseProfilePictureButton(ActionEvent event) {
		File newAccountProfilePictureFile = chooseImage("Select profile picture");
		Image profilePicture = new Image(newAccountProfilePictureFile.toURI().toString());

		newAccountImageView.setImage(profilePicture);
	}

	private File chooseImage(String title) {
		createTripPictureListHBox.getChildren().add(new ImageItem(createTripPictureListHBox, "some_picture.jpg", null));
		
		stage = mainPane.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		return fileChooser.showOpenDialog(stage);
	}

	private String getFileType(File file) {
		return file.getPath().substring(file.getPath().lastIndexOf('.') + 1);
	}

	@FXML
	private void handleCreateAccountButton(ActionEvent event) {
		String name = newAccountNameTextField.getText();
		String email = newAccountEmailTextField.getText();
		String password = newAccountPasswordTextField.getText();
		String repeatPassword = newAccountRepeatPasswordTextField.getText();
		byte[] profilePicture = null;

		BufferedImage bImage = SwingFXUtils.fromFXImage(newAccountImageView.getImage(), null);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bImage, "png", baos);
			profilePicture = baos.toByteArray();
			baos.close();
		} catch (IOException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}

		if (password.equals(repeatPassword)) {
			User user = new User(-1, email, name, profilePicture);
			try {
				clientController.signUp(user, hashPassword(password));
				showPane(profilePane);
				loadProfileInfo();
			} catch (RemoteException ex) {
				Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			newAccountRepeatPasswordTextField.setStyle("-fx-text-box-border: red");
		}
	}

	/**
	 * hashes the password using the SHA-256 algorithm
	 *
	 * @param password the password to be hashed
	 * @return the hash of the password
	 */
	private String hashPassword(String password) {
		byte[] hashBytes = null;
		// shitty attempt at salting the password :)
		password += password.substring(0, 4);
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			hashBytes = md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return String.format("%064x", new java.math.BigInteger(1, hashBytes)).toLowerCase();
	}

	@FXML
	private void handleCreateTripButtons(ActionEvent event) {
		if (event.getSource() == createTripAddPictureButton) {
			tripImageFile = chooseImage("Select trip picture");
		} else if (event.getSource() == createTripNextButton) {
			showPane(createTripPane2);
		} else if (event.getSource() == createTripBackButton) {
			showPane(createTripPane1);
		} else if (event.getSource() == createTripCancelButton1 || event.getSource() == createTripCancelButton2) {
			showPane(myTripsPane);
		} else if (event.getSource() == createTripCreateTripButton) {
			try {
				int tripId = createTrip();
				if (0 < tripId) {
					showPane(myTripsPane); //TODO - Should be changes to a pane showing the newly created trip instead
				}
			} catch (Exception ex) {
				Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private int createTrip() throws Exception {
		//Gets all the values
		String title = createTripTitleTextField.getText();
		String description = createTripDescriptionTextArea.getText();
		Category category = createTripCategoryComboBox.getValue();
		ArrayList<Category> categories = new ArrayList<>();
		categories.add(category);
		String priceString = createTripPriceTextField.getText();
		if (priceString == null || priceString.isEmpty()) {
			priceString = "0";
		}
		LocalDate date = createTripTimeStartDatePicker.getValue();
		Location location = createTripLocationComboBox.getValue();
		String meetingAddress = createTripAddressTextField.getText();
		String participantLimitString = createTripParticipantLimitTextField.getText();
		if (participantLimitString == null || participantLimitString.isEmpty()) {
			participantLimitString = "0";
		}
		//User organizer = clientController.getCurrentUser();
		//Temp: creates user and promotes as running instructor
		User organizer = new User(5, null, null, null);
		organizer.promoteToInstructor(new Category(2, "Running"));

		ArrayList<Category> organizerInstructorIn = new ArrayList<>();
		if (createTripInstructorCheckBox.isSelected()) {
			//Temp: Makes the organizer instructor in all trip categories
			for (Category c : categories) {
				if (c != null) {
					organizerInstructorIn.add(c.clone());
				}
			}
		}
		//TODO - Should add optional prices to GUI
		ArrayList<OptionalPrice> optionalPrices = new ArrayList<>();
		HashSet<String> tags = new HashSet<>();
		String tagsString = createTripTagsTextField.getText();
		if (tagsString != null && !tagsString.isEmpty()) {
			for (String tag : tagsString.split(" ")) {
				tags.add(tag);
			}
		}

		//Checks if parameters are valid
		if (!isTripParametersValid(title, category, priceString, date, location, participantLimitString, organizer.getCertificates(), tripImageFile)) {
			return -1;
		}

		//Converts price, date, participant limit and image now that validation is checked
		double price = Double.parseDouble(priceString);
		LocalDateTime dateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 12, 0);
		int participantLimit = Integer.parseInt(participantLimitString);
		//TODO - Should be able to add several images
		//TODO - tripAddress should not be null or empty
		ArrayList<byte[]> images = new ArrayList<>();
		if (tripImageFile != null) {
			BufferedImage image = ImageIO.read(tripImageFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, getFileType(tripImageFile), baos);
			images.add(baos.toByteArray());
		}
		//Creates trip
		return clientController.createTrip(title, description, categories, price, dateTime, location, meetingAddress, participantLimit, organizer, organizerInstructorIn, optionalPrices, tags, images);
	}

	private boolean isTripParametersValid(String title, Category category, String priceString, LocalDate date, Location location, String participantLimitString, List<Category> organizerCertificates, File imageFile) {
		boolean isTripParametersValid = true;
		//Title check
		if (title == null || title.isEmpty()) {
			createTripInvalidTitleText.setVisible(true);
			isTripParametersValid = false;
		} else {
			createTripInvalidTitleText.setVisible(false);
		}
		//Category check
		if (category == null) {
			createTripInvalidCategoryText.setVisible(true);
			isTripParametersValid = false;
		} else {
			createTripInvalidCategoryText.setVisible(false);
		}
		//Price check
		double price;
		try {
			price = Double.parseDouble(priceString);
		} catch (NumberFormatException e) {
			price = -1;
		}
		if (price < 0) {
			createTripInvalidPriceText.setVisible(true);
			isTripParametersValid = false;
		} else {
			createTripInvalidPriceText.setVisible(false);
		}
		//timeStart check
		if (date == null || date.isBefore(LocalDate.now())) {
			createTripInvalidDateText.setVisible(true);
			isTripParametersValid = false;
		} else {
			createTripInvalidDateText.setVisible(false);
		}
		//Location check
		if (location == null) {
			createTripInvalidLocationText.setVisible(true);
			isTripParametersValid = false;
		} else {
			createTripInvalidLocationText.setVisible(false);
		}
		//ParticipantLimit check
		int participantLimit;
		try {
			participantLimit = Integer.parseInt(participantLimitString);
		} catch (NumberFormatException e) {
			participantLimit = -1;
		}
		if (participantLimit < 0) {
			createTripInvalidParticipantLimitText.setVisible(true);
			isTripParametersValid = false;
		} else {
			createTripInvalidParticipantLimitText.setVisible(false);
		}
		//Picture check
		if (tripImageFile != null) {
			createTripInvalidPictureText.setVisible(false);
			try {
				BufferedImage image = ImageIO.read(tripImageFile);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, getFileType(tripImageFile), baos);
			} catch (Exception ex) {
				createTripInvalidPictureText.setVisible(true);
				isTripParametersValid = false;
				tripImageFile = null;
			}
		}
		//Organizer certificates check
		createTripInvalidCertificateText.setVisible(false);
		System.out.println(category);
		System.out.println(organizerCertificates);
		if (createTripInstructorCheckBox.isSelected() && !organizerCertificates.contains(category)) {
			createTripInvalidCertificateText.setVisible(true);
			isTripParametersValid = false;
		}

		return isTripParametersValid;
	}

	private void setUpCreateTripPane() {
		//Gets all categories from the server and displays them in the comboBox
		ObservableList<Category> categories = FXCollections.observableArrayList(clientController.getCategories());
		createTripCategoryComboBox.setItems(categories);

		//Gets all locations from the server and displays them in the comboBox
		ObservableList<Location> locations = FXCollections.observableArrayList(clientController.getLocations());
		createTripLocationComboBox.setItems(locations);

		//Reset parameters
		createTripTitleTextField.setText(null);
		tripImageFile = null;
		createTripDescriptionTextArea.setText(null);
		createTripCategoryComboBox.setValue(null);
		createTripInstructorCheckBox.setSelected(false);
		createTripAddressTextField.setText(null);
		createTripPriceTextField.setText(null);
		createTripLocationComboBox.setValue(null);
		createTripTimeStartDatePicker.setValue(null);
		createTripParticipantLimitTextField.setText(null);
		createTripTagsTextField.setText(null);

		//Reset warning texts
		createTripInvalidTitleText.setVisible(false);
		createTripInvalidPictureText.setVisible(false);
		createTripInvalidCategoryText.setVisible(false);
		createTripInvalidCertificateText.setVisible(false);
		createTripInvalidPriceText.setVisible(false);
		createTripInvalidLocationText.setVisible(false);
		createTripInvalidDateText.setVisible(false);
		createTripInvalidParticipantLimitText.setVisible(false);
	}

	@FXML
	private void handleMyTripsButtons(ActionEvent event) {
		if (event.getSource() == myTripsCreateTripButton) {
			setUpCreateTripPane();
			showPane(createTripPane1);
		}
		if (event.getSource() == myTripsModifyTripButton) {
			//TODO - Go to ModifyTripPane
		}
		if (event.getSource() == myTripsViewTripButton) {
			viewTrip(0);
		}
	}

	@FXML
	private void handleViewTripButton(ActionEvent event) {
		viewTrip(0);
	}

	private void viewTrip(int id) {
		Trip trip = clientController.viewTrip(1); //Should be id obtained from selected element in list view on my trips
		if (trip != null) {
			viewTripTitleLabel.setText("Trip #" + trip.getId() + " - " + trip.getTitle());
			viewTripDescriptionLabel.setText(trip.getDescription());
			viewTripPriceLabel.setText("Price: " + trip.getPrice());
			showPane(viewTripPane);
		}
	}

	private void loadProfileInfo() {
		profileNameLabel.setText(clientController.getCurrentUser().getName());
		profileEmailLabel.setText(clientController.getCurrentUser().getEmail());
		Image image = new Image(new ByteArrayInputStream(clientController.getCurrentUser().getImage()));
		profilePictureImageView.setImage(image);
	}
}
