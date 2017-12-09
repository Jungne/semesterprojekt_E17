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

/**
 * This class is the Server User Handler responsible for managing all users in
 * the system. This class handles creation of user objects to be sent to the
 * client and storage of user objects in the database received from the client.
 * This class also handles searching for users and log-on functionality. The
 * Server User Handler communicates with the Database Manager
 *
 * @author group 12
 */
public class ServerUserHandler {

	private static DBManager dbm = DBManager.getInstance();

	/**
	 *
	 * @param user
	 * @param password
	 * @return
	 */
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
		String sqlImageId = "null";
		if (profilePicture != null && profilePicture.getImageFile() != null) {
			String sqlImageTitle;
			if (profilePicture.getTitle() == null || profilePicture.getTitle().isEmpty()) {
				sqlImageTitle = "null";
			} else {
				sqlImageTitle = "'" + profilePicture.getTitle() + "'";
			}
			String imageQuery = "INSERT INTO Images (imageID, imageTitle, imageFile) VALUES (DEFAULT, " + sqlImageTitle + ", ?)";
			sqlImageId = dbm.executeImageInsertAndGetId(imageQuery, profilePicture.getImageFile()) + "";
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

	/**
	 *
	 * @param email
	 * @param password
	 * @return
	 */
	public static User signIn(String email, String password) {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("User email is null or empty.");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("User password is null or empty.");
		}

		String sqlEmail = "'" + email + "'";
		String sqlPassword = "'" + password + "'";

		String query = "SELECT userID, email, userName, imageFile "
						+ "FROM Users "
						+ "NATURAL JOIN Images "
						+ "WHERE email = " + sqlEmail + " AND password = " + sqlPassword + ";";

		try {
			ResultSet userRs = dbm.executeQuery(query);

			if (userRs.next()) {
				int userId = userRs.getInt("userID");
				String userEmail = userRs.getString("email");
				String userName = userRs.getString("userName");
				byte[] userImageFile = userRs.getBytes("imageFile");

				List<Category> certificates = getCertificates(userId);

				return new User(userId, userEmail, userName, certificates, new Image(userImageFile));
			}

		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

	/**
	 *
	 * @param email
	 * @return
	 */
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

	/**
	 *
	 * @param userId
	 * @return
	 */
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

	/**
	 * This method updates the current user.
	 *
	 * @param currentUserID
	 * @return the current user
	 */
	public static User updateUser(int currentUserID) throws RemoteException {

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

	/**
	 * This method handels changing the user profile picture.
	 *
	 * @param currentUserID
	 * @param profilePicture
	 */
	public static void changeProfilePicture(int currentUserID, Image profilePicture) throws RemoteException {

		//Inserts an image 
		String sqlImageId = "null";
		if (profilePicture != null && profilePicture.getImageFile() != null) {
			String sqlImageTitle;
			if (profilePicture.getTitle() == null || profilePicture.getTitle().isEmpty()) {
				sqlImageTitle = "null";
			} else {
				sqlImageTitle = "'" + profilePicture.getTitle() + "'";
			}
			String imageQuery = "INSERT INTO Images (imageID, imageTitle, imageFile) VALUES (DEFAULT, " + sqlImageTitle + ", ?)";
			sqlImageId = dbm.executeImageInsertAndGetId(imageQuery, profilePicture.getImageFile()) + "";
		}

		//Query to update user with new profile picture
		String updateUserQuery = ""
						+ "UPDATE Users "
						+ "SET imageID = " + sqlImageId + "\n"
						+ "WHERE userId=  " + currentUserID;

		dbm.executeUpdate(updateUserQuery);
	}

	/**
	 *
	 * @param query
	 * @return
	 */
	public static List<User> searchUsers(String query) {
		try {
			String sqlQuery = ""
							+ "SELECT Users.userID, email, userName, imageFile\n"
							+ "FROM Users\n"
							+ "INNER JOIN Images ON Users.imageID = images.imageID\n"
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
