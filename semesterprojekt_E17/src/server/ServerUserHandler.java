package server;

import database.DBManager;
import interfaces.Category;
import interfaces.Image;
import interfaces.User;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerUserHandler {

	private static DBManager dbm = DBManager.getInstance();

	public static User createUser(User user, String password) {
		if (user == null) {
			throw new IllegalArgumentException("User is null.");
		}

		String email = user.getEmail();
		String name = user.getName();
		Image profilePicture = user.getImage();

		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("User email is null or empty.");
		}
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("User name is null or empty.");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("User password is null or empty.");
		}
		if (!isEmailUnique(email)) {
			throw new IllegalArgumentException("User email is not unique.");
		}

		//Inserts an image if the given image is not null
		String sqlImageId;
		try {
			sqlImageId = addImage(profilePicture) + "";
		} catch (IllegalArgumentException ex) {
			sqlImageId = "null";
		}

		//Inserts the new user.
		String sqlEmail = "'" + email + "'";
		String sqlPassword = "'" + password + "'";
		String sqlUserName = "'" + name + "'";
		String query = "INSERT INTO Users(email, password, userName, imageId) "
						+ "VALUES(" + sqlEmail + ", " + sqlPassword + ", " + sqlUserName + ", " + sqlImageId + ")";
		dbm.executeUpdate(query);

		return signIn(email, password);
	}

	public static User signIn(String email, String password) {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("User email is null or empty.");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("User password is null or empty.");
		}

		String sqlEmail = "'" + email + "'";
		String sqlPassword = "'" + password + "'";

		String query = "SELECT userID, email, userName "
						+ "FROM Users "
						+ "WHERE email = " + sqlEmail + " AND password = " + sqlPassword + ";";

		try {
			ResultSet userRs = dbm.executeQuery(query);

			if (userRs.next()) {
				int userId = userRs.getInt("userID");
				String userEmail = userRs.getString("email");
				String userName = userRs.getString("userName");

				List<Category> certificates = getCertificates(userId);
				Image profilePicture = getProfilePicture(userId);

				return new User(userId, userEmail, userName, certificates, profilePicture);
			}

		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

	private static boolean isEmailUnique(String email) {
		ResultSet rs = dbm.executeQuery("SELECT * FROM Users WHERE email = '" + email + "';");

		try {
			if (rs.next()) {
				return false;
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return true;
	}

	private static List<Category> getCertificates(int userId) {
		List<Category> certificates = new ArrayList<>();
		String query = "SELECT categoryID, categoryName FROM Categories NATURAL JOIN Certificates WHERE userID = " + userId + ";";

		try {
			ResultSet certificatesRs = dbm.executeQuery(query);

			while (certificatesRs.next()) {
				int id = certificatesRs.getInt("categoryID");
				String name = certificatesRs.getString("categoryName");
				certificates.add(new Category(id, name));
			}

		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return certificates;
	}

	private static Image getProfilePicture(int userId) {
		Image profilePicture = null;
		String query = "SELECT imageID, imageTitle, imageFile FROM Images NATURAL JOIN Users WHERE userID = " + userId + ";";

		try {
			ResultSet imageRs = dbm.executeQuery(query);

			if (imageRs.next()) {
				int id = imageRs.getInt("imageID");
				String title = imageRs.getString("imageTitle");
				byte[] imageFile = imageRs.getBytes("imageFile");

				profilePicture = new Image(id, title, imageFile);
			}

		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return profilePicture;
	}

	private static int addImage(Image image) {
		if (image == null || image.getImageFile() == null) {
			throw new IllegalArgumentException("Image is null or empty.");
		}
		String sqlImageTitle = "null";
		if (image.getTitle() != null && !image.getTitle().isEmpty()) {
			sqlImageTitle = "'" + image.getTitle() + "'";
		}
		String imageQuery = "INSERT INTO Images (imageID, imageTitle, imageFile) VALUES (DEFAULT, " + sqlImageTitle + ", ?)";
		return dbm.executeImageInsertAndGetId(imageQuery, image.getImageFile());
	}

	/**
	 * This method updates the current user
	 *
	 * @param currentUserID
	 * @return the current user
	 */
	public static User updateUser2(int currentUserID) throws RemoteException {
		String query = "SELECT userID, email, userName, imageFile "
						+ "FROM Users "
						+ "NATURAL JOIN Images "
						+ "WHERE userID = " + currentUserID + ";";

		try {
			ResultSet userRs = dbm.executeQuery(query);

			if (userRs.next()) {
				int userId = userRs.getInt("userID");
				String userEmail = userRs.getString("email");
				String userName = userRs.getString("userName");
				byte[] userImageFile = userRs.getBytes("imageFile");

				return new User(userId, userEmail, userName, new Image(userImageFile));
			}

		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

	public static User updateUser(int userId) {
		User user = null;
		String query = "SELECT email, userName FROM Users WHERE userID = " + userId + ";";
		ResultSet userRs = dbm.executeQuery(query);

		try {
			if (userRs.next()) {
				String email = userRs.getString("email");
				String userName = userRs.getString("userName");

				List<Category> certificates = getCertificates(userId);
				Image profilePicture = getProfilePicture(userId);

				user = new User(userId, email, userName, certificates, profilePicture);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return user;
	}

	/**
	 * This method handels changing the user profile picture
	 *
	 * @param userId
	 * @param profilePicture
	 * @throws java.rmi.RemoteException
	 */
	public static void changeProfilePicture(int userId, Image profilePicture) throws RemoteException {
		if (profilePicture == null || profilePicture.getImageFile() == null) {
			throw new IllegalArgumentException("Image is null or empty.");
		}

		//Gets old image id as String
		String sqlOldImageId = null;
		String getImageIdQuery = "SELECT imageID FROM Users WHERE userID = " + userId + ";";
		ResultSet rs = dbm.executeQuery(getImageIdQuery);
		try {
			if (rs.next()) {
				sqlOldImageId = rs.getInt("imageID") + "";
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		//Adds new image to database
		int imageId;
		try {
			imageId = addImage(profilePicture);
		} catch (IllegalArgumentException ex) {
			return;
		}

		//Updates user to refer to new image instead
		String updateUserQuery = "UPDATE Users SET imageID = " + imageId + " WHERE userID = " + userId + ";";
		dbm.executeUpdate(updateUserQuery);

		//Deletes old image from datebase
		if (sqlOldImageId != null) {
			String deleteImageQuery = "DELETE FROM Images WHERE imageID = " + sqlOldImageId + ";";
			dbm.executeUpdate(deleteImageQuery);
		}
	}

	public static List<User> searchUsers(String query) {
		try {
			String sqlQuery = ""
							+ "SELECT Users.userID, email, userName, imageFile "
							+ "FROM Users "
							+ "INNER JOIN Images ON Users.imageID = images.imageID "
							+ "WHERE LOWER(userName) LIKE LOWER('%" + query + "%')";

			ResultSet usersRs = dbm.executeQuery(sqlQuery);

			ArrayList<User> users = new ArrayList<>();

			while (usersRs.next()) {
				int id = usersRs.getInt("userID");
				String email = usersRs.getString("email");
				String userName = usersRs.getString("userName");
				byte[] imageFile = usersRs.getBytes("imageFile");

				users.add(new User(id, email, userName, new Image(imageFile)));
			}

			return users;
		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

}
