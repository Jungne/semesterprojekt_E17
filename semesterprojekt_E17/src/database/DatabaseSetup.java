package database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSetup {

	private static DBManager dbm = DBManager.getInstance();

	private static String createCategoriesQuery
					= "CREATE TABLE IF NOT EXISTS Categories ("
					+ "categoryID serial, "
					+ "categoryName varchar(255) NOT NULL UNIQUE, "
					+ "PRIMARY KEY (categoryID)"
					+ ");";

	private static String createLocationsQuery
					= "CREATE TABLE IF NOT EXISTS Locations ("
					+ "locationID serial, "
					+ "locationName varchar(255) NOT NULL UNIQUE, "
					+ "PRIMARY KEY (locationID)"
					+ ");";

	private static String createImagesQuery
					= "CREATE TABLE IF NOT EXISTS Images ("
					+ "imageID serial, "
					+ "imageTitle varchar(255), "
					+ "imageFile bytea NOT NULL, "
					+ "PRIMARY KEY (imageID)"
					+ ");";

	private static String createUsersQuery
					= "CREATE TABLE IF NOT EXISTS Users ("
					+ "userID serial, "
					+ "email varchar(255) NOT NULL UNIQUE, "
					+ "password varchar(255) NOT NULL, "
					+ "userName varchar(255) NOT NULL, "
					+ "imageID int, "
					+ "PRIMARY KEY (userID), "
					+ "FOREIGN KEY (imageID) REFERENCES Images(imageID)"
					+ ");";

	private static String createCertificatesQuery
					= "CREATE TABLE IF NOT EXISTS Certificates ("
					+ "userID int, "
					+ "categoryID int, "
					+ "PRIMARY KEY (userID, categoryID), "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID), "
					+ "FOREIGN KEY (categoryID) REFERENCES Categories(categoryID)"
					+ ");";

	private static String createConversationsQuery
					= "CREATE TABLE IF NOT EXISTS Conversations ("
					+ "conversationID serial, "
					+ "type varchar(255), "
					+ "PRIMARY KEY (conversationID)"
					+ ");";

	private static String createUsersInConversationsQuery
					= "CREATE TABLE IF NOT EXISTS UsersInConversations ("
					+ "conversationID int, "
					+ "userID int, "
					+ "PRIMARY KEY (conversationID, userID), "
					+ "FOREIGN KEY (conversationID) REFERENCES Conversations(conversationID) ON DELETE CASCADE, "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID)"
					+ ");";

	private static String createMessagesQuery
					= "CREATE TABLE IF NOT EXISTS Messages ("
					+ "messageID serial, "
					+ "conversationID int, "
					+ "userID int, "
					+ "message varchar(255), "
					+ "time timestamp, "
					+ "PRIMARY KEY (messageID), "
					+ "FOREIGN KEY (conversationID) REFERENCES Conversations(conversationID) ON DELETE CASCADE, "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID)"
					+ ");";

	private static String createTripsQuery
					= "CREATE TABLE IF NOT EXISTS Trips ("
					+ "tripID serial, "
					+ "tripTitle varchar(255) NOT NULL, "
					+ "tripDescription varchar(255), "
					+ "tripPrice decimal(10,2), "
					+ "timeStart timestamp, "
					+ "locationID int, "
					+ "tripAddress varchar(255) NOT NULL, "
					+ "participantLimit int, "
					+ "userID int NOT NULL, "
					+ "conversationID int NOT NULL, "
					+ "PRIMARY KEY (tripID), "
					+ "FOREIGN KEY (locationID) REFERENCES Locations(locationID), "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID), "
					+ "FOREIGN KEY (conversationID) REFERENCES Conversations(conversationID) ON DELETE CASCADE"
					+ ");";

	private static String createCategoriesInTripsQuery
					= "CREATE TABLE IF NOT EXISTS CategoriesInTrips ("
					+ "tripID int, "
					+ "categoryID int, "
					+ "PRIMARY KEY (categoryID, tripID), "
					+ "FOREIGN KEY (tripID) REFERENCES Trips(tripID) ON DELETE CASCADE, "
					+ "FOREIGN KEY (categoryID) REFERENCES Categories(categoryID)"
					+ ");";

	private static String createUsersInTripsQuery
					= "CREATE TABLE IF NOT EXISTS UsersInTrips ("
					+ "tripID int, "
					+ "userID int, "
					+ "PRIMARY KEY (tripID, userID), "
					+ "FOREIGN KEY (tripID) REFERENCES Trips(tripID) ON DELETE CASCADE, "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID)"
					+ ");";

	private static String createInstructorsInTripsQuery
					= "CREATE TABLE IF NOT EXISTS InstructorsInTrips ("
					+ "tripID int, "
					+ "userID int, "
					+ "categoryID int, "
					+ "PRIMARY KEY (tripID, userID, categoryID), "
					+ "FOREIGN KEY (tripID) REFERENCES Trips(tripID) ON DELETE CASCADE, "
					+ "FOREIGN KEY (userID) REFERENCES Users(userID), "
					+ "FOREIGN KEY (categoryID) REFERENCES Categories(categoryID)"
					+ ");";

	private static String createOptionalPricesQuery
					= "CREATE TABLE IF NOT EXISTS OptionalPrices ("
					+ "priceID serial, "
					+ "tripID int NOT NULL, "
					+ "optionalPrice decimal(10,2) NOT NULL, "
					+ "priceDescription varchar(255) NOT NULL, "
					+ "PRIMARY KEY (priceID), "
					+ "FOREIGN KEY (tripID) REFERENCES Trips(tripID) ON DELETE CASCADE"
					+ ");";

	private static String createTagsInTripsQuery
					= "CREATE TABLE IF NOT EXISTS TagsInTrips ("
					+ "tripID int, "
					+ "tag varchar(255), "
					+ "PRIMARY KEY (tripID, tag), "
					+ "FOREIGN KEY (tripID) REFERENCES Trips(tripID) ON DELETE CASCADE"
					+ ");";

	private static String createImagesIntripsQuery
					= "CREATE TABLE IF NOT EXISTS ImagesIntrips ("
					+ "imageID int, "
					+ "tripID int, "
					//					+ "PRIMARY KEY (imageID, tripID), "
					+ "FOREIGN KEY (imageID) REFERENCES Images(imageID), "
					+ "FOREIGN KEY (tripID) REFERENCES Trips(tripID) ON DELETE CASCADE"
					+ ");";

	protected static String[] createTableQueries = {
		createCategoriesQuery,
		createLocationsQuery,
		createImagesQuery,
		createUsersQuery,
		createCertificatesQuery,
		createConversationsQuery,
		createUsersInConversationsQuery,
		createMessagesQuery,
		createTripsQuery,
		createCategoriesInTripsQuery,
		createUsersInTripsQuery,
		createInstructorsInTripsQuery,
		createOptionalPricesQuery,
		createTagsInTripsQuery,
		createImagesIntripsQuery
	};

	public static String[] dropTableQueries = {
		"DROP TABLE IF EXISTS ImagesIntrips;",
		"DROP TABLE IF EXISTS TagsInTrips;",
		"DROP TABLE IF EXISTS OptionalPrices;",
		"DROP TABLE IF EXISTS InstructorsInTrips;",
		"DROP TABLE IF EXISTS UsersInTrips;",
		"DROP TABLE IF EXISTS CategoriesInTrips;",
		"DROP TABLE IF EXISTS Trips;",
		"DROP TABLE IF EXISTS Messages;",
		"DROP TABLE IF EXISTS UsersInConversations;",
		"DROP TABLE IF EXISTS Conversations;",
		"DROP TABLE IF EXISTS Certificates;",
		"DROP TABLE IF EXISTS Users;",
		"DROP TABLE IF EXISTS Images;",
		"DROP TABLE IF EXISTS Locations;",
		"DROP TABLE IF EXISTS Categories;",};

	public static void main(String[] args) throws SQLException, IOException {
		dropTables();
		createTables();
		//addDefaultImages();
		addTempData();
	}

	private static void createTables() {
		for (String query : createTableQueries) {
			dbm.executeUpdate(query);
		}
	}

	private static void dropTables() {
		for (String query : dropTableQueries) {
			dbm.executeUpdate(query);
		}
	}

	private static void addTempData() {
		//Inserts categories
		dbm.executeUpdate("INSERT INTO Categories VALUES (DEFAULT, 'Kayaking');");
		dbm.executeUpdate("INSERT INTO Categories VALUES (DEFAULT, 'Running');");
		dbm.executeUpdate("INSERT INTO Categories VALUES (DEFAULT, 'Climbing');");
		dbm.executeUpdate("INSERT INTO Categories VALUES (DEFAULT, 'Hiking');");
		dbm.executeUpdate("INSERT INTO Categories VALUES (DEFAULT, 'Canoeing');");
		dbm.executeUpdate("INSERT INTO Categories VALUES (DEFAULT, 'Skiing');");
		dbm.executeUpdate("INSERT INTO Categories VALUES (DEFAULT, 'Sailing');");
		dbm.executeUpdate("INSERT INTO Categories VALUES (DEFAULT, 'Backpacking');");
		dbm.executeUpdate("INSERT INTO Categories VALUES (DEFAULT, 'Cycling');");
		
		//Inserts locations
		dbm.executeUpdate("INSERT INTO Locations VALUES (DEFAULT, 'Northern Jutland');");
		dbm.executeUpdate("INSERT INTO Locations VALUES (DEFAULT, 'Central Jutland');");
		dbm.executeUpdate("INSERT INTO Locations VALUES (DEFAULT, 'Southern Jutland');");
		dbm.executeUpdate("INSERT INTO Locations VALUES (DEFAULT, 'Funen');");
		dbm.executeUpdate("INSERT INTO Locations VALUES (DEFAULT, 'Zealand');");
		dbm.executeUpdate("INSERT INTO Locations VALUES (DEFAULT, 'Copenhagen');");
		//Inserts users
		dbm.executeUpdate("INSERT INTO Users VALUES (DEFAULT, 'dalun12@student.sdu.dk', '462ddb9fa125fdac01fe132e057295c3b8fd1946f394b12c382ec4ab43b25cf5', 'Daniel', null);");
		dbm.executeUpdate("INSERT INTO Users VALUES (DEFAULT, 'eitho16@student.sdu.dk', '462ddb9fa125fdac01fe132e057295c3b8fd1946f394b12c382ec4ab43b25cf5', 'Eirikur', null);");
		dbm.executeUpdate("INSERT INTO Users VALUES (DEFAULT, 'hjrin15@student.sdu.dk', '462ddb9fa125fdac01fe132e057295c3b8fd1946f394b12c382ec4ab43b25cf5', 'Hjalte', null);");
		dbm.executeUpdate("INSERT INTO Users VALUES (DEFAULT, 'julos14@student.sdu.dk', '462ddb9fa125fdac01fe132e057295c3b8fd1946f394b12c382ec4ab43b25cf5', 'Jungne', null);");
		dbm.executeUpdate("INSERT INTO Users VALUES (DEFAULT, 'lalun13@student.sdu.dk', '462ddb9fa125fdac01fe132e057295c3b8fd1946f394b12c382ec4ab43b25cf5', 'Lasse', null);");
		dbm.executeUpdate("INSERT INTO Users VALUES (DEFAULT, 'lawar15@student.sdu.dk', '462ddb9fa125fdac01fe132e057295c3b8fd1946f394b12c382ec4ab43b25cf5', 'Laura', null);");
//		dbm.executeUpdate("INSERT INTO Users VALUES (DEFAULT, 'tester', '462ddb9fa125fdac01fe132e057295c3b8fd1946f394b12c382ec4ab43b25cf5', 'Tim', null);");
//		dbm.executeUpdate("INSERT INTO Users VALUES (DEFAULT, 'qwer', '1718c24b10aeb8099e3fc44960ab6949ab76a267352459f203ea1036bec382c2', 'Heinrich', null);");
		dbm.executeUpdate("INSERT INTO Users VALUES (DEFAULT, 'admin', '1718c24b10aeb8099e3fc44960ab6949ab76a267352459f203ea1036bec382c2', 'Admin', null);");
		//Inserts certificates
		dbm.executeUpdate("INSERT INTO Certificates VALUES (1, 1)");
		dbm.executeUpdate("INSERT INTO Certificates VALUES (2, 2)");
		dbm.executeUpdate("INSERT INTO Certificates VALUES (3, 3)");
		dbm.executeUpdate("INSERT INTO Certificates VALUES (4, 3)");
		dbm.executeUpdate("INSERT INTO Certificates VALUES (5, 2)");
		dbm.executeUpdate("INSERT INTO Certificates VALUES (6, 1)");
		//Inserts conversations
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'trip')");
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'trip')");
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'trip')");
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'trip')");
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'trip')");
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'trip')");
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'trip')");
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'trip')");
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'trip')");
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'trip')");
//		dbm.executeUpdate("INSERT INTO Conversations VALUES (DEFAULT, 'users')");
		//Inserts UsersInConversations
//		dbm.executeUpdate("INSERT INTO UsersInConversations VALUES (11,4)");
//		dbm.executeUpdate("INSERT INTO UsersInConversations VALUES (11,5)");
		//Inserts trips
//		dbm.executeUpdate("insert into trips (tripid, triptitle, tripdescription, tripprice, timestart, locationid, tripaddress, participantlimit, userid, conversationid) values (DEFAULT, 'Doc Hollywood', 'Pallor', 70.14, '2016-11-23', 1, '8 Dawn Park', 11, 9, 1);");
//		dbm.executeUpdate("insert into trips (tripid, triptitle, tripdescription, tripprice, timestart, locationid, tripaddress, participantlimit, userid, conversationid) values (DEFAULT, 'Septien', 'Pathological fracture in oth disease, unsp ankle, init', 79.21, '2018-06-19', 1, '5 Bonner Hill', 12, 9, 2);");
//		dbm.executeUpdate("insert into trips (tripid, triptitle, tripdescription, tripprice, timestart, locationid, tripaddress, participantlimit, userid, conversationid) values (DEFAULT, 'Hours', 'Dislocation of unsp interphaln joint of l rng fngr, init', 276.2, '2017-06-07', 3, '829 Sullivan Circle', 14, 9, 3);");
//		dbm.executeUpdate("insert into trips (tripid, triptitle, tripdescription, tripprice, timestart, locationid, tripaddress, participantlimit, userid, conversationid) values (DEFAULT, 'Citizen Gangster ', 'Nail disorders in diseases classified elsewhere', 236.97, '2018-04-15', 1, '17 Dahle Alley', 11, 9, 4);");
//		dbm.executeUpdate("insert into trips (tripid, triptitle, tripdescription, tripprice, timestart, locationid, tripaddress, participantlimit, userid, conversationid) values (DEFAULT, 'Maid in Sweden', 'Unsp inj unsp musc/fasc/tend at thi lev, unsp thigh, sequela', 417.63, '2016-11-17', 2, '73400 Sauthoff Pass', 12, 9, 5);");
//		dbm.executeUpdate("insert into trips (tripid, triptitle, tripdescription, tripprice, timestart, locationid, tripaddress, participantlimit, userid, conversationid) values (DEFAULT, 'Whatever Happened to Harold Smith?', 'Other prurigo', 5.15, '2017-08-29', 1, '93070 Brown Terrace', 17, 9, 6);");
//		dbm.executeUpdate("insert into trips (tripid, triptitle, tripdescription, tripprice, timestart, locationid, tripaddress, participantlimit, userid, conversationid) values (DEFAULT, 'Anything But Love (a.k.a. Standard Time)', 'Oth physl fx upr end rad, unsp arm, subs for fx w delay heal', 233.64, '2017-01-04', 2, '4 Crowley Road', 2, 9, 7);");
//		dbm.executeUpdate("insert into trips (tripid, triptitle, tripdescription, tripprice, timestart, locationid, tripaddress, participantlimit, userid, conversationid) values (DEFAULT, 'Glory to the Filmmaker! (Kantoku · Banzai!)', 'Poisoning by other systemic antibiotics, assault, sequela', 3.55, '2018-12-30', 1, '3 South Crossing', 10, 9, 8);");
//		dbm.executeUpdate("insert into trips (tripid, triptitle, tripdescription, tripprice, timestart, locationid, tripaddress, participantlimit, userid, conversationid) values (DEFAULT, 'Time of Eve (Eve no jikan)', 'Puncture wound with foreign body of lip', 392.25, '2018-05-14', 2, '7725 Buena Vista Trail', 8, 9, 9);");
//		dbm.executeUpdate("insert into trips (tripid, triptitle, tripdescription, tripprice, timestart, locationid, tripaddress, participantlimit, userid, conversationid) values (DEFAULT, 'Burning Hot Summer, A (Un été brûlant)', 'Wedge comprsn fx T11-T12 vertebra, subs for fx w nonunion', 437.9, '2017-06-22', 2, '056 Stuart Trail', 12, 9, 10);");
		//Inserts CategoriesInTrips
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (1, 1)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (1, 2)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (1, 3)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (2, 1)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (2, 2)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (2, 3)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (3, 1)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (3, 3)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (4, 2)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (4, 3)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (5, 1)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (5, 2)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (6, 1)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (7, 2)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (8, 3)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (9, 1)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (10, 1)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (10, 2)");
//		dbm.executeUpdate("INSERT INTO CategoriesInTrips VALUES (10, 3)");
		//Inserts InstructosInTrips
//		dbm.executeUpdate("INSERT INTO InstructorsInTrips VALUES (3, 4, 3)");
	}

	private static void addDefaultImages() throws IOException, SQLException {
		byte[] defaultProfilePicture = Files.readAllBytes(new File("src/default_profile_picture.png").toPath());
		byte[] defaultTripImage = Files.readAllBytes(new File("src/default.jpg").toPath());

		//Inserts the imageFile
		String imageQuery1 = "INSERT INTO Images (imageTitle, imageFile) VALUES ('DefaultProfilePicture', ?)";
		PreparedStatement imageStatement1 = dbm.getConnection().prepareStatement(imageQuery1);
		imageStatement1.setBytes(1, defaultProfilePicture);
		imageStatement1.executeUpdate();
		String imageQuery2 = "INSERT INTO Images (imageTitle, imageFile) VALUES ('DefaultTripImage', ?)";
		PreparedStatement imageStatement2 = dbm.getConnection().prepareStatement(imageQuery2);
		imageStatement2.setBytes(1, defaultTripImage);
		imageStatement2.executeUpdate();
	}

}
