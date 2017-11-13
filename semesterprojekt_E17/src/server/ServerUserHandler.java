package server;

import database.DBManager;
import interfaces.Category;
import interfaces.User;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerUserHandler {

	public static User createUser(User newUser, String password) {
		try {
			String newUserEmail = newUser.getEmail();
			String newUserName = newUser.getName();
			List<Category> newUserCertificates = newUser.getCertificates();
			byte[] newUserImage = newUser.getImage();

			Connection connection = DBManager.getInstance().getConnection();

			//Inserts the imageFile
			String imageQuery = "INSERT INTO Images (imageTitle, imageFile) VALUES ('" + newUserName + "Image', ?)";
			PreparedStatement imageStatement = connection.prepareStatement(imageQuery, Statement.RETURN_GENERATED_KEYS);
			imageStatement.setBytes(1, newUserImage);
			imageStatement.executeUpdate();
			int imageId;
			ResultSet imageRs = imageStatement.getGeneratedKeys();
			if (imageRs.next()) {
				imageId = imageRs.getInt(1);
			} else {
				imageId = -1;
			}

			String query = "INSERT INTO Users(email, password, userName, imageId) "
							+ "VALUES(?, ?, ?, ?)";
			PreparedStatement userStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			userStatement.setString(1, newUserEmail);
			userStatement.setString(2, password);
			userStatement.setString(3, newUserName);
			userStatement.setInt(4, imageId);

			userStatement.executeUpdate();

			int newUserId;
			ResultSet userIdRs = userStatement.getGeneratedKeys();

			if (userIdRs.next()) {
				newUserId = imageRs.getInt(1);
			} else {
				newUserId = -1;
			}

			String getUserQuery = "SELECT userId, email, userName, imageFile "
							+ "FROM Users, Images "
							+ "WHERE userId = " + newUserId + " AND Users.imageId = Images.imageId";

			ResultSet userRs = DBManager.getInstance().executeQuery(getUserQuery);

			int userId = userRs.getInt(1);
			String userEmail = userRs.getString(2);
			String userName = userRs.getString(3);
			byte[] userImage = userRs.getBytes(4);

			User user = new User(userId, userEmail, userName, userImage);

			return user;
		} catch (SQLException ex) {
			Logger.getLogger(ServerUserHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static User signIn(String username, String password) {
		String query = "SELECT Users.userID, email, userName, imageFile "
						+ "FROM Users, Images "
						+ "WHERE email = '" + username + "' AND password = '" + password + "' AND Users.imageID = Images.imageId";
		User user = null;
		
		try {
			ResultSet userRs = DBManager.getInstance().executeQuery(query);

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
}
