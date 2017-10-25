package client;

import interfaces.IServerController;
import interfaces.Trip;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientController {
    
    private IServerController serverController;

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
    
    
}
