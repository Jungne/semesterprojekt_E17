package client;

import interfaces.IServerController;
import interfaces.Image;
import interfaces.User;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientUserHandler {

	public static User signUp(IServerController serverController, String email, String name, Image profilePicture, String password) {
		try {
			return serverController.signUp(new User(email, name, profilePicture), password);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	public static User signIn(IServerController serverController, String email, String password) {
		try {
			return serverController.signIn(email, password);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientTripHandler.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

}
