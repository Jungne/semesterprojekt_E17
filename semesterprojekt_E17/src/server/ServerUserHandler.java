package server;

import database.DBManager;
import interfaces.Category;
import interfaces.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerUserHandler {

	private static DBManager dbm = DBManager.getInstance();

	public static User createUser(User newUser, String password) {
		String newUserEmail = newUser.getEmail();
		String newUserName = newUser.getName();
		List<Category> newUserCertificates = newUser.getCertificates();
		byte[] newUserImage = newUser.getImage();

		if (newUserEmail == null || newUserEmail.isEmpty()) {
			throw new IllegalArgumentException("Invalid user email.");
		}
		if (newUserName == null || newUserName.isEmpty()) {
			return null;
		}
		if (password == null || password.isEmpty()) {
			return null;
		}

		String sqlImageId = "null";
		if (newUserImage != null) {
			//Inserts the imageFile
			String imageQuery = "INSERT INTO Images (imageTitle, imageFile) VALUES ('" + newUserName + "Image', ?)";
			int imageId = dbm.executeImageInsertAndGetId(imageQuery, newUserImage);
			sqlImageId = imageId + "";
		}

		//Inserts the new user.
		String query = "INSERT INTO Users(email, password, userName, imageId) "
						+ "VALUES('" + newUserEmail + "', '" + password + "', '" + newUserName + "', " + sqlImageId + ")";
		int newUserId = dbm.executeInsertAndGetId(query);

		return signIn(newUserEmail, password);
	}

	public static User signIn(String signInEmail, String password) {
		String query = "SELECT Users.userID, email, userName, imageFile "
						+ "FROM Users, Images "
						+ "WHERE email = '" + signInEmail + "' AND password = '" + password + "' AND Users.imageID = Images.imageId";
		User user = null;

		try {
			ResultSet userRs = dbm.executeQuery(query);

			userRs.next();

			int userId = userRs.getInt(1);
			String userEmail = userRs.getString(2);
			String userName = userRs.getString(3);
			byte[] userImage = userRs.getBytes(4);

			user = new User(userId, userEmail, userName, userImage);
		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return user;
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
				byte[] image = usersRs.getBytes("imageFile");
				
				users.add(new User(id, email, userName, image));
			}

			return users;
		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
