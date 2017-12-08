package server;

import client.ClientUserHandler;
import database.DBManager;
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
		//TODO - Should check if email is unique
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
		int newUserId = dbm.executeInsertAndGetId(query);

		return signIn(email, password);
	}

	public static User signIn(String email, String password) {
		String query = "SELECT userID, email, userName, imageFile "
						+ "FROM Users "
						+ "NATURAL JOIN Images "
						+ "WHERE email = '" + email + "' AND password = '" + password + "';";

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
	 * This method updates the current user
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
	 * This method handels changing the user profile picture
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
