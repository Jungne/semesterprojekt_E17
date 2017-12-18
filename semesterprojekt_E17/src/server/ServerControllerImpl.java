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

/**
 * ServerControllerImpl is responsible for managing all communication between
 * the server and the client. ServerControllerImpl distributes work to the
 * ServerUserHandler, ServerTripHandler and ServerMessagingHandler. These
 * handlers communicate to the Database server through the Database Manager.
 *
 * @author group 12
 */
public class ServerControllerImpl extends UnicastRemoteObject implements IServerController {

	/**
	 * This constructor
	 *
	 */
	public ServerControllerImpl() throws RemoteException {

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
	public void changeProfilePicture(int userId, Image profilePicture) throws RemoteException {
		ServerUserHandler.changeProfilePicture(userId, profilePicture);
	}

	@Override
	public int createTrip(Trip newTrip) throws RemoteException {
		return ServerTripHandler.createTrip(newTrip);
	}

	@Override
	public List<Trip> searchTrips(String searchTitle, List<Category> categories, LocalDate timeDateStart, int locationId, double priceMAX, String tripType) throws RemoteException {
		return ServerTripHandler.searchTrip(searchTitle, categories, timeDateStart, locationId, priceMAX, tripType);
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
	public void participateInTrip(int tripId, int userId) throws RemoteException, FullTripException {
		ServerTripHandler.participateInTrip(tripId, userId);
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
	public Trip showTrip(int tripId) throws RemoteException {
		return ServerTripHandler.showTrip(tripId);

	}

	@Override
	public Trip viewTrip(int tripId) throws RemoteException {
		return ServerTripHandler.viewTrip(tripId);
	}

	@Override
	public void registerClient(IChatClient client) throws RemoteException {
		ServerMessagingHandler.registerClient(client);
	}

	@Override
	public List<User> searchUsers(String query) throws RemoteException {
		return ServerUserHandler.searchUsers(query);
	}

	@Override
	public List<Trip> getMyTrips(int userId) throws RemoteException {
		return ServerTripHandler.getMyTrips(userId);
	}

	@Override
	public List<Trip> getMyOrganizedTrips(int userId) throws RemoteException {
		return ServerTripHandler.getMyOrganizedTrips(userId);
	}

	@Override
	public List<Conversation> getUserConversations(User user) throws RemoteException {
		return ServerMessagingHandler.getUserConversations(user);
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
