package server;

import database.DBManager;
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
 * This class handles creation of trip objects to be sent to the client. This class also handles storage of trip objects in the database received from the client.
 * 
 * @author group 12
 */
public class ServerTripHandler {

	public static void createTrip(Trip newTrip) {
		String query = "INSERT INTO Trips "; //TODO

		DBManager.getInstance().executeUpdate(query);
	}
/**
 * This method handles searching for trips, by building a SQL query from the given parameters.
 * @param searchTitle used for a regex.
 * @param category
 * @param timedateStart
 * @param location
 * @param priceMAX
 * @return list of trips matching search parameters.
 */
	public static List<Trip> searchTrip(String searchTitle, int category, Date timedateStart, int location, double priceMAX) {
		
		//Initializes the query string.
		String query = "SELECT Trips.tripID, tripTitle, description, tripPrice, imageFile FROM Trips, ImagesInTrips, Images "
						+ "WHERE trips.tripid = imagesintrips.tripid AND images.imageID = imagesintrips.imageID AND imagesintrips.imageid IN (SELECT MIN(imageid) FROM imagesintrips GROUP BY tripid)";

		//These if statements checks if the different parameters are used, and adds the necessary SQL code to the query string.
		if (!searchTitle.equals(null)) {
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
				InputStream inputStream = new ByteArrayInputStream(searchResult.getBytes(5));
				Image image = new Image(inputStream);
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
        public static void participateInTrip(Trip trip, User user){
        int tripID= trip.getId();
        int userID = user.getId();
        
    
    String query = "INSERT INTO UsersInTrips VALUES ('"+tripID+"', '"+userID+"');";
    DBManager.getInstance().executeUpdate(query);
    
    }

}
