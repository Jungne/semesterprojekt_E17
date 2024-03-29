package shared.remote;

import shared.Category;
import shared.Conversation;
import shared.FullTripException;
import shared.IllegalEmailException;
import shared.Image;
import shared.Location;
import shared.Message;
import shared.Trip;
import shared.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface
 *
 * @author group 12
 */
public interface IServerController extends Remote {

	//User functions
	public User signUp(User user, String password) throws RemoteException, IllegalEmailException;

	public User signIn(String username, String password) throws RemoteException;

	public void signOut() throws RemoteException;

	public User updateUser(int currentUserID) throws RemoteException;

	public void changeProfilePicture(int userID, Image profilePicture) throws RemoteException;

	//Trip functions
	public List<Trip> searchTrips(String searchTitle, List<Category> categories, LocalDate timeDateStart, int locationId, double priceMAX, String tripType) throws RemoteException;

	public int createTrip(Trip newTrip) throws RemoteException;

	public void modifyTrip(Trip trip) throws RemoteException;

	public void deleteTrip(int tripId, int organizerId) throws RemoteException;

	public void participateInTrip(int tripId, int userId) throws RemoteException, FullTripException;

	public boolean instructInTrip(Trip trip, User user) throws RemoteException;

	public void kickParticipant(Trip trip, int userId) throws RemoteException;

	public List<Category> getCategories() throws RemoteException;

	public List<Location> getLocations() throws RemoteException;

	public Trip viewTrip(int tripId) throws RemoteException;

	//Messaging functions
	public Conversation getConversation(Conversation conversation) throws RemoteException;

	public void sendMessage(Message message) throws RemoteException;

	public void updateConversation(Conversation conversation) throws RemoteException;

	public Trip showTrip(int tripId) throws RemoteException;

	public void registerClient(IChatClient client) throws RemoteException;

	public List<User> searchUsers(String query) throws RemoteException;

	public List<Trip> getMyTrips(int userId) throws RemoteException;

	public List<Trip> getMyOrganizedTrips(int userId) throws RemoteException;

	public List<Conversation> getUserConversations(User user) throws RemoteException;

	public String getConversationName(Conversation conversation, User user) throws RemoteException;

	public Conversation getUserConversation(int userId1, int userId2) throws RemoteException;

}
