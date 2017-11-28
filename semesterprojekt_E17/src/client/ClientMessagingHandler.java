
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
		protected MessageReceiver() throws RemoteException {
			super();
		}

		@Override
		public void receiveMessage(String message) throws RemoteException {
			// Unsupported operation
		}
		
		@Override
		public String getTest() {
			return test;
		}
		
	}
	
	public static MessageReceiver getMessagereceiverInstance() throws RemoteException {
		if (receiver == null) {
			receiver = new MessageReceiver();
		} 
		return receiver;
	}
	
	public static void setCurrentConversation(IServerController serverController, int tripID) throws RemoteException {
		serverController.addActiveConversation(tripID);
	}
}
