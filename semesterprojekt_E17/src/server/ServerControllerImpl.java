package server;

import interfaces.Category;
import interfaces.Conversation;
import interfaces.FullTripException;
import interfaces.IServerController;
import interfaces.Location;
import interfaces.Message;
import interfaces.Trip;
import interfaces.User;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerControllerImpl extends UnicastRemoteObject implements IServerController {

	public ServerControllerImpl() throws RemoteException {

	}

	@Override
	public User signUp(User user, String password) throws RemoteException {
		return ServerUserHandler.createUser(user, password);
	}

	@Override
	public User signIn(String username, String password) throws RemoteException {
		return ServerUserHandler.signIn(username, password);
	}

	@Override
	public void signOut() throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<Trip> getAllTrips() throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int createTrip(Trip newTrip) throws RemoteException {
		return ServerTripHandler.createTrip(newTrip);
	}

	@Override
	public List<Trip> searchTrips(String searchTitle, int category, LocalDate timedateStart, int location, double priceMAX) throws RemoteException {
		return ServerTripHandler.searchTrip(searchTitle, category, timedateStart, location, priceMAX);
	}

	@Override
	public void modifyTrip(Trip trip) throws RemoteException {
		ServerTripHandler.modifyTrip(trip);
	}

	@Override
	public void deleteTrip(Trip trip) throws RemoteException {
		ServerTripHandler.deleteTrip(trip);
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
	public void kickParticipant(Trip trip, User user) throws RemoteException {
		ServerTripHandler.kickParticipant(trip, user);
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
	public List<Conversation> getUserConversations(User user) throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Conversation getConversation(Conversation conversation) throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void sendMessage(Message message) throws RemoteException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
	public List<User> searchUsers(String query) {
		return ServerUserHandler.searchUsers(query);
	}
}
