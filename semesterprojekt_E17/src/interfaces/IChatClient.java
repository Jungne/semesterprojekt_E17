
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing the remote capabilities of a client.
 * 
 * @author hjaltefromholtrindom
 */
public interface IChatClient extends Remote {
	
	/**
	 * Receive a single message from the server
	 * @param message the text to be received
	 * @throws java.rmi.RemoteException
	 */
	public void receiveMessage(String message) throws RemoteException;
	
	public String getTest() throws RemoteException;
	
}
