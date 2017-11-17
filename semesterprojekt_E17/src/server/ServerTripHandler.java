package server;

import database.DBManager;
import interfaces.Category;
import interfaces.InstructorListItem;
import interfaces.Location;
import interfaces.OptionalPrice;
import interfaces.Trip;
import interfaces.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		if (!categoriesExists(newTrip.getCategories())) {
			return -1;
		}

		//Checks if location id exists
		if (!locationExists(newTrip.getLocation())) {
			return -1;
		}

		//Checks if organizer id exists
		if (!userExists(newTrip.getOrganizer())) {
			return -1;
		}

		//Checks if organizer have the required certificates
		for (InstructorListItem instructorListItem : newTrip.getInstructors()) {
			if (!certificateExists(instructorListItem)) {
				return -1;
			}
		}

		//Checks if values are valid and then converts values to SQL values
		if (newTrip.getTitle() == null || newTrip.getTitle().isEmpty()) {
			return -1;
		}
		String sqlTripTitle = "'" + newTrip.getTitle() + "'";

		String sqlTripDescription;
		if (newTrip.getDescription() == null || newTrip.getDescription().isEmpty()) {
			sqlTripDescription = "null";
		} else {
			sqlTripDescription = "'" + newTrip.getDescription() + "'";
		}

		if (newTrip.getPrice() < 0) {
			return -1;
		}
		String sqlTripPrice = newTrip.getPrice() + "";

		//Chooses the right format to save to the date of meeting
		if (newTrip.getTimeStart() == null || newTrip.getTimeStart().isBefore(LocalDateTime.now())) {
			return -1;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String sqlTimeStart = "'" + newTrip.getTimeStart().format(formatter) + "'";

		String sqlTripAddress;
		if (newTrip.getMeetingAddress() == null || newTrip.getMeetingAddress().isEmpty()) {
			sqlTripAddress = "null";
		} else {
			sqlTripAddress = "'" + newTrip.getMeetingAddress() + "'";
		}

		String sqlParticipantLimit;
		if (newTrip.getParticipantLimit() < 0) {
			return -1;
		}
		if (newTrip.getParticipantLimit() == 0) {
			sqlParticipantLimit = "null";
		} else {
			sqlParticipantLimit = newTrip.getParticipantLimit() + "";
		}

		//Creates the group conversation and returnes the conversation id. -1 is returned if it failed
		ArrayList<User> participants = new ArrayList<>();
		participants.add(newTrip.getOrganizer());
		int sqlConversationId = addConversation(participants);
		if (sqlConversationId == -1) {
			return -1;
		}
		//Adds a trip to the database and returnes its id
		int tripId = DBManager.getInstance().executeInsertAndGetId("INSERT INTO "
						+ "Trips (tripID, tripTitle, tripDescription, tripPrice, timeStart, "
						+ "locationID, tripAddress, participantLimit, userID, conversationID) "
						+ "VALUES ("
						+ "DEFAULT, "
						+ sqlTripTitle + ", "
						+ sqlTripDescription + ", "
						+ sqlTripPrice + ", "
						+ sqlTimeStart + ", "
						+ newTrip.getLocation().getId() + ", "
						+ sqlTripAddress + ", "
						+ sqlParticipantLimit + ", "
						+ newTrip.getOrganizer().getId() + ", "
						+ sqlConversationId + ");");

		//Adds categories to the trip in the database
		addCategories(tripId, Category.getCategoryIds(newTrip.getCategories()));

		//Adds organizer as participant in the trip in the database
		addParticipant(tripId, newTrip.getOrganizer().getId());

		//Adds a instructor in the trip for each instructorListItem
		for (InstructorListItem instructorListItem : newTrip.getInstructors()) {
			addInstructorInTrip(tripId, instructorListItem.getUser().getId(), instructorListItem.getCategory().getId());
		}

		//Adds the optional prices to the trip in the database
		addOptionalPrices(tripId, newTrip.getOptionalPrices());

		//Adds the tags to the trip in the database
		addTags(tripId, newTrip.getTags());

		//Adds the images to the trip in the database
		if (newTrip.getImages() != null) {
			for (byte[] image : newTrip.getImages()) {
				if (image != null) {
					addImagetoTrip(tripId, image);
				}
			}
		}

		return tripId;
	}

	public static List<Category> getCategories() {
		ArrayList<Category> categories = new ArrayList<>();
		ResultSet rs = DBManager.getInstance().executeQuery("SELECT categoryID, categoryName FROM Categories;");
		try {
			while (rs.next()) {
				categories.add(new Category(rs.getInt(1), rs.getString(2)));
			}
			return categories;
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	/**
	 * Gets all category ids from the database.
	 *
	 * @return a list with category ids.
	 */
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

	private static boolean categoriesExists(List<Category> categories) {
		if (categories == null) {
			return false;
		}
		return getCategoryIds().containsAll(Category.getCategoryIds(categories));

	}

	public static List<Location> getLocations() {
		ArrayList<Location> locations = new ArrayList<>();
		ResultSet rs = DBManager.getInstance().executeQuery("SELECT locationID, locationName FROM Locations;");
		try {
			while (rs.next()) {
				locations.add(new Location(rs.getInt(1), rs.getString(2)));
			}
			return locations;
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	/**
	 * Gets all location ids from the database.
	 *
	 * @return a list with location ids.
	 */
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

	/**
	 * Checks if the given location exists in the database
	 *
	 * @param location
	 * @return
	 */
	private static boolean locationExists(Location location) {
		if (location == null) {
			return false;
		}
		return getLocationIds().contains(location.getId());
	}

	/**
	 * Checks if the given user exists in the database.
	 *
	 * @param user
	 * @return true if user exist or false if user does not exist.
	 */
	private static boolean userExists(User user) {
		if (user == null) {
			return false;
		}
		ResultSet rs = DBManager.getInstance().executeQuery("SELECT userID FROM Users WHERE userID = " + user.getId() + ";");
		try {
			if (rs.next()) {
				return true;
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	private static boolean certificateExists(InstructorListItem instructorListItem) {
		if (instructorListItem == null || instructorListItem.getUser() == null || instructorListItem.getCategory() == null) {
			return false;
		}
		ResultSet rs = DBManager.getInstance().executeQuery("SELECT categoryID FROM Certificates "
						+ "WHERE userID = " + instructorListItem.getUser().getId() + " "
						+ "AND categoryID = " + instructorListItem.getCategory().getId() + ";");
		try {
			if (rs.next()) {
				return true;
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	private static int addConversation(List<User> users) {
		if (users == null || users.isEmpty()) {
			return -1;
		}

		//Inserts a new conversation id into Conversations
		int conversationId = DBManager.getInstance().executeInsertAndGetId("INSERT INTO Conversations (conversationID) VALUES (DEFAULT);");

		//Inserts all the users from the trip into UsersInConversations
		String query = "INSERT INTO UsersInConversations (conversationID, userID) VALUES ";
		String queryValues = "";
		for (User user : users) {
			queryValues += ("(" + conversationId + ", " + user.getId() + "), ");
		}
		query += queryValues.substring(0, queryValues.length() - 2) + ";";
		DBManager.getInstance().executeUpdate(query);
		return conversationId;
	}

	private static void addCategories(int tripId, List<Integer> categoryIds) {
		String query = "INSERT INTO CategoriesInTrips (tripID, categoryID) VALUES ";
		String queryValues = "";
		for (int categoryId : categoryIds) {
			queryValues += ("(" + tripId + ", " + categoryId + "), ");
		}
		query += queryValues.substring(0, queryValues.length() - 2) + ";";
		DBManager.getInstance().executeUpdate(query);
	}

	private static void addParticipant(int tripId, int userId) {
		DBManager.getInstance().executeUpdate("INSERT INTO UsersInTrips (tripID, userID) "
						+ "VALUES (" + tripId + ", " + userId + ");");
	}

	private static void addInstructorInTrip(int tripId, int userId, int categoryId) {
		DBManager.getInstance().executeUpdate("INSERT INTO InstructorsInTrips (tripID, userID, categoryID) "
						+ "VALUES (" + tripId + ", " + userId + ", " + categoryId + ");");
	}

	private static void addOptionalPrices(int tripId, List<OptionalPrice> optionalPrices) {
		if (optionalPrices == null || optionalPrices.isEmpty()) {
			return;
		}
		String query = "INSERT INTO OptionalPrices (priceID, tripID, optionalPrice, priceDescription) VALUES ";
		String queryValues = "";
		for (OptionalPrice optionalPrice : optionalPrices) {
			queryValues += ("(DEFAULT, "
							+ tripId + ", "
							+ optionalPrice.getPrice() + ", "
							+ "'" + optionalPrice.getDescription() + "'), ");
		}
		query += queryValues.substring(0, queryValues.length() - 2) + ";";
		DBManager.getInstance().executeUpdate(query);
	}

	private static void addTags(int tripId, Set<String> tags) {
		if (tags == null || tags.isEmpty()) {
			return;
		}
		String query = "INSERT INTO TagsInTrips (tripID, tag) VALUES ";
		String queryValues = "";
		for (String tag : tags) {
			queryValues += ("(" + tripId + ", '" + tag + "'), ");
		}
		query += queryValues.substring(0, queryValues.length() - 2) + ";";
		DBManager.getInstance().executeUpdate(query);
	}

	private static void addImagetoTrip(int tripId, byte[] image) {
		int imageId = DBManager.getInstance().executeImageInsertAndGetId("INSERT INTO Images (imageID, imageFile) VALUES (DEFAULT, ?);", image);
		DBManager.getInstance().executeUpdate("INSERT INTO ImagesInTrips (imageID, tripID) VALUES (" + imageId + ", " + tripId + ");");
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

	static Trip viewTrip(int tripID) {
		String query = "SELECT * FROM Trips WHERE tripID = " + tripID + ";";
		ResultSet rs = DBManager.getInstance().executeQuery(query);

		try {
			while (rs.next()) {
				int id = rs.getInt("tripID");
				String title = rs.getString("tripTitle");
				String description = rs.getString("tripDescription");
				double price = Double.parseDouble(rs.getString("tripPrice"));
				return new Trip(id, title, description, price, null);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

}
