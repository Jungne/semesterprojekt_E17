package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface IServerController extends Remote {

    //User functions
    public User signUp(User user, String password) throws RemoteException;
    
    public User signIn(String username, String password) throws RemoteException;
    
    public void signOut() throws RemoteException;
    
    //Trip functions    
    public List<Trip> getAllTrips() throws RemoteException;
    
    public List<Trip> searchTrips(String searchTitle, int category, LocalDate timedateStart, int location, double priceMAX) throws RemoteException;
    
    public int createTrip(Trip newTrip) throws RemoteException;
    
    public void modifyTrip(Trip trip) throws RemoteException;
    
    public void deleteTrip(Trip trip) throws RemoteException;
    
    public void participateInTrip(Trip trip, User user) throws RemoteException;
    
    public boolean instructInTrip(Trip trip, User user) throws RemoteException;
    
    public void kickParticipant(Trip trip, User user) throws RemoteException;
  
	  public List<Category> getCategories() throws RemoteException;

	  public List<Location> getLocations() throws RemoteException;

	  public Trip viewTrip(int tripID) throws RemoteException;
    
    //Messaging functions
    public List<Conversation> getUserConversations(User user) throws RemoteException;
    
    public Conversation getConversation(Conversation conversation) throws RemoteException;
    
    public void sendMessage(Message message) throws RemoteException;
    
    public void updateConversation(Conversation conversation) throws RemoteException;
  
    public Trip showTrip(int tripsID) throws RemoteException;

}
