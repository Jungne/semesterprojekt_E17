package server;

import database.DBManager;
import interfaces.Trip;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerTripHandler {

    public static void createTrip(Trip newTrip) {
	String query = "INSERT INTO Trips "; //TODO

	DBManager.getInstance().executeUpdate(query);
    }

    public static List<Trip> searchTrip(String searchTitle, int category, Date timedateStart, int location, double priceMAX) {

	String query = "SELECT Trips.tripID, tripTitle, description, tripPrice, imageFile FROM Trips, ImagesInTrips, Images "
		+ "WHERE trips.tripid = imagesintrips.tripid AND images.imageID = imagesintrips.imageID AND imagesintrips.imageid IN (SELECT MIN(imageid) FROM imagesintrips GROUP BY tripid)";

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

	ResultSet searchResult;
	
	searchResult = DBManager.getInstance().executeQuery(query);
	
	ArrayList<Trip> searchResultTrips = new ArrayList<>();
	
	try {
	    while(searchResult.next()){
		int id = searchResult.getInt(1);
		
	    
	  
	    searchResultTrips.add(trip);
	    
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

}
