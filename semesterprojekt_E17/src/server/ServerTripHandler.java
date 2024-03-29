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
 * ServerTripHandler is responsible for managing all trip related tasks in the
 * system. The class handles creation of trip objects to be sent to the client
 * and storage of trip objects in the database received from the client. This
 * class also handles searching for trips and other trip related tasks. The
 * Server Trip Handler communicates with the Database Manager.
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
			throw new IllegalArgumentException("Category does not exist.");
		}

		//Checks if location id exists
		if (!locationExists(newTrip.getLocation())) {
			throw new IllegalArgumentException("Location does not exist.");
		}

		//Checks if organizer id exists
		if (!userExists(newTrip.getOrganizer())) {
			throw new IllegalArgumentException("Organizer does not exist.");
		}

		//Checks if organizer have the required certificates
		if (newTrip.getInstructors() != null) {
			for (InstructorListItem instructorListItem : newTrip.getInstructors()) {
				if (!certificateExists(instructorListItem)) {
					throw new IllegalArgumentException("Certificate does not exist.");
				}
			}
		}

		//Checks if values are valid and then converts values to SQL values
		if (newTrip.getTitle() == null || newTrip.getTitle().isEmpty()) {
			throw new IllegalArgumentException("Trip title is null or empty.");
		}
		String sqlTripTitle = "'" + newTrip.getTitle() + "'";

		String sqlTripDescription;
		if (newTrip.getDescription() == null || newTrip.getDescription().isEmpty()) {
			sqlTripDescription = "null";
		} else {
			sqlTripDescription = "'" + newTrip.getDescription() + "'";
		}

		if (newTrip.getPrice() < 0) {
			throw new IllegalArgumentException("Trip price is less than zero.");
		}
		String sqlTripPrice = newTrip.getPrice() + "";

		if (newTrip.getTimeStart() == null || newTrip.getTimeStart().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Trip timeStart is null or before now.");
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String sqlTimeStart = "'" + newTrip.getTimeStart().format(formatter) + "'";

		String sqlTripAddress;
		if (newTrip.getMeetingAddress() == null || newTrip.getMeetingAddress().isEmpty()) {
			throw new IllegalArgumentException("Trip meeting address is null or empty.");
		} else {
			sqlTripAddress = "'" + newTrip.getMeetingAddress() + "'";
		}

		String sqlParticipantLimit;
		if (newTrip.getParticipantLimit() < 0) {
			throw new IllegalArgumentException("Participant limit is less than zero.");
		}
		if (newTrip.getParticipantLimit() == 0) {
			sqlParticipantLimit = "null";
		} else {
			sqlParticipantLimit = newTrip.getParticipantLimit() + "";
		}

		//Creates the group conversation and returnes the conversation id. -1 is returned if it failed
		int sqlConversationId = ServerMessagingHandler.addConversation("trip");

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
				addInstructorToTrip(tripId, instructorListItem.getUser().getId(), instructorListItem.getCategory().getId());
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
					addImageToTrip(tripId, image);
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
				locations.add(new Location(rs.getInt("locationID"), rs.getString("locationName")));
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
				locationIds.add(rs.getInt("locationID"));
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

	private static void addInstructorToTrip(int tripId, int userId, int categoryId) {
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

	private static void addImageToTrip(int tripId, Image image) {
		String sqlImageTitle;
		if (image.getTitle() == null || image.getTitle().isEmpty()) {
			sqlImageTitle = "null";
		} else {
			sqlImageTitle = "'" + image.getTitle() + "'";
		}

		int imageId = dbm.executeImageInsertAndGetId("INSERT INTO Images (imageID, imageTitle, imageFile) VALUES (DEFAULT, " + sqlImageTitle + ", ?);", image.getImageFile());
		dbm.executeUpdate("INSERT INTO ImagesInTrips (imageID, tripID) VALUES (" + imageId + ", " + tripId + ");");
	}

	public static synchronized void modifyTrip(Trip trip) {
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
	 * @param timeDateStart
	 * @param locationId
	 * @param priceMAX
	 * @param tripType
	 * @return list of trips matching search parameters.
	 */
	public static List<Trip> searchTrip(String searchTitle, List<Category> categories, LocalDate timeDateStart, int locationId, double priceMAX, String tripType) {

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

		if (timeDateStart != null) {
			query += " AND timeStart >= '" + timeDateStart + "'";
		}

		if (locationId > 0) {
			query += " AND locationID = '" + locationId + "'";
		}

		if (priceMAX >= 0) {
			query += " AND tripPrice <= '" + priceMAX + "'";
		}

		if (tripType.equals("NORMAL")) {
			query += " AND NOT trips.tripID IN (SELECT tripID FROM InstructorsInTrips)";
		} else if (tripType.equals("INSTRUCTOR")) {
			query += " AND trips.tripID IN (SELECT tripID FROM InstructorsInTrips)";
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

				Trip trip = new Trip(id, title, description, price);
				trip.setInstructors(getTripInstructors(id));

				searchResultTrips.add(trip);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		attachTripThumbnail(searchResultTrips);

		return searchResultTrips;
	}

	private static void attachTripThumbnail(List<Trip> trips) {
		//Gets the first image of each trip
		String imageQuery = "SELECT tripID, imageID, imageTitle, imageFile "
						+ "FROM Images "
						+ "NATURAL JOIN ImagesInTrips "
						+ "WHERE ImagesInTrips.imageID IN (SELECT MIN(imageid) FROM imagesintrips GROUP BY tripID) "
						+ "AND tripID IN (null";

		for (Trip trip : trips) {
			imageQuery += ", " + trip.getId();
		}
		imageQuery += ");";

		ResultSet imageRs = dbm.executeQuery(imageQuery);
		HashMap<Integer, Image> tripImages = new HashMap<>();

		try {
			while (imageRs.next()) {
				int id = imageRs.getInt("tripID");
				int imageId = imageRs.getInt("imageID");
				String imageTitle = imageRs.getString("imageTitle");
				byte[] imageFile = imageRs.getBytes("imageFile");

				tripImages.put(id, new Image(imageId, imageTitle, imageFile));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		for (Trip trip : trips) {
			Image image = tripImages.get(trip.getId());
			if (image != null) {
				trip.addImage(image);
			}
		}
	}

	public synchronized static void deleteTrip(int tripId, int organizerId) {
		String query = "DELETE FROM Conversations "
						+ "WHERE conversationID = ("
						+ "    SELECT conversationID "
						+ "    FROM Conversations "
						+ "    NATURAL JOIN Trips "
						+ "    WHERE TripID = " + tripId + " AND userID = " + organizerId + ");";

		dbm.executeUpdate(query);
	}

	public synchronized static void participateInTrip(int tripId, int userId) throws FullTripException {
		if (!isTripFull(tripId)) {
			addParticipant(tripId, userId);
		} else {
			throw new FullTripException("Trip is full.");
		}
	}

	public static void kickParticipant(Trip trip, int userId) {
		int tripId = trip.getId();
		String query = "DELETE FROM UsersInTrips WHERE tripID = " + tripId + "AND userID = " + userId + ";";
		dbm.executeUpdate(query);
	}

	public static Trip showTrip(int tripId) {
		String query = "SELECT * FROM Trips WHERE tripID = " + tripId + ";";
		Trip trip = (Trip) dbm.executeQuery(query);
		return trip;
	}

	public static Trip viewTrip(int tripId) {
		String query = "SELECT * FROM Trips WHERE tripID = " + tripId + ";";
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

				List<User> participants = getTripParticipants(tripId);
				List<InstructorListItem> instructors = getTripInstructors(tripId);

				Trip trip = new Trip(id, title, description, price, date, address, location, participantLimit, organizer, categories, images, participants);
				trip.setInstructors(instructors);

				return trip;
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private static List<Category> getCategoriesInTrip(int tripId) {
		String query = "SELECT categoryID, categoryName FROM CategoriesInTrips NATURAL JOIN Categories WHERE tripID = " + tripId + ";";
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

	private static Location getLocation(int locationId) {
		String query = "SELECT locationID, locationName FROM Locations WHERE locationID = " + locationId + ";";
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

	private static User getUserView(int userId) {
		String query = "SELECT userID, userName FROM Users WHERE userID = " + userId + ";";
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

	public static boolean isTripFull(int tripId) {
		try {
			ResultSet fullTripCheck = dbm.executeQuery("SELECT UsersInTrips.tripID, COUNT(UsersInTrips.tripID), MAX(participantLimit) "
							+ "FROM UsersInTrips "
							+ "INNER JOIN Trips On UsersInTrips.tripID = Trips.tripID "
							+ "WHERE UsersInTrips.tripID = " + tripId + " "
							+ "GROUP BY UsersInTrips.tripID");
			if (fullTripCheck.next()) {
				int usersInTrip = (int) fullTripCheck.getLong("count");
				int participantLimit = fullTripCheck.getInt("max");
				return usersInTrip >= participantLimit;
			} else {
				return false;
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	public static List<Trip> getMyTrips(int userId) {
		List<Trip> myTrips = new ArrayList<>();

		String query = "SELECT Trips.tripID, tripTitle, tripDescription, tripPrice "
						+ "FROM Trips "
						+ "INNER JOIN UsersInTrips ON Trips.tripID = UsersInTrips.tripID "
						+ "WHERE timeStart >= NOW() "
						+ "AND UsersInTrips.userID = " + userId + ";";

		ResultSet tripsRs = dbm.executeQuery(query);

		try {
			while (tripsRs.next()) {
				int id = tripsRs.getInt("tripID");
				String title = tripsRs.getString("tripTitle");
				String description = tripsRs.getString("tripDescription");
				double price = tripsRs.getDouble("tripPrice");
				myTrips.add(new Trip(id, title, description, price));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		attachTripThumbnail(myTrips);

		return myTrips;
	}

	public static List<Trip> getMyOrganizedTrips(int userId) {
		List<Trip> myTrips = new ArrayList<>();

		String query = "SELECT tripID, tripTitle, tripDescription, tripPrice "
						+ "FROM Trips "
						+ "WHERE userID = " + userId + ";";

		ResultSet tripsRs = dbm.executeQuery(query);

		try {
			while (tripsRs.next()) {
				int id = tripsRs.getInt("tripID");
				String title = tripsRs.getString("tripTitle");
				String description = tripsRs.getString("tripDescription");
				double price = tripsRs.getDouble("tripPrice");
				myTrips.add(new Trip(id, title, description, price));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		attachTripThumbnail(myTrips);

		return myTrips;
	}

	public static int getConversationId(int tripId) {
		String query = "SELECT conversationID "
						+ "FROM Trips "
						+ "WHERE tripID = " + tripId + ";";

		ResultSet conversationIdRs = dbm.executeQuery(query);

		try {
			if (conversationIdRs.next()) {
				return conversationIdRs.getInt("conversationID");
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return -1;
	}

	public static List<User> getTripParticipants(int tripId) {
		String query = ""
						+ "SELECT * "
						+ "FROM Users "
						+ "WHERE userID IN ("
						+ "    SELECT UsersInTrips.userID "
						+ "    FROM Trips "
						+ "    INNER JOIN UsersInTrips ON Trips.tripID = UsersInTrips.tripID "
						+ "    WHERE Trips.tripID = " + tripId + ");";

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

	private static List<InstructorListItem> getTripInstructors(int tripId) {
		String query = ""
						+ "SELECT userID, userName, categoryID, categoryName "
						+ "FROM Users "
						+ "NATURAL JOIN Certificates "
						+ "NATURAL JOIN Categories "
						+ "WHERE userID IN ("
						+ "    SELECT InstructorsInTrips.userID "
						+ "    FROM Trips "
						+ "    NATURAL JOIN InstructorsInTrips "
						+ "    WHERE Trips.tripID = " + tripId + ");";

		ResultSet instructorsRs = dbm.executeQuery(query);

		List<InstructorListItem> instructors = new ArrayList<>();

		try {
			while (instructorsRs.next()) {
				int userId = instructorsRs.getInt("userID");
				String userName = instructorsRs.getString("userName");
				int categoryId = instructorsRs.getInt("categoryID");
				String categoryName = instructorsRs.getString("categoryName");

				User user = new User(userId, userName);
				Category category = new Category(categoryId, categoryName);

				instructors.add(new InstructorListItem(user, category));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return instructors;
	}

}
