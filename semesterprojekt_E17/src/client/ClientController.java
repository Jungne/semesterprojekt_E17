package client;

import interfaces.Category;
import interfaces.IServerController;
import interfaces.Location;
import interfaces.OptionalPrice;
import interfaces.Trip;
import interfaces.User;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ClientController {

	private IServerController serverController;
	private User currentUser;

	public ClientController() throws RemoteException {
		String hostname = "localhost";

		try {
			Registry registry = LocateRegistry.getRegistry(hostname, 12345);
			serverController = (IServerController) registry.lookup("serverController");
		} catch (RemoteException | NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	public void signUp(User user, String password) throws RemoteException {
		currentUser = serverController.signUp(user, password);
	}

	public void signIn(String username, String password) throws RemoteException {
		currentUser = serverController.signIn(username, password);
	}

	public void signOut() throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<Trip> getAllTrips() throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<Trip> searchTrips(String searchTitle, int category, LocalDate timedateStart, int location, double priceMAX)  throws RemoteException {
		return serverController.searchTrips(searchTitle, category, timedateStart, location, priceMAX);
	}

	public Trip showTrip(int tripID) {
		return ClientTripHandler.showTrip(tripID, serverController);
	}

	public Trip viewTrip(int tripID) {
		return ClientTripHandler.viewTrip(tripID, serverController);
	}

	public void participateInTrip(User user, Trip trip) {
		ClientTripHandler.participateInTrip(user, serverController, trip);
	}

	public int createTrip(String title, String description, List<Category> categories, double price, LocalDateTime timeStart, Location location, String meetingAddress, int participantLimit, User organizer, List<Category> organizerInstructorIn, List<OptionalPrice> optionalPrices, Set<String> tags, List<byte[]> images) throws Exception {
		return ClientTripHandler.createTrip(serverController, title, description, categories, price, timeStart, location, meetingAddress, participantLimit, organizer, organizerInstructorIn, optionalPrices, tags, images);
	}

	public void modifyTrip(Trip trip) {
		ClientTripHandler.modifyTrip(trip, serverController);
	}

	public void deleteTrip(Trip trip) {
		ClientTripHandler.deleteTrip(serverController, trip);
	}

	public boolean instructInTrip(Trip trip, User user) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void kickParticipant(Trip trip, User user) {
		ClientTripHandler.kickParticipant(serverController, trip, user);
	}

	public List<Category> getCategories() {
		return ClientTripHandler.getCategories(serverController);
	}

	public List<Location> getLocations() {
		return ClientTripHandler.getLocations(serverController);
	}

	public User getCurrentUser() {
		return currentUser;
	}
}
