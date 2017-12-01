
package client;

import interfaces.IChatClient;
import interfaces.IServerController;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author hjaltefromholtrindom
 */
public class ClientMessagingHandler {
	
	private static MessageReceiver receiver = null; 
	
		/**
	 * Remote object that is passed to the server and used to perform callbacks
	 * that deliver messages into the queue object
	 * @author ups
	 *
	 */
	@SuppressWarnings("serial")
	private static class MessageReceiver extends UnicastRemoteObject implements IChatClient {
		private String test = "test";
		private int userId;
		
		protected MessageReceiver(int userId) throws RemoteException {
			super();
			this.userId = userId;
		}

		@Override
		public void receiveMessage(String message) throws RemoteException {
			// Unsupported operation
		}
		
		@Override
		public int getUserId() {
			return userId;
		}		
	}
	
	public static MessageReceiver getMessagereceiverInstance(int userId) throws RemoteException {
		if (receiver == null) {
			receiver = new MessageReceiver(userId);
		} 
		return receiver;
	}
	
	public static void setCurrentConversation(IServerController serverController, int tripID) throws RemoteException {
		serverController.addActiveConversation(tripID);
	}
	
	
}