package client;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import interfaces.Trip;
import interfaces.User;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

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
	@FXML
	private Button logInOutButton;
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
	private ImageView newAccountImageView;
	@FXML
	private TextField newAccountNameTextField;
	@FXML
	private TextField newAccountEmailTextField;
	@FXML
	private PasswordField newAccountPasswordTextField;
	@FXML
	private PasswordField newAccountRepeatPasswordTextField;
	@FXML
	private Button newAccountCreateButton;
	@FXML
	private Button newAccountProfilePictureButton;

	private File newAccountProfilePictureFile;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			clientController = new ClientController();

			newAccountImageView.setImage(new Image("default_profile_picture.png"));
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

	private void handleLogInButton(ActionEvent event) {
		String username = logInEmailTextField.getText();
		String password = hashPassword(logInPasswordTextField.getText());

		try {
			clientController.signIn(username, password);
		} catch (RemoteException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void handleChooseProfilePictureButton(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select profile picture");
		Node source = (Node) event.getSource();
		Window stage = source.getScene().getWindow();
		newAccountProfilePictureFile = fileChooser.showOpenDialog(stage);
		Image profilePicture = new Image(newAccountProfilePictureFile.toURI().toString());

		newAccountImageView.setImage(profilePicture);
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
				clientController.signUp(user, password);
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

	private byte[] convertImageToByteArray() {
		byte[] returnArray = null;
		try {
			ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(newAccountProfilePictureFile);
			ImageIO.write(image, "png", byteArrayOutput);
			byteArrayOutput.flush();
			
			String base64String = Base64.encode(byteArrayOutput.toByteArray());
			byteArrayOutput.close();
			
			returnArray = Base64.decode(base64String);
		} catch (IOException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Base64DecodingException ex) {
			Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return returnArray;				
	}
}
