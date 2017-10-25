package server;

import interfaces.Conversation;
import interfaces.IServerController;
import interfaces.Message;
import interfaces.Trip;
import interfaces.User;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerControllerImpl extends UnicastRemoteObject implements IServerController {

    public ServerControllerImpl() throws RemoteException{
	
    }

    @Override
    public User signUp(User user) throws RemoteException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User signIn(String username, String password) throws RemoteException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void signOut() throws RemoteException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Trip> getAllTrips() throws RemoteException {
	ArrayList<Trip> tripList = new ArrayList<>();
	tripList.add(new Trip(0, "First trip"));
	
	return tripList;
    }

    @Override
    public void createTrip(Trip newTrip) throws RemoteException {
	
    }

    @Override
    public void modifyTrip(Trip trip) throws RemoteException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteTrip(Trip trip) throws RemoteException {
	ServerTripHandler.deleteTrip(trip);
    }

    @Override
    public void participateInTrip(Trip trip, User user) throws RemoteException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean instructInTrip(Trip trip, User user) throws RemoteException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void leaveTrip(Trip trip) throws RemoteException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void kickParticipant(Trip trip, User user) throws RemoteException {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
}
