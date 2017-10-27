package client;

import interfaces.Category;
import interfaces.Conversation;
import interfaces.IServerController;
import interfaces.Location;
import interfaces.OptionalPrice;
import interfaces.Trip;
import interfaces.User;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	public List<Trip> getAllTrips() throws RemoteException {

		return serverController.getAllTrips();
	}

	public List<Trip> searchTrips(String searchTitle, int category, Date timedateStart, int location, double priceMAX) {

		try {
			return serverController.searchTrips(searchTitle, category, timedateStart, location, priceMAX);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

	public Trip showTrip() {
		return null; //TODO
	}

	public void participateInTrip(User user, Trip trip) {
		ClientTripHandler.participateInTrip(user, serverController, trip);
	}

	public void createTrip(String title, String description, double price, User organizer, Date timeStart, String meetingAddress, Location location, int participantlimit, Conversation conversation, String category, List<User> participants, List<User> instructors, List<Category> categories, List<OptionalPrice> optionalPrices, List<String> tags) {
		ClientTripHandler.createTrip(serverController, title, description, price, organizer, timeStart, meetingAddress, location, participantlimit, conversation, category, participants, instructors, categories, optionalPrices, tags);
	}

	public void modifyTrip(Trip trip) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deleteTrip(Trip trip) {
		ClientTripHandler.deleteTrip(serverController, trip);
	}

	public boolean instructInTrip(Trip trip, User user) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void leaveTrip(Trip trip) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void kickParticipant(Trip trip, User user) {
		ClientTripHandler.kickParticipant(serverController, trip, user);
	}
}