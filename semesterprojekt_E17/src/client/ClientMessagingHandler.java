package client;

import gui.HBoxCell;
import shared.Conversation;
import shared.remote.IChatClient;
import shared.remote.IServerController;
import shared.Message;
import shared.User;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

/**
 * ClientMessagingHandler is responsible for managing all messaging activity
 * between the client and the server.
 *
 * @author group 12
 */
public class ClientMessagingHandler {

	private static MessageReceiver receiver = null;
	private static Conversation activeConversation = null;
	private static int activeConversationId;
	private static ObservableList messagesList;

	/**
	 * Remote object that is passed to the server and used to perform callbacks
	 * that deliver messages, represents a user on the server.
	 */
	@SuppressWarnings("serial")
	private static class MessageReceiver extends UnicastRemoteObject implements IChatClient {

		private String test = "test";
		private User user;

		protected MessageReceiver(User user) throws RemoteException {
			super();
			this.user = user;
		}

		@Override
		public void receiveMessage(Message message) throws RemoteException {
			if (activeConversationId == message.getConversationId()) {
				messagesList.add(new HBoxCell(message, user));
			}
		}

		@Override
		public int getUserId() {
			return user.getId();
		}
	}

	public static MessageReceiver getMessagereceiverInstance(User user) throws RemoteException {
		if (receiver == null) {
			receiver = new MessageReceiver(user);
		}
		return receiver;
	}

	public static void setCurrentConversation(IServerController serverController, int conversationID) throws RemoteException {
		activeConversationId = conversationID;
	}

	public static void setActiveConversation(IServerController serverController, Conversation conversation) throws RemoteException {
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

	public static void setMessagesList(ObservableList messagesList) {
		ClientMessagingHandler.messagesList = messagesList;
	}

	public static void signOut() {
		receiver = null;
	}

	public static String getConversationName(IServerController serverController, Conversation conversation, User currentUser) throws RemoteException {
		return serverController.getConversationName(conversation, currentUser);
	}

	public static Conversation getUserConversation(IServerController serverController, int userId1, int userId2) {
		try {
			return serverController.getUserConversation(userId1, userId2);
		} catch (RemoteException ex) {
			Logger.getLogger(ClientMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

}
