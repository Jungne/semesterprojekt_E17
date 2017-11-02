package server;

import database.DBManager;
import interfaces.Category;
import interfaces.Trip;
import interfaces.User;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 * This class handles creation of trip objects to be sent to the client. This
 * class also handles storage of trip objects in the database received from the
 * client.
 *
 * @author group 12
 */
public class ServerTripHandler {

	/**
	 * Creates a trip in the database by using the information in newTrip.
	 *
	 * @param newTrip
	 * @return the trip id of the newly created trip or -1 if the trip failed to
	 * be created.
	 */
	public static int createTrip(Trip newTrip) {

		//Checks if category ids exists
		if (!getCategoryIds().containsAll(newTrip.getCategoryIds())) {
			return -1;
		}

		//Checks if location id exists
		if (!getLocationIds().contains(newTrip.getLocation().getId())) {
			return -1;
		}

		//Checks if organizer id exists
		//Checks if the instructor is organizer and if the trip contain the associated category
		//Checks if the organizer is allowed to instruct in that category
		//
		//Creates the group conversation and returnes the conversation id
		int groupConversationId = createConversation(newTrip.getParticipants());

		//TODO - create rest of trip
		//temp return
		return -1;
	}

	private static List<Integer> getCategoryIds() {
		ArrayList<Integer> categoryIds = new ArrayList<>();
		ResultSet rs = DBManager.getInstance().executeQuery("SELECT categoryID FROM Categories;");
		try {
			while (rs.next()) {
				categoryIds.add(rs.getInt(1));
			}
			return categoryIds;
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	private static List<Integer> getLocationIds() {
		ArrayList<Integer> locationIds = new ArrayList<>();
		ResultSet rs = DBManager.getInstance().executeQuery("SELECT locationID FROM Locations;");
		try {
			while (rs.next()) {
				locationIds.add(rs.getInt(1));
			}
			return locationIds;
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	private static int createConversation(List<User> users) {
		//Inserts a new conversation id into Conversations
		int conversationId = DBManager.getInstance().executeInsertAndGetId("INSERT INTO Conversations (conversationID) VALUES (DEFAULT);");

		//Inserts all the users from the trip into UsersInConversations
		String query = "INSERT INTO UsersInConversations (userID, conversationID) VALUES ";
		String queryValues = "";
		for (User user : users) {
			queryValues += ("(" + user.getId() + ", " + conversationId + "), ");
		}
		query += queryValues.substring(0, queryValues.length() - 2) + ";";
		DBManager.getInstance().executeUpdate(query);
		return conversationId;
	}

	static void modifyTrip(Trip trip) {
		String query = "UPDATE trips "
						+ "SET "
						+ "tripTitle = " + trip.getTitle() + ", "
						+ "tripDescription = " + trip.getDescription() + ", "
						+ "tripPrice = " + trip.getPrice() + " "
						+ "WHERE tripID = " + trip.getId() + ";";

		DBManager.getInstance().executeUpdate(query);

		//Need to also update trip categories and users in trip
	}

	/**
	 * This method handles searching for trips, by building a SQL query from the
	 * given parameters.
	 *
	 * @param searchTitle used for a regex.
	 * @param category
	 * @param timedateStart
	 * @param location
	 * @param priceMAX
	 * @return list of trips matching search parameters.
	 */
	public static List<Trip> searchTrip(String searchTitle, int category, Date timedateStart, int location, double priceMAX) {

		//Initializes the query string.
		String query = "SELECT Trips.tripID, tripTitle, tripdescription, tripPrice, imageFile FROM Trips, ImagesInTrips, Images "
						+ "WHERE trips.tripid = imagesintrips.tripid AND images.imageID = imagesintrips.imageID AND imagesintrips.imageid IN (SELECT MIN(imageid) FROM imagesintrips GROUP BY tripid)";

		//These if statements checks if the different parameters are used, and adds the necessary SQL code to the query string.
		if (!searchTitle.equals("")) {
			query += " AND tripTitle LIKE '%" + searchTitle + "%'";
		}

		if (category >= 0) {
			query += " AND categoryID = '" + category + "'";
		}

		if (timedateStart != null) {
			query += " AND timeStart >= '" + timedateStart + "'";
		}

		if (location >= 0) {
			query += " AND locationID = '" + location + "'";
		}

		if (priceMAX >= 0) {
			query += " AND tripPrice <= " + priceMAX + "'";
		}

		//Initializes a resultset and an ArrayList for handling the creation of trips to be returned.
		ResultSet searchResult = DBManager.getInstance().executeQuery(query);
		ArrayList<Trip> searchResultTrips = new ArrayList<>();

		try {
			while (searchResult.next()) {
				int id = searchResult.getInt(1);
				String title = searchResult.getString(2);
				String description = searchResult.getString(3);
				double price = searchResult.getDouble(4);
				byte[] image = searchResult.getBytes(5);
				searchResultTrips.add(new Trip(id, title, description, price, image));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return searchResultTrips;
	}

	public static void deleteTrip(Trip trip) {
		String query = "DELETE FROM Trips WHERE tripID = " + trip.getId() + ";";

		DBManager.getInstance().executeUpdate(query);
	}

	public static void participateInTrip(Trip trip, User user) {
		int tripID = trip.getId();
		int userID = user.getId();

		String query = "INSERT INTO UsersInTrips VALUES ('" + tripID + "', '" + userID + "');";
		DBManager.getInstance().executeUpdate(query);
	}

	public static void kickParticipant(Trip trip, User user) {
		int tripID = trip.getId();
		int userID = user.getId();
		String query = "DELETE FROM UsersInTrips WHERE tripID = " + trip.getId() + "AND userID = " + userID + ";";
		DBManager.getInstance().executeUpdate(query);
	}

	public static Trip showTrip(int tripsID) {
		String query = "SELECT * FROM Trips WHERE tripID = " + tripsID + ";";
		Trip trip = (Trip) DBManager.getInstance().executeQuery(query);
		return trip;
	}

}
