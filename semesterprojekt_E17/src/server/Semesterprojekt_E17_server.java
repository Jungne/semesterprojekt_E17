package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the server start class.
 *
 * @author group 12
 */
public class Semesterprojekt_E17_server {

	private static final int REGISTRY_PORT = 12312;
	private static final String REMOTE_OBJECT_NAME = "serverController";

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {

		try {
			//
			ServerControllerImpl serverController = new ServerControllerImpl();
			Registry registry = LocateRegistry.createRegistry(REGISTRY_PORT);
			registry.bind(REMOTE_OBJECT_NAME, serverController);
			System.out.println("Server is running");
		} catch (RemoteException ex) {
			Logger.getLogger(Semesterprojekt_E17_server.class.getName()).log(Level.SEVERE, null, ex);
		} catch (AlreadyBoundException ex) {
			Logger.getLogger(Semesterprojekt_E17_server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
