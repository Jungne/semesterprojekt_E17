package client;

import interfaces.IServerController;
import interfaces.Trip;
import interfaces.User;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTripHandler {
    
    public static void createTrip(IServerController serverController, int id, String title, String category, Date timedate, String location, double price, String description, List<String> gear, List<User> participants, List<User> instructors, List<User> organizers, List<String> tags) {
	try {
	    serverController.createTrip(new Trip(id, title, category, timedate, location, price, description, gear, participants, instructors, organizers, tags));
	} catch (RemoteException ex) {
	    Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    public static void deleteTrip(IServerController serverController, Trip trip) {
        try {
            serverController.deleteTrip(trip);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   public static void participateInTrip(User user, IServerController serverController, Trip trip) {
        try {
            serverController.participateInTrip(trip, user);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
