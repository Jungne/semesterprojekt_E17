package client;

import shared.Category;
import shared.FullTripException;
import shared.remote.IServerController;
import shared.Image;
import shared.Location;
import shared.OptionalPrice;
import shared.Trip;
import shared.User;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTripHandler {

	public static int createTrip(IServerController serverController, String title, String description, List<Category> categories, double price, LocalDateTime timeStart, Location location, String meetingAddress, int participantLimit, User organizer, List<Category> organizerInstructorIn, List<OptionalPrice> optionalPrices, Set<String> tags, List<Image> images) throws Exception {
		try {
			return serverController.createTrip(new Trip(title, description, categories, price, timeStart, location, meetingAddress, participantLimit, organizer, organizerInstructorIn, optionalPrices, tags, images));
		} catch (RemoteException ex) {
			Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
			return -1;
		}
	}

	public static List<Trip> searchTrips(IServerController serverController, String searchTitle, List<Category> categories, LocalDate timedateStart, int location, double priceMAX, String tripType) throws RemoteException {
		return serverController.searchTrips(searchTitle, categories, timedateStart, location, priceMAX, tripType);
	}

	public static void deleteTrip(IServerController serverController, int tripId, int organizerId) {
		try {
			serverController.deleteTrip(tripId, organizerId);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void participateInTrip(IServerController serverController, int tripId, int userId) throws FullTripException {
		try {
			serverController.participateInTrip(tripId, userId);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void kickParticipant(IServerController serverController, Trip trip, int userId) {
		try {
			serverController.kickParticipant(trip, userId);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static Trip showTrip(int tripID, IServerController serverController) {
		try {
			return serverController.showTrip(tripID);
		} catch (RemoteException ex) {

			Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);

		}
		return null;
	}

	static Trip viewTrip(int tripID, IServerController serverController) {
		try {
			return serverController.viewTrip(tripID);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
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

	static void modifyTrip(Trip trip, IServerController serverController) {
		try {
			serverController.modifyTrip(trip);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static List<Trip> getMyTrips(IServerController serverController, int userId) {
		try {
			return serverController.getMyTrips(userId);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static List<Trip> getMyOrganizedTrips(IServerController serverController, int userId) {
		try {
			return serverController.getMyOrganizedTrips(userId);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

}
