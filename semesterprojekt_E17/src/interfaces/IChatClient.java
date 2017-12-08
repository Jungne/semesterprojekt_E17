package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing the remote capabilities of a client.
 *
 * @author group 12
 */
public interface IChatClient extends Remote {

	/**
	 * Receive a single message from the server
	 *
	 * @param message the text to be received
	 * @throws java.rmi.RemoteException
	 */
	public void receiveMessage(Message message) throws RemoteException;

	public int getUserId() throws RemoteException;
	
}
