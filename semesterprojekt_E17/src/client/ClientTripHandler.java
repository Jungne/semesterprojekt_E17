package client;

import interfaces.Category;
import interfaces.Conversation;
import interfaces.IServerController;
import interfaces.Location;
import interfaces.OptionalPrice;
import interfaces.Trip;
import interfaces.User;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTripHandler {
    
    public static void createTrip(IServerController serverController, String title, String description, double price, User organizer, Date timeStart, String meetingAddress, Location location, int participantlimit, Conversation conversation, String category, List<User> participants, List<User> instructors, List<Category> categories, List<OptionalPrice> optionalPrices, List<String> tags) {
	try {
	    serverController.createTrip(new Trip(-1, title, description, price, organizer, timeStart, meetingAddress, location, participantlimit, conversation, category, participants, instructors, categories, optionalPrices, tags));
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
    public static void kickParticipant(IServerController serverController, Trip trip, User user) {
		 try {
            serverController.kickParticipant(trip, user);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
