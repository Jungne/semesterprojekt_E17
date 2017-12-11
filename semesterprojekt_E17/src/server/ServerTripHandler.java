package server;

import database.DBManager;
import shared.Category;
import shared.FullTripException;
import shared.Image;
import shared.InstructorListItem;
import shared.Location;
import shared.OptionalPrice;
import shared.Trip;
import shared.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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

	private static DBManager dbm = DBManager.getInstance();

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
		if (newTrip.getInstructors() != null) {
			for (InstructorListItem instructorListItem : newTrip.getInstructors()) {
				if (!certificateExists(instructorListItem)) {
					return -1;
				}
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

		if (newTrip.getTimeStart() == null || newTrip.getTimeStart().isBefore(LocalDateTime.now())) {
			return -1;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String sqlTimeStart = "'" + newTrip.getTimeStart().format(formatter) + "'";

		String sqlTripAddress;
		if (newTrip.getMeetingAddress() == null || newTrip.getMeetingAddress().isEmpty()) {
			return -1;
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
		int sqlConversationId = ServerMessagingHandler.addConversation("trip");
		if (sqlConversationId == -1) {
			return -1;
		}
		//Adds a trip to the database and returnes its id
		int tripId = dbm.executeInsertAndGetId("INSERT INTO "
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

		//Adds organizer as participant in the trip in the database and adds to trip conversation.
		addParticipant(tripId, newTrip.getOrganizer().getId());

		//Adds a instructor in the trip for each instructorListItem
		if (newTrip.getInstructors() != null) {
			for (InstructorListItem instructorListItem : newTrip.getInstructors()) {
				addInstructorInTrip(tripId, instructorListItem.getUser().getId(), instructorListItem.getCategory().getId());
			}
		}

		//Adds the optional prices to the trip in the database
		addOptionalPrices(tripId, newTrip.getOptionalPrices());

		//Adds the tags to the trip in the database
		addTags(tripId, newTrip.getTags());

		//Adds the images to the trip in the database
		if (newTrip.getImages() != null) {
			for (Image image : newTrip.getImages()) {
				if (image != null && image.getImageFile() != null) {
					addImagetoTrip(tripId, image);
				}
			}
		}

		return tripId;
	}

	public static List<Category> getCategories() {
		ArrayList<Category> categories = new ArrayList<>();
		ResultSet rs = dbm.executeQuery("SELECT categoryID, categoryName FROM Categories;");
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
		ResultSet rs = dbm.executeQuery("SELECT categoryID FROM Categories;");
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
		ResultSet rs = dbm.executeQuery("SELECT locationID, locationName FROM Locations;");
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
		ResultSet rs = dbm.executeQuery("SELECT locationID FROM Locations;");
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
	 * >>>>>>> refs/remotes/origin/master Checks if the given user exists in the
	 * database.
	 *
	 * @param user
	 * @return true if user exist or false if user does not exist.
	 */
	private static boolean userExists(User user) {
		if (user == null) {
			return false;
		}
		ResultSet rs = dbm.executeQuery("SELECT userID FROM Users WHERE userID = " + user.getId() + ";");
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
		ResultSet rs = dbm.executeQuery("SELECT categoryID FROM Certificates "
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

	private static void addCategories(int tripId, List<Integer> categoryIds) {
		String query = "INSERT INTO CategoriesInTrips (tripID, categoryID) VALUES ";
		String queryValues = "";
		for (int categoryId : categoryIds) {
			queryValues += ("(" + tripId + ", " + categoryId + "), ");
		}
		query += queryValues.substring(0, queryValues.length() - 2) + ";";
		dbm.executeUpdate(query);
	}

	private static void addParticipant(int tripId, int userId) {
		dbm.executeUpdate("INSERT INTO UsersInTrips (tripID, userID) "
						+ "VALUES (" + tripId + ", " + userId + ");");
		ServerMessagingHandler.addUserToConversation(userId, getConversationId(tripId));
	}

	private static void addInstructorInTrip(int tripId, int userId, int categoryId) {
		dbm.executeUpdate("INSERT INTO InstructorsInTrips (tripID, userID, categoryID) "
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
		dbm.executeUpdate(query);
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
		dbm.executeUpdate(query);
	}

	private static void addImagetoTrip(int tripId, Image image) {
		String sqlImageTitle;
		if (image.getTitle() == null || image.getTitle().isEmpty()) {
			sqlImageTitle = "null";
		} else {
			sqlImageTitle = "'" + image.getTitle() + "'";
		}

		int imageId = dbm.executeImageInsertAndGetId("INSERT INTO Images (imageID, imageTitle, imageFile) VALUES (DEFAULT, " + sqlImageTitle + ", ?);", image.getImageFile());
		dbm.executeUpdate("INSERT INTO ImagesInTrips (imageID, tripID) VALUES (" + imageId + ", " + tripId + ");");
	}

	static synchronized void modifyTrip(Trip trip) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String query = "UPDATE trips "
						+ "SET "
						+ "tripTitle = '" + trip.getTitle() + "', "
						+ "tripDescription = '" + trip.getDescription() + "', "
						+ "tripPrice = " + trip.getPrice() + ", "
						+ "timeStart = '" + trip.getTimeStart().format(formatter) + "', "
						+ "locationID = " + trip.getLocation().getId() + ", "
						+ "tripAddress = '" + trip.getMeetingAddress() + "', "
						+ "participantLimit = " + trip.getParticipantLimit() + ", "
						+ "userID = " + trip.getOrganizer().getId() + " "
						+ "WHERE tripID = " + trip.getId() + ";";

		dbm.executeUpdate(query);

		if (!trip.getCategories().isEmpty()) {
			query = "DELETE FROM CategoriesInTrips WHERE tripID = " + trip.getId() + ";";
			dbm.executeUpdate(query);

			for (Category category : trip.getCategories()) {
				query = "INSERT INTO CategoriesInTrips (tripID, CategoryID) VALUES (" + trip.getId() + ", " + category.getId() + ");";
				dbm.executeUpdate(query);
			}
		}

		if (!trip.getParticipants().isEmpty()) {
			query = "DELETE FROM UsersInTrips WHERE tripID = " + trip.getId() + ";";
			dbm.executeUpdate(query);

			for (User participant : trip.getParticipants()) {
				query = "INSERT INTO UsersInTrips (tripID, UserID) VALUES (" + trip.getId() + ", " + participant.getId() + ");";
				dbm.executeUpdate(query);
			}
		}

		//Need to be able to modify images
	}

	/**
	 * This method handles searching for trips, by building a SQL query from the
	 * given parameters.
	 *
	 * @param searchTitle used for a regex.
	 * @param categories
	 * @param timedateStart
	 * @param location
	 * @param priceMAX
	 * @param tripType
	 * @return list of trips matching search parameters.
	 */
	public static List<Trip> searchTrip(String searchTitle, List<Category> categories, LocalDate timedateStart, int location, double priceMAX, String tripType) {

		//Initializes the query string
		String query = "SELECT DISTINCT Trips.tripID, tripTitle, tripDescription, tripPrice "
						+ "FROM Trips, CategoriesInTrips, InstructorsInTrips "
						+ "WHERE timeStart >= NOW()";

		//These if statements checks if the different parameters are used, and adds the necessary SQL code to the query string
		if (!(searchTitle == null || searchTitle.isEmpty())) {
			query += " AND LOWER(tripTitle) LIKE '%" + searchTitle.toLowerCase() + "%'";
		}

		if (categories != null) {
			query += " AND Trips.tripID = CategoriesInTrips.tripid AND CategoriesInTrips.categoryID IN (";

			for (Category category : categories) {
				query += category.getId() + ",";
			}
			query = query.substring(0, query.length() - 1) + ")";
		}

		if (timedateStart != null) {
			query += " AND timeStart >= '" + timedateStart + "'";
		}

		if (location > 0) {
			query += " AND locationID = '" + location + "'";
		}

		if (priceMAX >= 0) {
			query += " AND tripPrice <= '" + priceMAX + "'";
		}

		if (tripType.equals("NORMAL")) {
			query += " AND NOT InstructorsInTrips.tripID = trips.tripID";
		} else if (tripType.equals("INSTRUCTOR")) {
			query += " AND InstructorsInTrips.tripID = trips.tripID";
		}

		//Initializes a ResultSet and an ArrayList for handling the creation of trips to be returned
		ResultSet searchResult = dbm.executeQuery(query);
		ArrayList<Trip> searchResultTrips = new ArrayList<>();

		try {
			if (searchResult == null) {
				throw new SQLException();
			}
			while (searchResult.next()) {
				int id = searchResult.getInt("tripID");
				String title = searchResult.getString("tripTitle");
				String description = searchResult.getString("tripDescription");
				double price = searchResult.getDouble("tripPrice");

				searchResultTrips.add(new Trip(id, title, description, price));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		//Gets the first image of each trip
		String imageQuery = "SELECT tripID, imageFile "
						+ "FROM Images "
						+ "NATURAL JOIN ImagesInTrips "
						+ "WHERE ImagesInTrips.imageID IN (SELECT MIN(imageid) FROM imagesintrips GROUP BY tripID) "
						+ "AND tripID IN (null";

		for (Trip trip : searchResultTrips) {
			imageQuery += ", " + trip.getId();
		}
		imageQuery += ");";

		ResultSet imageRs = dbm.executeQuery(imageQuery);
		HashMap<Integer, Image> tripImages = new HashMap<>();

		try {
			while (imageRs.next()) {
				int id = imageRs.getInt("tripID");
				byte[] imageFile = imageRs.getBytes("imageFile");

				tripImages.put(id, new Image(imageFile));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		for (Trip trip : searchResultTrips) {
			Image image = tripImages.get(trip.getId());
			if (image != null) {
				trip.addImage(image);
			}
		}

		return searchResultTrips;
	}

	public synchronized static void deleteTrip(int tripId, int organizerId) {
		String query = "DELETE "
						+ "FROM Conversations "
						+ "WHERE conversationID = ("
						+ "    SELECT conversationID "
						+ "    FROM Conversations "
						+ "    NATURAL JOIN Trips "
						+ "    WHERE TripID = " + tripId + " AND userID = " + organizerId + ");";

		dbm.executeUpdate(query);
	}

	public synchronized static void participateInTrip(Trip trip, User user) throws FullTripException {
		int tripID = trip.getId();
		int userID = user.getId();

		if (!isTripFull(trip)) {
			addParticipant(tripID, userID);
			System.out.println("Joined trip");
		} else {
			throw new FullTripException("Trip is full.");
		}
	}

	public static void kickParticipant(Trip trip, int userId) {
		int tripId = trip.getId();
		String query = "DELETE FROM UsersInTrips WHERE tripID = " + tripId + "AND userID = " + userId + ";";
		dbm.executeUpdate(query);
	}

	public static Trip showTrip(int tripsID) {
		String query = "SELECT * FROM Trips WHERE tripID = " + tripsID + ";";
		Trip trip = (Trip) dbm.executeQuery(query);
		return trip;
	}

	public static Trip viewTrip(int tripID) {
		String query = "SELECT * FROM Trips WHERE tripID = " + tripID + ";";
		ResultSet rs = dbm.executeQuery(query);

		try {
			if (rs.next()) {
				int id = rs.getInt("tripID");
				String title = rs.getString("tripTitle");
				String description = rs.getString("tripDescription");
				double price = Double.parseDouble(rs.getString("tripPrice"));

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime date = LocalDateTime.parse(rs.getString("timeStart"), formatter);

				String address = rs.getString("tripAddress");
				Location location = getLocation(rs.getInt("locationId"));
				int participantLimit = rs.getInt("participantLimit");
				User organizer = getUserView(rs.getInt("userID"));

				List<Category> categories = getCategoriesInTrip(id).isEmpty() ? null : getCategoriesInTrip(id);
				List<Image> images = getImagesInTrip(id);

				List<User> participants = getTripParticipants(tripID);

				return new Trip(id, title, description, price, date, address, location, participantLimit, organizer, categories, images, participants);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private static List<Category> getCategoriesInTrip(int id) {
		String query = "SELECT categoryID, categoryName FROM CategoriesInTrips NATURAL JOIN Categories WHERE tripID = " + id + ";";
		ResultSet rs = dbm.executeQuery(query);

		ArrayList<Category> categories = new ArrayList<>();

		try {
			while (rs.next()) {
				categories.add(new Category(rs.getInt("categoryID"), rs.getString("categoryName")));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return categories;
	}

	private static List<Image> getImagesInTrip(int id) {
		String query = "SELECT imageID, imageTitle, imageFile FROM ImagesInTrips NATURAL JOIN Images WHERE tripID = " + id + ";";
		ResultSet rs = dbm.executeQuery(query);

		ArrayList<Image> images = new ArrayList<>();

		try {
			while (rs.next()) {
				int imageId = rs.getInt("imageID");
				String imageTitle = rs.getString("imageTitle");
				byte[] imageFile = rs.getBytes("imageFile");

				images.add(new Image(imageId, imageTitle, imageFile));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return images;
	}

	private static Location getLocation(int id) {
		String query = "SELECT locationID, locationName FROM Locations WHERE locationID = " + id + ";";
		ResultSet rs = dbm.executeQuery(query);

		try {
			if (rs.next()) {
				return new Location(rs.getInt("locationID"), rs.getString("locationName"));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private static User getUserView(int id) {
		String query = "SELECT userID, userName FROM Users WHERE userID = " + id + ";";
		ResultSet rs = dbm.executeQuery(query);

		try {
			if (rs.next()) {
				return new User(rs.getInt("userID"), rs.getString("userName"));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static boolean isTripFull(Trip trip) {
		try {
			ResultSet fullTripCheck = dbm.executeQuery("SELECT UsersInTrips.tripId, COUNT(UsersInTrips.tripId), MAX(participantLimit)\n"
							+ "FROM UsersInTrips\n"
							+ "INNER JOIN Trips On UsersInTrips.tripId = Trips.tripID\n"
							+ "WHERE UsersInTrips.tripId = " + trip.getId() + "\n"
							+ "GROUP BY UsersInTrips.tripId");
			if (fullTripCheck.next()) {
				int usersInTrip = (int) fullTripCheck.getLong("count");
				int participantLimit = fullTripCheck.getInt("max");
				System.out.println(usersInTrip >= participantLimit);
				return usersInTrip >= participantLimit;
			} else {
				return false;
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public static List<Trip> getMyTrips(User user) {
		try {
			String query = ""
							+ "SELECT trips.tripid, triptitle, tripdescription, tripprice, imagefile\n"
							+ "FROM trips\n"
							+ "INNER JOIN usersInTrips ON trips.tripid = usersInTrips.tripid\n"
							+ "INNER JOIN imagesInTrips ON imagesInTrips.tripid = trips.tripid\n"
							+ "INNER JOIN images ON images.imageid = imagesInTrips.imageid\n"
							+ "WHERE usersInTrips.userid = " + user.getId() + " AND imagesintrips.imageid IN (SELECT MIN(imageid) FROM imagesintrips GROUP BY tripid) AND timeStart >= NOW()";
			List<Trip> myTrips = new ArrayList<>();

			ResultSet tripsRs = dbm.executeQuery(query);

			while (tripsRs.next()) {
				int id = tripsRs.getInt(1);
				String title = tripsRs.getString(2);
				String description = tripsRs.getString(3);
				double price = tripsRs.getDouble(4);
				byte[] imageFile = tripsRs.getBytes(5);
				myTrips.add(new Trip(id, title, description, price, new Image(imageFile)));
			}

			return myTrips;
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static List<Trip> getMyTrips(User user, int organizerId) {
		try {
			String query = ""
							+ "SELECT trips.tripid, triptitle, tripdescription, tripprice, imagefile\n"
							+ "FROM trips\n"
							+ "INNER JOIN usersInTrips ON trips.tripid = usersInTrips.tripid\n"
							+ "INNER JOIN imagesInTrips ON imagesInTrips.tripid = trips.tripid\n"
							+ "INNER JOIN images ON images.imageid = imagesInTrips.imageid\n"
							+ "WHERE usersInTrips.userid = " + user.getId() + " AND imagesintrips.imageid IN (SELECT MIN(imageid) FROM imagesintrips GROUP BY tripid) AND trips.userID = " + organizerId;
			List<Trip> myTrips = new ArrayList<>();

			ResultSet tripsRs = dbm.executeQuery(query);

			while (tripsRs.next()) {
				int id = tripsRs.getInt(1);
				String title = tripsRs.getString(2);
				String description = tripsRs.getString(3);
				double price = tripsRs.getDouble(4);
				byte[] imageFile = tripsRs.getBytes(5);
				myTrips.add(new Trip(id, title, description, price, new Image(imageFile)));
			}

			return myTrips;
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static int getConversationId(int tripId) {
		String query = ""
						+ "SELECT conversationId "
						+ "FROM Trips "
						+ "WHERE tripId = " + tripId;

		ResultSet conversationIdRs = dbm.executeQuery(query);

		try {
			if (conversationIdRs.next()) {
				return conversationIdRs.getInt("conversationId");
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return -1;
	}

	public static List<User> getTripParticipants(int tripId) {
		String query = ""
						+ "SELECT *\n"
						+ "FROM Users\n"
						+ "WHERE userID IN (\n"
						+ "    SELECT UsersInTrips.userID \n"
						+ "    FROM Trips\n"
						+ "    INNER JOIN UsersInTrips ON Trips.tripID = UsersInTrips.tripID\n"
						+ "    WHERE Trips.tripID = " + tripId + ")";

		ResultSet participantsRs = dbm.executeQuery(query);

		List<User> participants = new ArrayList<>();

		try {
			while (participantsRs.next()) {
				int id = participantsRs.getInt("userID");
				String userName = participantsRs.getString("userName");

				participants.add(new User(id, userName));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return participants;
	}

}
