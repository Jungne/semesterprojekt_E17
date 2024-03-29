package gui;

import client.ClientController;
import client.ClientMessagingHandler;
import shared.Category;
import shared.Conversation;
import shared.FullTripException;
import shared.Image;
import shared.Location;
import shared.Message;
import shared.OptionalPrice;
import shared.Trip;
import shared.User;
import shared.InstructorListItem;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import shared.IllegalEmailException;

public class FXMLDocumentController implements Initializable {

	private ClientController clientController;
	private Window stage;

	// <editor-fold defaultstate="collapsed" desc="Toolbar - Elements">
	@FXML
	private AnchorPane mainPane;
	@FXML
	private ToolBar toolBar;
	@FXML
	private ImageView toolBarImageView;
	@FXML
	private Button toolBarMessagingButton;
	@FXML
	private Button toolBarBrowseTripsButton;
	@FXML
	private Button toolBarMyTripsButton;
	@FXML
	private Button toolBarProfileButton;
	@FXML
	private Button toolbarLogInLogOutButton;
	@FXML
	private Button toolBarBrowseUsersButton;
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Browse Trips - Elements">
	@FXML
	private AnchorPane browseTripsPane;
	@FXML
	private Button searchTripsButton;
	@FXML
	private ListView<HBoxCell> browseTripsListView;
	@FXML
	private CheckBox searchTripsNormalCheckBox;
	@FXML
	private CheckBox searchTripsInstructorCheckBox;
	@FXML
	private TextField searchTripsPriceTextField;
	@FXML
	private DatePicker searchTripsDatePicker;
	@FXML
	private TextField searchTripsTitleTextField;
	@FXML
	private ComboBox<Location> searchTripsLocationComboBox;
	@FXML
	private ComboBox<Category> searchTripsCategoryComboBox;
	@FXML
	private HBox searchTripCategoryListHBox;
	@FXML
	private Text searchTripInvalidPriceText;
	@FXML
	private Button searchTripsViewTripButton;

	private boolean searchTripsCategoryComboBoxIsDisabled = false;
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="My Trips - Elements">
	@FXML
	private AnchorPane myTripsPane;
	@FXML
	private Button myTripsCreateTripButton;
	@FXML
	private Button myTripsModifyTripButton;
	@FXML
	private Button myTripsViewTripButton;
	@FXML
	private ListView<HBoxCell> myTripsListView;
	@FXML
	private Button myTripsDeleteTripButton;
	@FXML
	private ToggleGroup myTripsToggleGroup;
	@FXML
	private RadioButton myTripsToggleAll;
	@FXML
	private RadioButton myTripsOrganizedRadioButton;
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Create Trip - Elements">
	// <editor-fold defaultstate="collapsed" desc="Pane 1 - Elements">
	@FXML
	private AnchorPane createTripPane1;
	@FXML
	private TextField createTripTitleTextField;
	@FXML
	private Text createTripInvalidTitleText;
	@FXML
	private Button createTripAddPictureButton;
	@FXML
	private HBox createTripPictureListHBox;
	@FXML
	private Text createTripInvalidPictureText;
	@FXML
	private TextArea createTripDescriptionTextArea;
	@FXML
	private Text createTripDescriptionLengthLabel;
	@FXML
	private ComboBox<Category> createTripCategoryComboBox;
	@FXML
	private HBox createTripCategoryListHBox;
	@FXML
	private Text createTripInvalidCategoryText;
	@FXML
	private Text createTripIntructorText;
	@FXML
	private Button createTripCancelButton1;
	@FXML
	private Button createTripNextButton;

	private boolean createTripCategoryComboboxIsDisabled = false;
	private int currentIntructorTextOccupiers = 0;
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Pane 2 - Elements">
	@FXML
	private AnchorPane createTripPane2;
	@FXML
	private TextField createTripAddressTextField;
	@FXML
	private Text createTripInvalidMeetingAddressText;
	@FXML
	private TextField createTripPriceTextField;
	@FXML
	private Text createTripInvalidPriceText;
	@FXML
	private ComboBox<Location> createTripLocationComboBox;
	@FXML
	private Text createTripInvalidLocationText;
	@FXML
	private TextField createTripParticipantLimitTextField;
	@FXML
	private Text createTripInvalidParticipantLimitText;
	@FXML
	private DatePicker createTripTimeStartDatePicker;
	@FXML
	private Spinner<Integer> createTripHourSpinner;
	@FXML
	private Spinner<Integer> createTripMinuteSpinner;
	@FXML
	private Text createTripInvalidDateText;
	@FXML
	private TextField createTripTagsTextField;
	@FXML
	private Button createTripCreateTripButton;
	@FXML
	private Button createTripCancelButton2;
	@FXML
	private Button createTripBackButton;
	// </editor-fold>
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="View and Modify Trip - Elements">
	private Trip viewedTrip;
	private AnchorPane lastPane;

	@FXML
	private AnchorPane viewTripPane;
	@FXML
	private Text viewTripTitleLabel;
	@FXML
	private ListView<HBoxCell> viewListOfParticipants;
	@FXML
	private Button joinTripButton;
	@FXML
	private Button viewTripLastButton;
	@FXML
	private Button viewTripNextButton;
	@FXML
	private Text viewTripParticipantsLabel;
	@FXML
	private ImageView viewTripPaneImageView;
	@FXML
	private TextArea viewTripDescriptionTextArea;
	@FXML
	private Label viewTripDescriptionLengthLabel;
	@FXML
	private TextField viewTripPriceTextField;
	@FXML
	private TextField viewTripAddressTextField;
	@FXML
	private Text viewTripOrganizerLabel;
	@FXML
	private TextField viewTripDateTextField;
	@FXML
	private TextField viewTripLocationTextField;
	@FXML
	private Label viewTripLimitLabel;
	@FXML
	private TextArea viewTripCategoriesTextArea;
	@FXML
	private Button viewTripKickButton;
	@FXML
	private Button viewTripBackButton;
	@FXML
	private Button viewTripSaveChangesButton;
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Modify Trip - Elements">
	@FXML
	private AnchorPane modifyTripPane;
	@FXML
	private Text modifyTripIDLabel;
	@FXML
	private TextField modifyTripTitleTextField;
	@FXML
	private TextField modifyTripDescriptionTextField;
	@FXML
	private TextField modifyTripPriceTextField;
	@FXML
	private Button modifyTripSaveChangesButton;
	@FXML
	private Button modifyTripCancelButton;
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="LogInOut - Elements">
	@FXML
	private AnchorPane logInPane;
	@FXML
	private TextField logInEmailTextField;
	@FXML
	private Text logInInvalidEmailText;
	@FXML
	private TextField logInPasswordTextField;
	@FXML
	private Text logInInvalidPasswordText;
	@FXML
	private Button logInButton;
	@FXML
	private Hyperlink newAccountButton;
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="New Account - Elements">
	@FXML
	private AnchorPane newAccountPane;
	@FXML
	private TextField newAccountNameTextField;
	@FXML
	private Text newAccountInvalidNameText;
	@FXML
	private TextField newAccountEmailTextField;
	@FXML
	private Text newAccountInvalidEmailText;
	@FXML
	private PasswordField newAccountPasswordTextField;
	@FXML
	private Text newAccountInvalidPasswordText;
	@FXML
	private PasswordField newAccountRepeatPasswordTextField;
	@FXML
	private Text newAccountInvalidRepeatPasswordText;
	@FXML
	private ImageView newAccountImageView;
	@FXML
	private Button newAccountProfilePictureButton;
	@FXML
	private Button newAccountCreateButton;
	@FXML
	private Button newAccountBackButton;

	private Image newAccountProfilePicture;
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Profile - Elements">
	@FXML
	private AnchorPane profilePane;
	@FXML
	private ImageView profilePictureImageView;
	@FXML
	private Label profileNameLabel;
	@FXML
	private Label profileEmailLabel;
	@FXML
	private Button profileChangeProfileButton;
	@FXML
	private ListView<HBoxCell> profileCertificatesListView;
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Browse users - Elements">
	@FXML
	private AnchorPane browseUsersPane;
	@FXML
	private Button browseUsersSearchButton;
	@FXML
	private TextField browseUsersTextField;
	@FXML
	private ListView<HBoxCell> browseUsersListView;
	@FXML
	private Button browseUsersMessageButton;
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Messaging - Elements">
	private ObservableList messagingConversationList;

	@FXML
	private AnchorPane messagingPane;
	@FXML
	private ListView<HBoxCell> messagingConversationsListView;
	@FXML
	private ListView<?> messagingActiveConversationListView;
	@FXML
	private TextField messagingTextField;
	@FXML
	private Button messagingSendButton;
	// </editor-fold>

	int CurrentImageIndex = 0;
	@FXML
	private Label viewTripInstructorsLabel;
	@FXML
	private ListView<HBoxCell> viewTripInstructorsListView;
	@FXML
	private Label viewTripInstructedTripLabel;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			clientController = new ClientController();
			stage = null;

			messagingConversationsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBoxCell>() {

				@Override
				public void changed(ObservableValue<? extends HBoxCell> observable, HBoxCell newValue, HBoxCell oldValue) {
					int id = messagingConversationsListView.getSelectionModel().getSelectedItem().getConversationId();
					String type = messagingConversationsListView.getSelectionModel().getSelectedItem().getType();
					try {
						clientController.setCurrentConversation(id);
						showConversation(getConversation(new Conversation(id, type)));
					} catch (RemoteException ex) {
						Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			});

			//Set toolbar picture
			toolBarImageView.setImage(new javafx.scene.image.Image("client/default_pictures/ToolbarImage.jpg"));

			//Load all trips to browse trips, when the program starts
			resetBrowseTripPane();

		} catch (RemoteException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
		viewTripDescriptionTextArea.setTextFormatter(new TextFormatter(rejectChange));
		createTripDescriptionTextArea.setTextFormatter(new TextFormatter(rejectChange));
	}

	// <editor-fold defaultstate="collapsed" desc="Common - Methods">
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

	private void showTrips(List<Trip> trips, ListView listView) {
		List<HBoxCell> list = new ArrayList<>();

		trips.parallelStream().forEach((Trip trip) -> {
			synchronized (list) {
				list.add(new HBoxCell(trip));
			}
		});
		//Sorts the list
		list.sort(new Comparator<HBoxCell>() {
			public int compare(HBoxCell h1, HBoxCell h2) {
				return h1.getTripId() - h2.getTripId();
			}
		});

		ObservableList observableList = FXCollections.observableArrayList(list);
		listView.setItems(observableList);
	}

	private void viewTrip(int tripId, boolean modifyMode, AnchorPane lastPane) {
		viewedTrip = clientController.viewTrip(tripId);
		this.lastPane = lastPane;
		if (viewedTrip != null) {
			viewTripTitleLabel.setText(viewedTrip.getTitle());
			viewTripOrganizerLabel.setText("Organizer: " + viewedTrip.getOrganizer().getName());
			viewTripDescriptionTextArea.setText(viewedTrip.getDescription());
			viewTripDescriptionLengthLabel.setText(viewTripDescriptionTextArea.getText().length() + "/255");
			viewTripPriceTextField.setText("" + viewedTrip.getPrice());

			//viewListOfParticipants.getItems().addAll(viewedTrip.getParticipants());
			viewListOfParticipants.getItems().clear();
			for (User user : viewedTrip.getParticipants()) {
				viewListOfParticipants.getItems().add(new HBoxCell(user.getId(), user.getName()));
			}

			viewTripAddressTextField.setText(viewedTrip.getMeetingAddress());
			viewTripDateTextField.setText(viewedTrip.getTimeStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
			viewTripLocationTextField.setText(viewedTrip.getLocation().getName());
			viewTripCategoriesTextArea.setText(viewedTrip.getCategories() == null || viewedTrip.getCategories().isEmpty() ? "" : viewedTrip.getCategories().toString());

			viewTripInstructorsListView.getItems().clear();
			if (viewedTrip.getInstructors().isEmpty()) {
				viewTripInstructedTripLabel.setVisible(false);
				viewTripInstructorsLabel.setVisible(false);
				viewTripInstructorsListView.setVisible(false);
			} else {
				viewTripInstructedTripLabel.setVisible(true);
				viewTripInstructorsLabel.setVisible(true);
				viewTripInstructorsListView.setVisible(true);

				for (InstructorListItem instructor : viewedTrip.getInstructors()) {
					viewTripInstructorsListView.getItems().add(new HBoxCell(0, instructor.getUser().getName() + ": " + instructor.getCategory().getName()));
				}
			}

			viewTripLimitLabel.setText(viewedTrip.getParticipants().size() + "/" + viewedTrip.getParticipantLimit());
			if (viewedTrip.getImages().size() <= 0) {
				viewTripNextButton.setVisible(true);
				viewTripLastButton.setVisible(true);
				viewTripPaneImageView.setImage(viewedTrip.getImages().isEmpty() ? new javafx.scene.image.Image("client/default_pictures/trip_picture.png") : new javafx.scene.image.Image(new ByteArrayInputStream(viewedTrip.getImages().get(0).getImageFile())));
			} else if (viewedTrip.getImages().size() > 0) {
				viewTripPaneImageView.setImage(viewedTrip.getImages().isEmpty() ? new javafx.scene.image.Image("client/default_pictures/trip_picture.png") : new javafx.scene.image.Image(new ByteArrayInputStream(viewedTrip.getImages().get(0).getImageFile())));
				viewTripNextButton.setVisible(true);
				viewTripLastButton.setVisible(true);
				viewTripNextButton.setDisable(true);

			}
			viewTripDescriptionTextArea.setEditable(modifyMode);
			viewTripPriceTextField.setEditable(modifyMode);
			viewTripAddressTextField.setEditable(modifyMode);
			viewTripDateTextField.setEditable(modifyMode);
			//viewTripLocationTextField.setEditable(modifyMode);

			joinTripButton.setVisible(!modifyMode);
			viewTripSaveChangesButton.setVisible(modifyMode);

			joinTripButton.setDisable(
							viewedTrip.getParticipants().contains(clientController.getCurrentUser())
							|| viewedTrip.getParticipants().size() >= viewedTrip.getParticipantLimit()
							|| modifyMode
							|| clientController.getCurrentUser() == null);

			viewTripKickButton.setVisible(modifyMode);
			viewTripKickButton.setDisable(clientController.getCurrentUser() != null && viewedTrip.getOrganizer().getId() == clientController.getCurrentUser().getId() && modifyMode);

			showPane(viewTripPane);
		}
	}

	/**
	 * This method handels choosing an image and is in use when creating a trip,
	 * creating a user and when updating the profile picture
	 *
	 * @param title
	 */
	private File chooseImage(String title) {
		stage = mainPane.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		return fileChooser.showOpenDialog(stage);
	}

	private void enableToolbar(boolean isSignIn) {
		if (isSignIn) {
			toolBarMyTripsButton.setDisable(false);
			toolBarProfileButton.setDisable(false);
			toolBarMessagingButton.setDisable(false);
			toolbarLogInLogOutButton.setText("Log out");
		} else {
			toolBarMyTripsButton.setDisable(true);
			toolBarProfileButton.setDisable(true);
			toolBarMessagingButton.setDisable(true);
			toolbarLogInLogOutButton.setText("Log in");
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Profile - Methods">
	/**
	 * This method loads the user profile
	 *
	 */
	private void loadProfileInfo() {
		profileNameLabel.setText(clientController.getCurrentUser().getName());
		profileEmailLabel.setText(clientController.getCurrentUser().getEmail());

		//Loads the current users image unless there isn't any. Else it loads the default profile picture
		if (clientController.getCurrentUser().getImage() == null || clientController.getCurrentUser().getImage().getImageFile() == null) {
			profilePictureImageView.setImage(new javafx.scene.image.Image("client/default_pictures/profile_picture.png"));
		} else {
			InputStream inputStream = new ByteArrayInputStream(clientController.getCurrentUser().getImage().getImageFile());
			profilePictureImageView.setImage(new javafx.scene.image.Image(inputStream));
		}

		//Loads the current users certificates
		List<HBoxCell> list = new ArrayList<>();
		for (Category category : clientController.getCurrentUser().getCertificates()) {
			list.add(new HBoxCell(category.getId(), category.getName()));
		}
		ObservableList observableList = FXCollections.observableArrayList(list);
		profileCertificatesListView.setItems(observableList);
	}

	/**
	 * This method handels all the buttons on the profile Pane
	 *
	 */
	@FXML
	private void handleProfileButtons(ActionEvent event) {
		if (event.getSource() == profileChangeProfileButton) {
			changeProfilePicture();
		}
	}

	/**
	 * This method handels changing the user profile picture
	 *
	 */
	private void changeProfilePicture() {
		File profilePictureFile = chooseImage("Select profile picture");

		Platform.runLater(() -> {
			try {
				String imageFileName = profilePictureFile.getName();
				String imageFileType = imageFileName.substring(imageFileName.lastIndexOf('.') + 1);
				//Converts file to byte array
				BufferedImage image = ImageIO.read(profilePictureFile);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, imageFileType, baos);

				Image profilePicture = new Image(imageFileName, baos.toByteArray());
				clientController.changeProfilePicture(profilePicture);
				clientController.updateUser();
				loadProfileInfo();

			} catch (Exception ex) {
				//Failed to choose valid image
			}
		});
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="View Trip - Methods">
	@FXML
	private void handleViewTripButtons(ActionEvent event) {
		if (event.getSource() == joinTripButton) {
			try {
				clientController.participateInTrip(viewedTrip.getId());
				viewTrip(viewedTrip.getId(), false, lastPane);
			} catch (FullTripException ex) {
				//TODO popup explanation, that trip is full.
				System.out.println(ex);
			}
		} else if (event.getSource() == viewTripKickButton) {
			int userId = viewListOfParticipants.getSelectionModel().getSelectedItem().getUserId();
			clientController.kickParticipant(viewedTrip, userId);
			viewTrip(viewedTrip.getId(), false, lastPane);
		} else if (event.getSource() == viewTripBackButton) {
			showPane(lastPane);
		} else if (event.getSource() == viewTripSaveChangesButton) {
			handleSaveChanges();
			showPane(myTripsPane);
		} else if (event.getSource() == viewTripLastButton) {
			/*
                    *Start
			 */
			List<Image> data = viewedTrip.getImages();

			//Set Image.
			if (CurrentImageIndex != 0) {
				CurrentImageIndex--;
				viewTripPaneImageView.setImage(viewedTrip.getImages().isEmpty() ? new javafx.scene.image.Image("client/default_pictures/trip_picture.png") : new javafx.scene.image.Image(new ByteArrayInputStream(viewedTrip.getImages().get(CurrentImageIndex).getImageFile())));
			}
			//Should our buttun be disabled ?
			if (CurrentImageIndex == 0) {
				viewTripLastButton.setDisable(true);
				viewTripNextButton.setDisable(false);
			}
			/*
                    *end*/
		} else if (event.getSource() == viewTripNextButton) {
			/*
                    *Start
			 */
			List<Image> data = viewedTrip.getImages();
			int test = data.size() - 1;
			//Set Image.
			if (CurrentImageIndex <= test) {
				CurrentImageIndex++;
				viewTripPaneImageView.setImage(viewedTrip.getImages().isEmpty() ? new javafx.scene.image.Image("client/default_pictures/trip_picture.png") : new javafx.scene.image.Image(new ByteArrayInputStream(viewedTrip.getImages().get(CurrentImageIndex).getImageFile())));
			}
			//Should our buttun be disabled ?

			if (CurrentImageIndex == test) {
				viewTripLastButton.setDisable(false);
				viewTripNextButton.setDisable(true);
			}
			/*
                    *end
			 */

		}

	}

	private void handleSaveChanges() {
		int id = viewedTrip.getId();
		String title = viewTripTitleLabel.getText();
		String description = viewTripDescriptionTextArea.getText();
		double price = Double.parseDouble(viewTripPriceTextField.getText());

		List<User> participants = new ArrayList<User>();

		for (HBoxCell cell : viewListOfParticipants.getItems()) {
			participants.add(new User(cell.getUserId(), cell.getLabel1Text()));
		}

		//Organizer - needs update
		User organizer = viewedTrip.getOrganizer();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.parse(viewTripDateTextField.getText().replace("T", " ") + ":00", formatter);

		String address = viewTripAddressTextField.getText();
		//Location - needs update
		Location location = viewedTrip.getLocation();
		//Categories - needs update
		List<Category> categories = viewedTrip.getCategories();
		//ParticipantLimit - needs update
		int participantLimit = viewedTrip.getParticipantLimit();
		//Images - needs update
		List<Image> images = viewedTrip.getImages();

		Trip trip = new Trip(id, title, description, price, date, address, location, participantLimit, organizer, categories, images, participants);

		clientController.modifyTrip(trip);
	}

	@FXML
	private void handleViewTripListView(MouseEvent event) {
		if (viewListOfParticipants.getSelectionModel().getSelectedItem() != null) {
			int userId = viewListOfParticipants.getSelectionModel().getSelectedItem().getUserId();
			int organizerId = viewedTrip.getOrganizer().getId();
			viewTripKickButton.setDisable(userId == organizerId);
		}
	}

	UnaryOperator<Change> rejectChange = change -> {
		if (change.isContentChange()) {
			updateDescriptionLengthLabels(change);
			if (change.getControlNewText().length() > 255) {
				return null;
			}
		}
		return change;
	};

	private void updateDescriptionLengthLabels(Change change) {
		int newLength = change.getControlNewText().length();
		if (newLength > 255) {
			viewTripDescriptionLengthLabel.setText(viewTripDescriptionTextArea.getText().length() + "/255");
			createTripDescriptionLengthLabel.setText(createTripDescriptionTextArea.getText().length() + "/255");
		} else {
			viewTripDescriptionLengthLabel.setText(newLength + "/255");
			createTripDescriptionLengthLabel.setText(newLength + "/255");
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Browse Trips - Methods">
	/**
	 * This method handles all the buttons on search trip pane
	 *
	 * @param event
	 */
	@FXML
	private void handleSearchTripsButtons(ActionEvent event) {
		if (event.getSource() == searchTripsButton) {
			if (isSearchTripPriceParameterValid() == false) {
				return;
			}
			searchTrips();
		} else if (event.getSource() == searchTripsCategoryComboBox) {
			if (searchTripsCategoryComboBoxIsDisabled) {
				return;
			}
			addCategoryListItem2();
		} else if (event.getSource() == searchTripsViewTripButton) {
			if (browseTripsListView.getSelectionModel().isEmpty()) {
				//If no trip is selected, then nothing happens
			} else {
				int tripId = browseTripsListView.getSelectionModel().getSelectedItem().getTripId();
				viewTrip(tripId, false, browseTripsPane);
				if (viewedTrip.getImages().size() > 1) {
					viewTripNextButton.setDisable(false);
				}

			}
		}
	}

	/**
	 * This method checks if the price for a trip is valid. The method is in use
	 * when a user is browsing trips
	 *
	 * @Return boolean isTripPriceParameterValid - True if the price is valid
	 * otherwise false    
	 */
	private boolean isSearchTripPriceParameterValid() {

		//The return value. Boolean value is true if the trip pirce is valid    
		boolean isTripPriceParameterValid = true;

		//Get price
		String priceString = searchTripsPriceTextField.getText();
		double price;

		//If the user price field is empty, then the "searchTripInvalidPriceText" 
		//label is not visable 
		if (priceString == null || priceString.equals("")) {
			price = 0;
		} else {
			try {
				price = Double.parseDouble(priceString);
			} catch (NumberFormatException e) {
				price = -1;
			}
		}

		if (price < 0) {
			searchTripInvalidPriceText.setVisible(true);
			isTripPriceParameterValid = false;
		} else {
			searchTripInvalidPriceText.setVisible(false);
		}

		return isTripPriceParameterValid;
	}

	/**
	 * This method handles searching for trips
	 *
	 */
	private void searchTrips() {
		Platform.runLater(() -> {
			try {

				String searchTitle;
				ArrayList<Category> categories = null;
				int locationID;
				double priceMax = -1;
				String tripType = "";

				//Get search trip by title
				searchTitle = searchTripsTitleTextField.getText();

				//Get location
				try {
					//Get the location ID for the selected location in the combobox
					locationID = searchTripsLocationComboBox.getValue().getId();
				} catch (Exception e) {
					locationID = -1;
				}

				//Get categories
				if (!searchTripCategoryListHBox.getChildren().isEmpty()) {

					categories = new ArrayList<>();

					for (Node node : searchTripCategoryListHBox.getChildren()) {
						CategoryListItem2 categoryListItem = (CategoryListItem2) node;
						categories.add(categoryListItem.getCategory());
					}
				}

				//Check if the user is browsing for normal trips
				if (searchTripsNormalCheckBox.isSelected() && searchTripsInstructorCheckBox.isSelected() == false) {
					tripType = "NORMAL";
				} //Check if the user is searching for trips with an instructor
				else if (searchTripsInstructorCheckBox.isSelected() && searchTripsNormalCheckBox.isSelected() == false) {
					tripType = "INSTRUCTOR";
				} //If normal and trips with an instructor are selected or if no checkbox for trip type is selected
				else if (searchTripsInstructorCheckBox.isSelected() && searchTripsNormalCheckBox.isSelected() || searchTripsInstructorCheckBox.isSelected() == false && searchTripsNormalCheckBox.isSelected() == false) {
					tripType = "";
				}

				//Get price
				if (!searchTripsPriceTextField.getText().equals("")) {
					priceMax = Double.parseDouble(searchTripsPriceTextField.getText());
				}

				//Get date
				LocalDate date = searchTripsDatePicker.getValue();

				//Get date
				List<Trip> trips = clientController.searchTrips(searchTitle, categories, date, locationID, priceMax, tripType);

				showTrips(trips, browseTripsListView);
			} catch (RemoteException ex) {
				Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
	}

	/**
	 * This method is resetting the BrowseTrip Pane. Resets all the elements and
	 * reloads all trips
	 *
	 */
	private void resetBrowseTripPane() {
		searchTripsCategoryComboBoxIsDisabled = true;

		//Reset parameters for browse trips
		searchTripsNormalCheckBox.setSelected(false);
		searchTripsInstructorCheckBox.setSelected(false);
		searchTripsPriceTextField.clear();
		searchTripsDatePicker.setValue(null);
		searchTripsTitleTextField.clear();

		//Reset price warning text
		searchTripInvalidPriceText.setVisible(false);

		//Gets all locations from the server and displays them in the comboBox
		ObservableList<Location> locations = FXCollections.observableArrayList(clientController.getLocations());
		locations.add(0, new Location(-1, "All locations"));
		searchTripsLocationComboBox.setItems(locations);

		//Gets all categories from the server and displays them in the comboBox
		ObservableList<Category> categories = FXCollections.observableArrayList(clientController.getCategories());
		searchTripsCategoryComboBox.setItems(categories);

		//Resets category HBox
		searchTripCategoryListHBox.getChildren().clear();

		searchTripsCategoryComboBox.setValue(null);
		searchTripsCategoryComboBoxIsDisabled = false;

		//Reload all trips
		searchTrips();
	}

	/**
	 * This method handles adding a category from the combobox, as a search
	 * parameter, when browsing for trips. Adds the category to the list
	 *
	 */
	private void addCategoryListItem2() {
		Category category = searchTripsCategoryComboBox.getValue();
		//Checks if category already exists in list
		for (Node node : searchTripCategoryListHBox.getChildren()) {
			if (((CategoryListItem2) node).getCategory().equals(category)) {
				return;
			}
		}

		//Adds the category to HBox
		searchTripCategoryListHBox.getChildren().add(new CategoryListItem2(this, category));
	}

	/**
	 * This method handles removing a category from beeing a search paramenter,
	 * when browsing for trips. Removes the category from the list
	 *
	 * @param categoryListItem
	 */
	protected void removeCategoryListItem2(CategoryListItem2 categoryListItem) {
		searchTripCategoryListHBox.getChildren().remove(categoryListItem);
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Log in - Methods">
	private void resetLogInPane() {
		logInEmailTextField.clear();
		logInPasswordTextField.clear();
		logInInvalidEmailText.setVisible(false);
		logInInvalidPasswordText.setVisible(false);
	}

	@FXML
	private void handleLogInButton(ActionEvent event) {
		String email = logInEmailTextField.getText();
		String password = logInPasswordTextField.getText();

		//Checks if email or password is empty
		if (!isLogInParametersValid(email, password)) {
			return;
		}

		//Signs in and checks if it succeeded
		boolean isSignedIn = false;
		try {
			isSignedIn = clientController.signIn(email, hashPassword(password));
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}

		if (isSignedIn) {
			showPane(profilePane);
			loadProfileInfo();
			enableToolbar(true);
			getUserConversations();
		} else {
			logInInvalidEmailText.setText("Email does not match with password!");
			logInInvalidEmailText.setVisible(true);
			logInInvalidPasswordText.setText("Password does not match with email!");
			logInInvalidPasswordText.setVisible(true);
		}
	}

	private boolean isLogInParametersValid(String email, String password) {
		boolean isLogInParametersValid = true;

		if (email == null || email.isEmpty()) {
			isLogInParametersValid = false;
			logInInvalidEmailText.setText("Enter email!");
			logInInvalidEmailText.setVisible(true);
		} else {
			logInInvalidEmailText.setVisible(false);
		}

		if (password == null || password.isEmpty()) {
			isLogInParametersValid = false;
			logInInvalidPasswordText.setText("Enter password!");
			logInInvalidPasswordText.setVisible(true);
		} else {
			logInInvalidPasswordText.setVisible(false);
		}

		return isLogInParametersValid;
	}

	@FXML
	private void handleNewAccountButton(ActionEvent event) {
		showPane(newAccountPane);
		resetNewAccountPane();
	}

	/**
	 * hashes the password using the SHA-256 algorithm
	 *
	 * @param password the password to be hashed
	 * @return the hash of the password
	 */
	private String hashPassword(String password) {
		if (password == null || password.length() < 4) {
			return null;
		}

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
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="New Account - Methods">
	private void resetNewAccountPane() {
		//Resets text and style
		newAccountNameTextField.clear();
		newAccountNameTextField.setStyle(null);
		newAccountEmailTextField.clear();
		newAccountEmailTextField.setStyle(null);
		newAccountPasswordTextField.clear();
		newAccountPasswordTextField.setStyle(null);
		newAccountRepeatPasswordTextField.clear();
		newAccountRepeatPasswordTextField.setStyle(null);

		//Resets warning texts
		newAccountInvalidNameText.setVisible(false);
		newAccountInvalidEmailText.setVisible(false);
		newAccountInvalidPasswordText.setVisible(false);
		newAccountInvalidRepeatPasswordText.setVisible(false);

		//Resets imageFile and imageView
		newAccountProfilePicture = null;
		newAccountImageView.setImage(new javafx.scene.image.Image("client/default_pictures/profile_picture.png"));

		newAccountRepeatPasswordTextField.setStyle(null);
	}

	@FXML
	private void handleNewAccountButtons(ActionEvent event) {
		if (event.getSource() == newAccountProfilePictureButton) {
			chooseProfilePicture();
		} else if (event.getSource() == newAccountCreateButton) {
			createAccount();
		} else if (event.getSource() == newAccountBackButton) {
			showPane(logInPane);
			resetLogInPane();
		}
	}

	private void chooseProfilePicture() {
		File imageFile = chooseImage("Select profile picture");

		Platform.runLater(() -> {
			try {
				String imageFileName = imageFile.getName();
				String imageFileType = imageFileName.substring(imageFileName.lastIndexOf('.') + 1);
				//Converts file to byte array
				BufferedImage image = ImageIO.read(imageFile);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, imageFileType, baos);

				newAccountProfilePicture = new Image(imageFileName, baos.toByteArray());
				newAccountImageView.setImage(new javafx.scene.image.Image(imageFile.toURI().toString()));
			} catch (Exception ex) {
				//Failed to choose valid image
			}
		});
	}

	private void createAccount() {
		String name = newAccountNameTextField.getText();
		String email = newAccountEmailTextField.getText();
		String password = newAccountPasswordTextField.getText();
		String repeatPassword = newAccountRepeatPasswordTextField.getText();
		Image profilePicture = newAccountProfilePicture;

		if (!isNewAccountParametersValid(name, email, password, repeatPassword)) {
			return;
		}

		try {
			clientController.signUp(email, name, profilePicture, hashPassword(password));
			showPane(profilePane);
			loadProfileInfo();
			enableToolbar(true);
			newAccountInvalidEmailText.setVisible(false);
		} catch (IllegalEmailException ex) {
			newAccountInvalidEmailText.setText("Email is already in use!");
			newAccountInvalidEmailText.setVisible(true);
		}

	}

	private boolean isNewAccountParametersValid(String name, String email, String password, String repeatPassword) {
		boolean isNewAccountParametersValid = true;

		//Checks if name is valid
		if (name == null || name.isEmpty()) {
			isNewAccountParametersValid = false;
			newAccountInvalidEmailText.setText("Enter email!");
			newAccountInvalidNameText.setVisible(true);
		} else {
			newAccountInvalidNameText.setVisible(false);
		}
		//Checks if email is valid
		if (email == null || email.isEmpty()) {
			isNewAccountParametersValid = false;
			newAccountInvalidEmailText.setVisible(true);
		} else {
			newAccountInvalidEmailText.setVisible(false);
		}
		//Checks if password is valid
		boolean isPasswordValid = true;
		if (password == null || password.isEmpty()) {
			isNewAccountParametersValid = false;
			isPasswordValid = false;
			newAccountInvalidPasswordText.setText("Enter password!");
			newAccountInvalidPasswordText.setVisible(true);
		} else if (password.length() < 6) {
			isNewAccountParametersValid = false;
			isPasswordValid = false;
			newAccountInvalidPasswordText.setText("Password must be at least 6 characters");
			newAccountInvalidPasswordText.setVisible(true);
		} else {
			newAccountInvalidPasswordText.setVisible(false);
		}
		//Checks if repeated password is valid
		if (repeatPassword == null || !repeatPassword.equals(password) && isPasswordValid) {
			isNewAccountParametersValid = false;
			newAccountInvalidRepeatPasswordText.setVisible(true);
		} else {
			newAccountInvalidRepeatPasswordText.setVisible(false);
		}

		return isNewAccountParametersValid;
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Modify Trips - Methods">
	@FXML
	private void handleModifyTripButtons(ActionEvent event) {
		if (event.getSource().equals(modifyTripSaveChangesButton)) {
			clientController.modifyTrip(new Trip(
							Integer.parseInt(modifyTripIDLabel.getText().substring(6)),
							modifyTripTitleTextField.getText(),
							modifyTripDescriptionTextField.getText(),
							Double.parseDouble(modifyTripPriceTextField.getText()),
							null
			));
		} else if (event.getSource().equals(modifyTripCancelButton)) {
			resetModifyTripPane();
			showPane(myTripsPane);
		}
	}

	private void resetModifyTripPane() {
		modifyTripTitleTextField.setText("");
		modifyTripDescriptionTextField.setText("");
		modifyTripPriceTextField.setText("");
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Toolbar - Methods">
	@FXML
	private void handleToolBarButtons(ActionEvent event) throws RemoteException {
		if (event.getSource() == toolBarMessagingButton) {
			showPane(messagingPane);
			getUserConversations();
		} else if (event.getSource() == toolBarBrowseTripsButton) {
			showPane(browseTripsPane);
			resetBrowseTripPane();
		} else if (event.getSource() == toolBarMyTripsButton) {
			showPane(myTripsPane);
			resetMyTripsPane();
		} else if (event.getSource() == toolBarProfileButton) {
			showPane(profilePane);
			loadProfileInfo();
		} else if (event.getSource() == toolbarLogInLogOutButton) {
			showPane(logInPane);
			resetLogInPane();
			if (clientController.getCurrentUser() != null) {
				clientController.signOut();
				enableToolbar(false);
			}
		} else if (event.getSource() == toolBarBrowseUsersButton) {
			showPane(browseUsersPane);
			resetBrowseUsersPane();
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Create Trip - Methods">
	private void resetCreateTripPane() {
		createTripCategoryComboboxIsDisabled = true;

		//Gets all categories from the server and displays them in the comboBox
		ObservableList<Category> categories = FXCollections.observableArrayList(clientController.getCategories());
		createTripCategoryComboBox.setItems(categories);

		//Gets all locations from the server and displays them in the comboBox
		ObservableList<Location> locations = FXCollections.observableArrayList(clientController.getLocations());
		createTripLocationComboBox.setItems(locations);

		//Sets the ValueFactory for hour and minute Spinner
		createTripHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
		createTripMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));

		//Reset parameters
		createTripTitleTextField.setText(null);
		createTripDescriptionTextArea.setText(null);
		createTripDescriptionLengthLabel.setText("0/255");
		createTripCategoryComboBox.setValue(null);
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
		createTripIntructorText.setVisible(false);
		createTripInvalidPriceText.setVisible(false);
		createTripInvalidLocationText.setVisible(false);
		createTripInvalidDateText.setVisible(false);
		createTripInvalidParticipantLimitText.setVisible(false);

		//Reset HBox lists
		createTripPictureListHBox.getChildren().clear();
		createTripCategoryListHBox.getChildren().clear();

		createTripCategoryComboboxIsDisabled = false;
	}

	@FXML
	private void handleCreateTripButtons(ActionEvent event) {
		if (event.getSource() == createTripAddPictureButton) {
			addImageListItem();
		} else if (event.getSource() == createTripCategoryComboBox) {
			if (createTripCategoryComboboxIsDisabled) {
				return;
			}
			addCategoryListItem();
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
					viewTrip(tripId, true, myTripsPane);
				}
			} catch (Exception ex) {
				Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private void addImageListItem() {
		//Chooses file with FileChooser
		File imageFile = chooseImage("Select trip picture");

		new Thread(() -> {
			createTripInvalidPictureText.setVisible(false);
			try {
				String imageFileName = imageFile.getName();
				String imageFileType = imageFileName.substring(imageFileName.lastIndexOf('.') + 1);
				//Converts file to byte array
				BufferedImage image = ImageIO.read(imageFile);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(image, imageFileType, baos);
				//Inserts values in imageItem
				ImageListItem imageListItem = new ImageListItem(this, new Image(imageFileName, baos.toByteArray()));

				Platform.runLater(() -> {
					createTripPictureListHBox.getChildren().add(imageListItem);
				});

			} catch (Exception ex) {
				createTripInvalidPictureText.setVisible(true);
			}
		}).start();
	}

	protected void removeImageListItem(ImageListItem imageListItem) {
		createTripPictureListHBox.getChildren().remove(imageListItem);
	}

	private void addCategoryListItem() {
		Category category = createTripCategoryComboBox.getValue();
		//Checks if category already exists in list
		for (Node node : createTripCategoryListHBox.getChildren()) {
			if (((CategoryListItem) node).getCategory().equals(category)) {
				return;
			}
		}

		//Adds the category to HBox and reveal the instructor text
		createTripIntructorText.setVisible(true);
		createTripCategoryListHBox.getChildren().add(new CategoryListItem(this, category));
	}

	protected boolean hasCertificate(Category category) {
		if (clientController.getCurrentUser().getCertificates().contains(category)) {
			return true;
		}
		showMessageFiveSeconds(category);
		return false;
	}

	private void showMessageFiveSeconds(Category category) {
		currentIntructorTextOccupiers++;
		//Show warning message
		createTripIntructorText.setText("You do not have cerficate for '" + category.getName() + "'-instructor!");
		createTripIntructorText.setFill(Paint.valueOf("#da0303"));
		createTripIntructorText.setOpacity(1);

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				//Checks if instructor text is used elsewhere
				currentIntructorTextOccupiers--;
				if (currentIntructorTextOccupiers != 0) {
					return;
				}
				//Show default message
				createTripIntructorText.setText("Click on categories to participate as instructor");
				createTripIntructorText.setFill(Paint.valueOf("black"));
				createTripIntructorText.setOpacity(0.5);
			}
		};
		new java.util.Timer().schedule(timerTask, 5000);
	}

	protected void removeCategoryListItem(CategoryListItem categoryListItem) {
		createTripCategoryListHBox.getChildren().remove(categoryListItem);
		if (createTripCategoryListHBox.getChildren().isEmpty()) {
			createTripIntructorText.setVisible(false);
		}
	}

	/**
	 * This method handles creating a new trip
	 *
	 */
	private int createTrip() throws Exception {
		//Gets all the values
		String title = createTripTitleTextField.getText();
		String description = createTripDescriptionTextArea.getText();

		//Gets categories and categories that the organizer will instruct in
		ArrayList<Category> categories = new ArrayList<>();
		ArrayList<Category> organizerInstructorIn = new ArrayList<>();
		for (Node node : createTripCategoryListHBox.getChildren()) {
			CategoryListItem categoryListItem = (CategoryListItem) node;
			categories.add(categoryListItem.getCategory());
			if (categoryListItem.isInstructor()) {
				organizerInstructorIn.add(categoryListItem.getCategory());
			}
		}
		//Gets trip price
		String priceString = createTripPriceTextField.getText();
		if (priceString == null || priceString.isEmpty()) {
			priceString = "0";
		}
		//Gets date and time
		LocalTime time = LocalTime.of(createTripHourSpinner.getValue(), createTripMinuteSpinner.getValue());
		LocalDate date = createTripTimeStartDatePicker.getValue();
		//Gets location, meeting address and participant limit
		Location location = createTripLocationComboBox.getValue();
		String meetingAddress = createTripAddressTextField.getText();
		String participantLimitString = createTripParticipantLimitTextField.getText();
		if (participantLimitString == null || participantLimitString.isEmpty()) {
			participantLimitString = "0";
		}
		//Get the organizer
		User organizer = clientController.getCurrentUser();
		//Gets optional prices
		//TODO - Should add optional prices to GUI
		ArrayList<OptionalPrice> optionalPrices = new ArrayList<>();
		//Gets tags
		HashSet<String> tags = new HashSet<>();
		String tagsString = createTripTagsTextField.getText();
		if (tagsString != null && !tagsString.isEmpty()) {
			for (String tag : tagsString.split(" ")) {
				tags.add(tag);
			}
		}
		//Gets trip images
		List<Image> images = new ArrayList<>();
		for (Node node : createTripPictureListHBox.getChildren()) {
			images.add(((ImageListItem) node).getImage());
		}

		//Checks if parameters are valid
		if (!isTripParametersValid(title, categories, priceString, date, location, meetingAddress, participantLimitString)) {
			return -1;
		}

		//Converts price, date, participant limit and image now that validation is checked
		double price = Double.parseDouble(priceString);
		LocalDateTime dateTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getHour(), time.getMinute());
		int participantLimit = Integer.parseInt(participantLimitString);

		//Creates trip
		return clientController.createTrip(title, description, categories, price, dateTime, location, meetingAddress, participantLimit, organizer, organizerInstructorIn, optionalPrices, tags, images);
	}

	/**
	 * This method checks if all the parameters are valid when creating a new trip
	 *
	 * @param title
	 * @param categories
	 * @param priceString
	 * @param date
	 * @param location
	 * @param meetingAddress
	 * @param participantLimitString
	 *
	 * @return boolean isTripParametersValid - Returns true if all parameters are
	 * valid
	 */
	private boolean isTripParametersValid(String title, List<Category> categories, String priceString, LocalDate date, Location location, String meetingAddress, String participantLimitString) {
		boolean isTripParametersValid = true;
		//Title check
		if (title == null || title.isEmpty()) {
			createTripInvalidTitleText.setVisible(true);
			isTripParametersValid = false;
		} else {
			createTripInvalidTitleText.setVisible(false);
		}
		//category check
		if (categories.isEmpty()) {
			createTripInvalidCategoryText.setVisible(true);
			isTripParametersValid = false;
		} else {
			createTripInvalidCategoryText.setVisible(false);
		}
		//price check
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
		//location check
		if (location == null) {
			createTripInvalidLocationText.setVisible(true);
			isTripParametersValid = false;
		} else {
			createTripInvalidLocationText.setVisible(false);
		}
		//meetingAddress
		if (meetingAddress == null || meetingAddress.isEmpty()) {
			createTripInvalidMeetingAddressText.setVisible(true);
			isTripParametersValid = false;
		} else {
			createTripInvalidMeetingAddressText.setVisible(false);
		}
		//participantLimit check
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
		//Reset picture warning
		createTripInvalidPictureText.setVisible(false);

		return isTripParametersValid;
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="My Trips - Methods">
	/**
	 * This method handles all the buttons under the MyTrips pane
	 *
	 * @param event
	 */
	@FXML
	private void handleMyTripsButtons(ActionEvent event) {
		if (event.getSource() == myTripsCreateTripButton) {
			resetCreateTripPane();
			showPane(createTripPane1);
		} else if (event.getSource() == myTripsModifyTripButton) {

			//If no trip is selected, then nothing happens
			if (myTripsListView.getSelectionModel().isEmpty()) {

			} else {
				int id = myTripsListView.getSelectionModel().getSelectedItem().getTripId();
				viewTrip(id, true, myTripsPane);
			}
		} else if (event.getSource() == myTripsViewTripButton) {

			//If no trip is selected, then nothing happens
			if (myTripsListView.getSelectionModel().isEmpty()) {

			} else {
				int id = myTripsListView.getSelectionModel().getSelectedItem().getTripId();
				viewTrip(id, false, myTripsPane);

			}
		} else if (event.getSource() == myTripsDeleteTripButton) {
			if (!myTripsListView.getSelectionModel().isEmpty()) {
				clientController.deleteTrip(myTripsListView.getSelectionModel().getSelectedItem().getTripId());
				loadMyOrganizedTrips();
			}
		} else if (myTripsToggleGroup.getToggles().contains(event.getSource())) {
			myTripsDeleteTripButton.setDisable(true);
			myTripsModifyTripButton.setDisable(true);
			myTripsViewTripButton.setDisable(true);
			if (myTripsToggleGroup.getSelectedToggle() == myTripsToggleAll) {
				loadMyTrips();
			} else if (myTripsToggleGroup.getSelectedToggle() == myTripsOrganizedRadioButton) {
				loadMyOrganizedTrips();
			}
		}
	}

	private void resetMyTripsPane() {
		loadMyTrips();
		myTripsDeleteTripButton.setDisable(true);
		myTripsToggleAll.setSelected(true);
		myTripsModifyTripButton.setDisable(true);
		myTripsViewTripButton.setDisable(true);
	}

	@FXML
	private void handleMyTripsListView(MouseEvent event) {
		if (myTripsListView.getSelectionModel().getSelectedItem() != null) {
			int tripId = myTripsListView.getSelectionModel().getSelectedItem().getTripId();
			int organizerId = clientController.viewTrip(tripId).getOrganizer().getId();
			myTripsModifyTripButton.setDisable(organizerId != clientController.getCurrentUser().getId());
			myTripsDeleteTripButton.setDisable(organizerId != clientController.getCurrentUser().getId());
			myTripsViewTripButton.setDisable(false);
		}
	}

	/**
	 * This method loads all the trips for a specifc user under the MyTrips pane
	 *
	 */
	private void loadMyTrips() {
		Platform.runLater(() -> {
			List<Trip> myTrips = clientController.getMyTrips();

			if (myTrips != null) {
				List<HBoxCell> list = new ArrayList<>();

				for (Trip trip : myTrips) {
					list.add(new HBoxCell(trip));
				}

				ObservableList observableList = FXCollections.observableArrayList(list);
				myTripsListView.setItems(observableList);
			}
		});
	}

	private void loadMyOrganizedTrips() {
		Platform.runLater(() -> {
			List<Trip> myTrips = clientController.getMyOrganizedTrips();

			if (myTrips != null) {
				List<HBoxCell> list = new ArrayList<>();

				for (Trip trip : myTrips) {
					list.add(new HBoxCell(trip));
				}

				ObservableList observableList = FXCollections.observableArrayList(list);
				myTripsListView.setItems(observableList);
			}
		});
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Browse Users - Methods">
	/**
	 * This method handles the search button when browsing for users
	 *
	 * @param event
	 */
	@FXML
	private void handleBrowseUsersSearchButtons(ActionEvent event) {
		if (event.getSource().equals(browseUsersSearchButton)) {
			searchUsers();
		} else if (event.getSource().equals(browseUsersMessageButton)) {
			if (browseUsersListView.getSelectionModel().isEmpty()) {
				//Nothing happens if no user is selected
			} else {
				int userId = browseUsersListView.getSelectionModel().getSelectedItem().getUserId();
				goToUserConversation(userId);
			}
		}
	}

	private Conversation getConversation(Conversation conversation) {
		try {
			return clientController.getConversation(conversation);
		} catch (RemoteException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private void searchUsers() {
		Platform.runLater(() -> {
			try {
				String searchText = browseUsersTextField.getText();
				List<User> users = clientController.searchUsers(searchText);
				List<HBoxCell> list = new ArrayList<>();

//				for (User user : users) {
//					list.add(new HBoxCell(user));
//				}
				users.parallelStream().forEach((User user) -> {
					synchronized (list) {
						list.add(new HBoxCell(user));
					}
				});

				ObservableList observableList = FXCollections.observableArrayList(list);
				browseUsersListView.setItems(observableList);
			} catch (RemoteException ex) {
				Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
	}

	public void resetBrowseUsersPane() {

		//Reset parameters for browse users
		browseUsersTextField.clear();

		//Reload all users
		searchUsers();

		//Disables the message button if not logged in
		if (clientController.getCurrentUser() == null) {
			browseUsersMessageButton.setDisable(true);
		} else {
			browseUsersMessageButton.setDisable(false);
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed" desc="Messaging - Methods">
	private void getUserConversations() {
		Platform.runLater(() -> {
			List<Conversation> conversations = clientController.getUserConversations();

			if (conversations != null) {
				List<HBoxCell> list = new ArrayList<>();

				for (Conversation conversation : conversations) {
					String name = clientController.getConversationName(conversation);
					list.add(new HBoxCell(conversation, name));
				}

				ObservableList observableList = FXCollections.observableArrayList(list);
				messagingConversationsListView.setItems(observableList);
			}
		});
	}

	private void showConversation(Conversation conversation) {
		List<HBoxCell> list = new ArrayList<>();
		int userId = clientController.getCurrentUser().getId();

		for (Message message : getConversation(conversation).getMessages()) {
			list.add(new HBoxCell(message, clientController.getCurrentUser()));
		}

		Collections.reverse(list);
		messagingConversationList = FXCollections.observableArrayList(list);
		messagingActiveConversationListView.setItems(messagingConversationList);
		messagingActiveConversationListView.scrollTo(messagingConversationList.size() - 1);
		ClientMessagingHandler.setMessagesList(messagingConversationList);
	}

	private void sendMessage(String message) {
		try {
			clientController.sendMessage(message);
		} catch (RemoteException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void handleSendMessageButton(ActionEvent event) {
		sendMessage(messagingTextField.getText());
		messagingTextField.setText("");
	}

	private void goToUserConversation(int userId) {
		Conversation conversation = clientController.getUserConversation(userId);

		boolean conversationExists = false;

		for (int i = 0; i < messagingConversationsListView.getItems().size(); i++) {
			if (messagingConversationsListView.getItems().get(i).getConversationId() == conversation.getId()) {
				messagingConversationsListView.getSelectionModel().clearAndSelect(i);
				conversationExists = true;
			}
		}
		if (!conversationExists) {
			messagingConversationsListView.getItems().add(new HBoxCell(conversation, clientController.getConversationName(conversation)));
			messagingConversationsListView.getSelectionModel().clearAndSelect(messagingConversationsListView.getItems().size() - 1);
		}

		showPane(messagingPane);
	}
	// </editor-fold>

}
