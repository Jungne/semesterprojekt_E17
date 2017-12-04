package client;

import interfaces.Conversation;
import interfaces.IChatClient;
import interfaces.IServerController;
import interfaces.Message;
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
	private static Conversation activeConversation = null;
	private static int activeConversationId;

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
		public void receiveMessage(Message message) throws RemoteException {
			System.out.println(message.getMessage());
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

	public static void setCurrentConversation(IServerController serverController, int conversationID) throws RemoteException {
		serverController.addActiveConversation(conversationID);
//		activeConversation = serverController.getConversation(new Conversation(tripID, "trip"));
		activeConversationId = conversationID;
	}

	public static void setActiveConversation(IServerController serverController, Conversation conversation) throws RemoteException {
//		activeConversation = serverController.getConversation(conversation);
		activeConversationId = conversation.getId();
	}

	public static List<Conversation> getUserConversations(IServerController serverController, User user) {
		try {
			return serverController.getUserConversations(user);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static Conversation getConversation(IServerController serverController, Conversation conversation) throws RemoteException {
		return serverController.getConversation(conversation);
	}

	public static void sendMessage(IServerController serverController, String message, User user) throws RemoteException {
		serverController.sendMessage(new Message(user.getId(), message, activeConversationId));
	}
}
