package server;

import shared.Category;
import shared.Conversation;
import shared.FullTripException;
import shared.IllegalEmailException;
import shared.remote.IChatClient;
import shared.remote.IServerController;
import shared.Image;
import shared.Location;
import shared.Message;
import shared.Trip;
import shared.User;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the main server controller responsible for
 *
 * @author group 12
 */
public class ServerControllerImpl extends UnicastRemoteObject implements IServerController {

	//
	private ServerMessagingHandler messagingHandler;

	/**
	 * This constructor
	 *
	 */
	public ServerControllerImpl() throws RemoteException {
		messagingHandler = new ServerMessagingHandler();
	}

	@Override
	public User signUp(User user, String password) throws RemoteException, IllegalEmailException {
		return ServerUserHandler.createUser(user, password);
	}

	@Override
	public User signIn(String email, String password) throws RemoteException {
		return ServerUserHandler.signIn(email, password);
	}

	@Override
	public void signOut() throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public User updateUser(int currentUserID) throws RemoteException {
		return ServerUserHandler.updateUser(currentUserID);
	}

	@Override
	public void changeProfilePicture(int currentUserID, Image profilePicture) throws RemoteException {
		ServerUserHandler.changeProfilePicture(currentUserID, profilePicture);
	}

	@Override
	public int createTrip(Trip newTrip) throws RemoteException {
		return ServerTripHandler.createTrip(newTrip);
	}

	@Override
	public List<Trip> searchTrips(String searchTitle, List<Category> categories, LocalDate timedateStart, int location, double priceMAX, String tripType) throws RemoteException {
		return ServerTripHandler.searchTrip(searchTitle, categories, timedateStart, location, priceMAX, tripType);
	}

	@Override
	public void modifyTrip(Trip trip) throws RemoteException {
		ServerTripHandler.modifyTrip(trip);
	}

	@Override
	public void deleteTrip(int tripId, int organizerId) throws RemoteException {
		ServerTripHandler.deleteTrip(tripId, organizerId);
	}

	@Override
	public void participateInTrip(Trip trip, User user) throws RemoteException, FullTripException {
		ServerTripHandler.participateInTrip(trip, user);

	}

	@Override
	public boolean instructInTrip(Trip trip, User user) throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void kickParticipant(Trip trip, int userId) throws RemoteException {
		ServerTripHandler.kickParticipant(trip, userId);
	}

	@Override
	public List<Category> getCategories() throws RemoteException {
		return ServerTripHandler.getCategories();
	}

	@Override
	public List<Location> getLocations() throws RemoteException {
		return ServerTripHandler.getLocations();
	}

	@Override
	public Conversation getConversation(Conversation conversation) throws RemoteException {
		return ServerMessagingHandler.getConversation(conversation);
	}

	@Override
	public void sendMessage(Message message) throws RemoteException {
		ServerMessagingHandler.sendMessage(message);
	}

	@Override
	public void updateConversation(Conversation conversation) throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Trip showTrip(int tripsID) throws RemoteException {
		return ServerTripHandler.showTrip(tripsID);

	}

	@Override
	public Trip viewTrip(int tripID) throws RemoteException {
		return ServerTripHandler.viewTrip(tripID);
	}

	@Override
	public void registerClient(IChatClient client) {
		try {
			messagingHandler.registerClient(client);
		} catch (RemoteException ex) {
			Logger.getLogger(ServerControllerImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public List<User> searchUsers(String query) {
		return ServerUserHandler.searchUsers(query);
	}

	@Override
	public List<Trip> getMyTrips(User user) {
		return ServerTripHandler.getMyTrips(user);
	}

	@Override
	public List<Trip> getMyTrips(User user, int organizerId) {
		return ServerTripHandler.getMyTrips(user, organizerId);
	}

	@Override
	public void addActiveConversation(int tripID) {
		messagingHandler.addActiveConversation(tripID);
	}

	@Override
	public List<Conversation> getUserConversations(User user) throws RemoteException {
		return messagingHandler.getUserConversations(user);
	}

	@Override
	public String getConversationName(Conversation conversation, User user) throws RemoteException {
		return ServerMessagingHandler.getConversationName(conversation, user);
	}

	@Override
	public Conversation getUserConversation(int userId1, int userId2) throws RemoteException {
		return ServerMessagingHandler.getUserConversation(userId1, userId2);
	}

}
