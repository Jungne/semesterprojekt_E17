package database;

import java.sql.SQLException;

public class DatabaseSetup {

	private static DBManager dbm;

	private static String createCategoriesQuery
					= "CREATE TABLE IF NOT EXISTS Categories ("
					+ "categoryID serial, "
					+ "categoryName varchar(255), "
					+ "PRIMARY KEY (categoryID)"
					+ ");";

	private static String createLocationsQuery
					= "CREATE TABLE IF NOT EXISTS Locations ("
					+ "locationID serial, "
					+ "locationName varchar(255), "
					+ "PRIMARY KEY (locationID)"
					+ ");";

	private static String createImagesQuery
					= "CREATE TABLE IF NOT EXISTS Images ("
					+ "imageID serial, "
					+ "imageTitle varchar(255), "
					+ "imageFile bytea, "
					+ "PRIMARY KEY (imageID)"
					+ ");";

	private static String createUsersQuery
					= "CREATE TABLE IF NOT EXISTS Users ("
					+ "userID serial, "
					+ "email varchar(255) NOT NULL UNIQUE, "
					+ "password varchar(255), "
					+ "userName varchar(255), "
					+ "imageID int, "
					+ "PRIMARY KEY (userID), "
					+ "FOREIGN KEY (imageID) REFERENCES Images(imageID)"
					+ ");";

	private static String createInstructorsQuery
					= "CREATE TABLE IF NOT EXISTS Instructors ("
					+ "userID int, "
					+ "categoryID int, "
					+ "PRIMARY KEY (userID, categoryID), "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID), "
					+ "FOREIGN KEY (categoryID) REFERENCES Images(categoryID)"
					+ ");";

	private static String createUsersInConversationsQuery
					= "CREATE TABLE IF NOT EXISTS UsersInConversations ("
					+ "conversationID int, "
					+ "userID int, "
					+ "PRIMARY KEY (conversationID, userID), "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID)"
					+ ");";

	private static String createMessagesQuery
					= "CREATE TABLE IF NOT EXISTS Messages ("
					+ "messageID serial, "
					+ "conversationID int, "
					+ "message varchar(255), "
					+ "userID int, "
					+ "time timestamp, "
					+ "PRIMARY KEY (messageID), "
					+ "FOREIGN KEY (conversationID) REFERENCES UsersInConversations(conversationID), "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID)"
					+ ");";

	private static String createTripsQuery
					= "CREATE TABLE IF NOT EXISTS Trips ("
					+ "tripID serial, "
					+ "tripTitle varchar(255), "
					+ "description varchar(255), "
					+ "userID int, "
					+ "timeStart timestamp, "
					+ "timeEnd timestamp, "
					+ "tripAddress varchar(255), "
					+ "locationID int, "
					+ "participantLimit int, "
					+ "conversationID int, "
					+ "PRIMARY KEY (tripID), "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID), "
					+ "FOREIGN KEY (locationID) REFERENCES Locations(locationID), "
					+ "FOREIGN KEY (conversationID) REFERENCES UsersInConversations(conversationID)"
					+ ");";

	private static String createUsersInTripsQuery
					= "CREATE TABLE IF NOT EXISTS UsersInTrips ("
					+ "tripID int, "
					+ "userID int, "
					+ "PRIMARY KEY (tripID, userID), "
					+ "FOREIGN KEY (tripID) REFERENCES Trips(tripID), "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID)"
					+ ");";

	private static String createInstructorsInTripsQuery
					= "CREATE TABLE IF NOT EXISTS InstructorsInTrips ("
					+ "tripID int, "
					+ "userID int, "
					+ "categoryID int, "
					+ "PRIMARY KEY (tripID, userID, categoryID), "
					+ "FOREIGN KEY (tripID) REFERENCES Trips(tripID), "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID), "
					+ "FOREIGN KEY (categoryID) REFERENCES Categories(categoryID)"
					+ ");";

	private static String createPricesQuery
					= "CREATE TABLE IF NOT EXISTS Prices ("
					+ "priceID serial, "
					+ "tripID int, "
					+ "price decimal(10,2), "
					+ "description varchar(255), "
					+ "mandatory bit NOT NULL, "
					+ "PRIMARY KEY (priceID), "
					+ "FOREIGN KEY (tripID) REFERENCES trips(tripID)"
					+ ");";

	private static String createCategoriesInTripsQuery
					= "CREATE TABLE IF NOT EXISTS CategoriesInTrips ("
					+ "tripID int, "
					+ "categoryID int, "
					+ "PRIMARY KEY (tripID, categoryID), "
					+ "FOREIGN KEY (tripID) REFERENCES Trips(tripID), "
					+ "FOREIGN KEY (categoryID) REFERENCES Categories(categoryID)"
					+ ");";

	private static String createTagsInTripsQuery
					= "CREATE TABLE IF NOT EXISTS TagsInTrips ("
					+ "tripID int, "
					+ "tag varchar(255) NOT NULL, "
					+ "PRIMARY KEY (tripID, tag), "
					+ "FOREIGN KEY (tripID) REFERENCES Trips(tripID)"
					+ ");";

	private static String createImagesIntripsQuery
					= "CREATE TABLE IF NOT EXISTS ImagesIntrips ("
					+ "imageID int, "
					+ "tripID int, "
					+ "PRIMARY KEY (imageID), "
					+ "FOREIGN KEY (imageID) REFERENCES Images(imageID)"
					+ ");";

	protected static String[] createTableQueries = {
		createCategoriesQuery,
		createLocationsQuery,
		createImagesQuery,
		createUsersQuery,
		createInstructorsQuery,
		createUsersInConversationsQuery,
		createMessagesQuery,
		createTripsQuery,
		createUsersInTripsQuery,
		createInstructorsInTripsQuery,
		createPricesQuery,
		createCategoriesInTripsQuery,
		createTagsInTripsQuery,
		createImagesIntripsQuery
	};

	public static void main(String[] args) throws SQLException {
		dbm = DBManager.getInstance();
		for (String query : createTableQueries) {
			dbm.executeUpdate(query);
		}
	}
}
