package server;

import database.DBManager;
import interfaces.Conversation;
import interfaces.IChatClient;
import interfaces.Message;
import interfaces.User;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hjaltefromholtrindom
 */
public class ServerMessagingHandler {

	private static DBManager dbm = DBManager.getInstance();

	/**
	 * All clients of the server, in the form of remote objects.
	 */
	private static Map<Integer, IChatClient> clientsMap = new HashMap<>();

	/**
	 * Register clients in the form of remote objects.
	 */
	public void registerClient(IChatClient client) throws RemoteException {
		clientsMap.put(client.getUserId(), client);
		System.out.println(client.getUserId());
	}

	/**
	 * Register the active conversations.
	 */
	public void addActiveConversation(int conversationID) {
		System.out.print(conversationID);

	}

	public List<Conversation> getUserConversations(User user) {
		String query = ""
						+ "SELECT conversationID, type\n"
						+ "FROM UsersInConversations\n"
						+ "NATURAL JOIN Conversations\n"
						+ "WHERE userID = " + user.getId() + ";";

		ResultSet conversationsRs = dbm.executeQuery(query);
		ArrayList<Conversation> conversations = new ArrayList<>();

		try {
			while (conversationsRs.next()) {
				conversations.add(new Conversation(conversationsRs.getInt("conversationID"), conversationsRs.getString("type")));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return conversations;
	}

	/**
	 * Inserts a new conversation and return the ID.
	 *
	 * @param type of trip
	 * @return ID of created conversation
	 */
	public static int addConversation(String type) {
		return dbm.executeInsertAndGetId("INSERT INTO Conversations (conversationID, type) VALUES (DEFAULT, '" + type + "');");
	}

	public static void addUserToConversation(int userId, int conversationId) {
		String query = ""
						+ "INSERT INTO UsersInConversations (conversationId, userId) "
						+ "VALUES (" + conversationId + "," + userId + ")";

		dbm.executeUpdate(query);
	}

	public static String getConversationName(Conversation conversation, User user) {
		if (conversation.getType().equals("trip")) {
			String query = ""
							+ "SELECT triptitle\n"
							+ "FROM trips\n"
							+ "WHERE conversationID = " + conversation.getId();

			ResultSet conversationNameRs = dbm.executeQuery(query);

			try {
				if (conversationNameRs.next()) {
					return conversationNameRs.getString("tripTitle");
				}
			} catch (SQLException ex) {
				Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else if (conversation.getType().equals("users")) {
			String query = ""
							+ "SELECT userName\n"
							+ "FROM UsersInConversations\n"
							+ "NATURAL JOIN Users\n"
							+ "WHERE conversationId = " + conversation.getId() + " AND NOT Users.userId = " + user.getId();

			ResultSet conversationNameRs = dbm.executeQuery(query);

			try {
				String conversationName = "";
				while (conversationNameRs.next()) {
					conversationName += conversationNameRs.getString("userName") + ", ";
				}

				if (conversationName.equals("")) {
					return "";
				} else {
					return conversationName.substring(0, conversationName.length() - 2);
				}
			} catch (SQLException ex) {
				Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return "ConversationID: " + conversation.getId();
	}

	public static Conversation getConversation(Conversation conversation) {
		String query = ""
						+ "SELECT *\n"
						+ "FROM Messages\n"
						+ "WHERE conversationID = " + conversation.getId();
		ResultSet conversationRs = dbm.executeQuery(query);

		Set<Message> messages = new TreeSet<>();

		try {
			while (conversationRs.next()) {
				int id = conversationRs.getInt("messageID");
				int userId = conversationRs.getInt("userID");
				String message = conversationRs.getString("message");
				LocalDateTime timestamp = conversationRs.getTimestamp("time").toLocalDateTime();
				int conversationId = conversationRs.getInt("conversationID");

				messages.add(new Message(id, userId, message, timestamp, conversationId));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return new Conversation(conversation.getId(), conversation.getType(), null, messages); //Should also return participants at some point.
	}

	public static void sendMessage(Message message) {
		try {
			String query = ""
							+ "INSERT INTO Messages "
							+ "VALUES (DEFAULT, " + message.getConversationId() + ", " + message.getSenderId() + ", '" + message.getMessage() + "', NOW())";
//			String query = ""
//							+ "INSERT INTO Messages "
//							+ "VALUES (DEFAULT, 1, 2, 'testbesked', NOW())";

			int id = dbm.executeInsertAndGetId(query);

			ResultSet messageRs = dbm.executeQuery("SELECT * FROM messages WHERE messageID = " + id);

			if (messageRs.next()) {
				int returnMessageId = messageRs.getInt("messageID");
				int returnConversationId = messageRs.getInt("conversationID");
				int returnSenderId = messageRs.getInt("userID");
				String returnMessage = messageRs.getString("message");
				LocalDateTime returnTimestamp = messageRs.getTimestamp("time").toLocalDateTime();
				broadcastMessage(new Message(returnMessageId, returnSenderId, returnMessage, returnTimestamp, returnConversationId));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static void broadcastMessage(Message message) {
		try {
//			clientsMap.get(message.getSenderId()).receiveMessage(message);
			for (IChatClient client : clientsMap.values()) {
				client.receiveMessage(message);
			}
		} catch (RemoteException ex) {
			Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
