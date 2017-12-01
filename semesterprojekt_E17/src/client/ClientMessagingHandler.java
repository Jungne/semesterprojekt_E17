package client;

import interfaces.Conversation;
import interfaces.IChatClient;
import interfaces.IServerController;
import interfaces.User;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hjaltefromholtrindom
 */
public class ClientMessagingHandler {

	private static MessageReceiver receiver = null;

	/**
	 * Remote object that is passed to the server and used to perform callbacks
	 * that deliver messages into the queue object
	 *
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

	public static List<Conversation> loadMyConversations(IServerController serverController, User user) {
		try {
			return serverController.loadMyConversations(user);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

}
