package server;

import database.DBManager;
import interfaces.Trip;
import java.util.Date;
import java.util.List;

public class ServerTripHandler {

    public static void createTrip(Trip newTrip) {
	String query = "INSERT INTO Trips "; //TODO

	DBManager.getInstance().executeUpdate(query);
    }

    public static List<Trip> searchTrip(String searchTitle, int category, Date timedateStart, int location, double priceMAX) {

	String query = "SELECT * FROM Trips WHERE";

	if (!searchTitle.equals(null)) {
	    query += " tripTitle LIKE '%" + searchTitle + "%' AND";
	}

	if (category >= 0) {
	    query += " categoryID = '" + category + "' AND";
	}

	if (timedateStart != null) {
	    query += " timeStart >= '" + timedateStart + "' AND";
	}

	if (location >= 0) {
	    query += " locationID = '" + location + "' AND";
	}

	if (priceMAX >= 0) {
	    query += "tripPrice <= " + priceMAX + "' AND";
	}

	query += " TRUE";

	DBManager.getInstance().executeUpdate(query);
	
	return null;
    }

    public static void deleteTrip(Trip trip) {
        String query = "DELETE FROM Trips WHERE tripID = " + trip.getId() + ";";

        DBManager.getInstance().executeUpdate(query);
    }

}
