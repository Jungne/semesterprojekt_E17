package client;

import interfaces.Category;
import interfaces.Conversation;
import interfaces.FullTripException;
import interfaces.IServerController;
import interfaces.Image;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientController {

	private IServerController serverController;
	private User currentUser = null;
	private Conversation activeConversation;

	public ClientController() throws RemoteException {
		//String hostname = "tek-sb3-glo0a.tek.sdu.dk";
		String hostname = "localhost";

		try {
			Registry registry = LocateRegistry.getRegistry(hostname, 12312);
			serverController = (IServerController) registry.lookup("serverController");
		} catch (RemoteException | NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	public void signUp(String email, String name, Image profilePicture, String password) throws RemoteException {
		currentUser = ClientUserHandler.signUp(serverController, email, name, profilePicture, password);
	}

	public boolean signIn(String email, String password) {
		currentUser = ClientUserHandler.signIn(serverController, email, password);
		if (currentUser != null) {
			try {
				serverController.registerClient(ClientMessagingHandler.getMessagereceiverInstance(currentUser));
				return true;
			} catch (RemoteException ex) {
				Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return false;
	}

	public void signOut() throws RemoteException {
		currentUser = null;
		ClientMessagingHandler.signOut();
	}

	public void updateUser() throws RemoteException {
		currentUser = ClientUserHandler.updateUser(serverController, currentUser.getId());
	}

	public void changeProfilePicture(Image profilePicture) {
		int currentUserID = currentUser.getId();
		ClientUserHandler.changeProfilePicture(serverController, currentUserID, profilePicture);
	}

	public List<Trip> searchTrips(String searchTitle, List<Category> categories, LocalDate timedateStart, int location, double priceMAX, String tripType) throws RemoteException {
		return serverController.searchTrips(searchTitle, categories, timedateStart, location, priceMAX, tripType);
	}

	public Trip showTrip(int tripID) {
		return ClientTripHandler.showTrip(tripID, serverController);
	}

	public Trip viewTrip(int tripID) {
		return ClientTripHandler.viewTrip(tripID, serverController);
	}

	public void participateInTrip(Trip trip) throws FullTripException {
		ClientTripHandler.participateInTrip(currentUser, serverController, trip);
	}

	public int createTrip(String title, String description, List<Category> categories, double price, LocalDateTime timeStart, Location location, String meetingAddress, int participantLimit, User organizer, List<Category> organizerInstructorIn, List<OptionalPrice> optionalPrices, Set<String> tags, List<Image> images) throws Exception {
		return ClientTripHandler.createTrip(serverController, title, description, categories, price, timeStart, location, meetingAddress, participantLimit, organizer, organizerInstructorIn, optionalPrices, tags, images);
	}

	public void modifyTrip(Trip trip) {
		ClientTripHandler.modifyTrip(trip, serverController);
	}

	public void deleteTrip(int tripId) {
		ClientTripHandler.deleteTrip(serverController, tripId, currentUser.getId());
	}

	public boolean instructInTrip(Trip trip, User user) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void kickParticipant(Trip trip, int userId) {
		ClientTripHandler.kickParticipant(serverController, trip, userId);
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

	public List<User> searchUsers(String query) throws RemoteException {
		return ClientUserHandler.searchUsers(serverController, query);
	}

	public List<Trip> getMyTrips() {
		if (currentUser != null) {
			return ClientTripHandler.getMyTrips(currentUser, serverController);
		}
		return null;
	}

	public List<Trip> getMyOrganizedTrips() {
		if (currentUser != null) {
			return ClientTripHandler.getMyTrips(currentUser, serverController, currentUser.getId());
		}
		return null;
	}

	public void setCurrentConversation(int tripID) throws RemoteException {
		ClientMessagingHandler.setCurrentConversation(serverController, tripID);
	}

	public List<Conversation> getUserConversations() {
		return ClientMessagingHandler.getUserConversations(serverController, currentUser);
	}

	public String getConversationName(Conversation conversation){
		try {
			return ClientMessagingHandler.getConversationName(serverController, conversation, currentUser);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public Conversation getConversation(Conversation conversation) throws RemoteException {
		return ClientMessagingHandler.getConversation(serverController, conversation);
	}

	public void sendMessage(String message) throws RemoteException {
		ClientMessagingHandler.sendMessage(serverController, message, currentUser);
	}

	public void setActiveConversation(Conversation conversation) throws RemoteException {
		ClientMessagingHandler.setActiveConversation(serverController, conversation);
	}

	public Conversation getUserConversation(int userId) {
		return ClientMessagingHandler.getUserConversation(serverController, currentUser.getId(), userId);
	}
	
}
