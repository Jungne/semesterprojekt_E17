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

	public static void createTrip(IServerController serverController, String title, String description, double price, Date timeStart, Location location, String meetingAddress, int participantlimit, User organizer, List<User> participants, List<User> instructors, List<OptionalPrice> optionalPrices, Conversation conversation, List<Category> categories, List<String> tags) {
		try {
			serverController.createTrip(new Trip(-1, title, description, price, timeStart, location, meetingAddress, participantlimit, organizer, participants, instructors, optionalPrices, conversation, categories, tags));
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
        public static Trip showTrip(int tripsID,IServerController serverController) {
          try {
		return serverController.showTrip(tripsID);
		}catch (RemoteException ex) {
                    
			Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
 
                }
          return null;
}
}
