package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServerController extends Remote {

    //User functions
    public User signUp(User user) throws RemoteException;
    
    public User signIn(String username, String password) throws RemoteException;
    
    public void signOut() throws RemoteException;
    
    //Trip functions    
    public List<Trip> getAllTrips() throws RemoteException;
    
    public void createTrip(Trip newTrip) throws RemoteException;
    
    public void modifyTrip(Trip trip) throws RemoteException;
    
    public void deleteTrip(Trip trip) throws RemoteException;
    
    public void participateInTrip(Trip trip, User user) throws RemoteException;
    
    public boolean instructInTrip(Trip trip, User user) throws RemoteException;
    
    public void leaveTrip(Trip trip) throws RemoteException;
    
    public void kickParticipant(Trip trip, User user) throws RemoteException;
    
    //Messaging functions
    public List<Conversation> getUserConversations(User user) throws RemoteException;
    
    public Conversation getConversation(Conversation conversation) throws RemoteException;
    
    public void sendMessage(Message message) throws RemoteException;
    
    public void updateConversation(Conversation conversation) throws RemoteException;
    
}
