package server;

import database.DBManager;
import interfaces.Trip;

public class ServerTripHandler {
    public static void createTrip(Trip newTrip) {
	String query = "INSERT INTO Trips "; //TODO
	
	DBManager.getInstance().executeUpdate(query);
    }
}
