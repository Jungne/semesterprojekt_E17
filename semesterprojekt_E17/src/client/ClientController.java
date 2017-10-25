package client;

import interfaces.IServerController;
import interfaces.Trip;
import interfaces.User;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;

public class ClientController {
    
    private IServerController serverController;
    private User currentUser;

    public ClientController() throws RemoteException{
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
    
    public List<Trip> searchTrips() {
	return null; //TODO
    }
    
    public Trip showTrip() {
	return null; //TODO
    }
    
    public void participateInTrip(User user) {
	
    }
    
    public void createTrip(int id, String title, String category, Date timedate, String location, double price, String description, List<String> gear, List<User> participants, List<User> instructors, List<User> organizers, List<String> tags) {
	ClientTripHandler.createTrip(serverController, id, title, category, timedate, location, price, description, gear, participants, instructors, organizers, tags);
    }
    
    public void modifyTrip(Trip trip) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteTrip(Trip trip) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean instructInTrip(Trip trip, User user) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void leaveTrip(Trip trip) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void kickParticipant(Trip trip, User user) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
