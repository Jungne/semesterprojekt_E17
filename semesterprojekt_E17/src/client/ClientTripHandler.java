package client;

import interfaces.Category;
import interfaces.IServerController;
import interfaces.Location;
import interfaces.OptionalPrice;
import interfaces.Trip;
import interfaces.User;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTripHandler {

	public static int createTrip(IServerController serverController, String title, String description, List<Category> categories, double price, LocalDateTime timeStart, Location location, String meetingAddress, int participantLimit, User organizer, List<Category> organizerInstructorIn, List<OptionalPrice> optionalPrices, Set<String> tags, List<byte[]> images) throws Exception {
		try {
			return serverController.createTrip(new Trip(title, description, categories, price, timeStart, location, meetingAddress, participantLimit, organizer, organizerInstructorIn, optionalPrices, tags, images));
		} catch (RemoteException ex) {
			Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
			return -1;
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

	public static List<Category> getCategories(IServerController serverController) {
		try {
			return serverController.getCategories();
		} catch (RemoteException ex) {
			Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public static List<Location> getLocations(IServerController serverController) {
		try {
			return serverController.getLocations();
		} catch (RemoteException ex) {
			Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

}
