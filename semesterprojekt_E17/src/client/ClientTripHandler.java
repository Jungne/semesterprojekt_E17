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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTripHandler {

	public static void createTrip(IServerController serverController, String title, String description, double price, Date timeStart, Location location, String meetingAddress, int participantLimit, User organizer, List<Category> organizerInstructorIn, List<OptionalPrice> optionalPrices, List<Category> categories, Set<String> tags) throws Exception {
		try {
			serverController.createTrip(new Trip(title, description, categories, price, timeStart, location, meetingAddress, participantLimit, organizer, organizerInstructorIn, optionalPrices, tags));
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

	public static Trip showTrip(int tripsID, IServerController serverController) {
		try {
			return serverController.showTrip(tripsID);
		} catch (RemoteException ex) {

			Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);

		}
		return null;
	}
}
