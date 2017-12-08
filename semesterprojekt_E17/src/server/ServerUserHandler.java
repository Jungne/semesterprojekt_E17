package server;

import database.DBManager;
import interfaces.Category;
import interfaces.Image;
import interfaces.User;
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
