package server;

import database.DBManager;
import shared.Conversation;
import shared.remote.IChatClient;
import shared.Message;
import shared.User;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private static Map<Integer, List<Integer>> conversationsMap = new HashMap<>();

	/**
	 * Register clients in the form of remote objects.
	 *
	 * @param client
	 * @throws java.rmi.RemoteException
	 */
	public void registerClient(IChatClient client) throws RemoteException {
		clientsMap.put(client.getUserId(), client);
	}

	/**
	 * Register the active conversations.
	 *
	 * @param conversationID
	 */
	public void addActiveConversation(int conversationID) {
		System.out.print(conversationID);
	}

	public List<Conversation> getUserConversations(User user) {
		String query = ""
						+ "SELECT conversationID, type "
						+ "FROM UsersInConversations "
						+ "NATURAL JOIN Conversations "
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

	//Should be named getConversationMessages, change at some point for clarity
	public static Conversation getConversation(Conversation conversation) {
		String query = ""
						+ "SELECT messageID, conversationID, userID, message, time, userName\n"
						+ "FROM Messages\n"
						+ "NATURAL JOIN Users\n"
						+ "WHERE conversationID = " + conversation.getId();
		ResultSet conversationRs = dbm.executeQuery(query);

		Set<Message> messages = new TreeSet<>();

		try {
			while (conversationRs.next()) {
				int id = conversationRs.getInt("messageID");
				User user = new User(conversationRs.getInt("userID"), conversationRs.getString("userName"));
				String message = conversationRs.getString("message");
				LocalDateTime timestamp = conversationRs.getTimestamp("time").toLocalDateTime();
				int conversationId = conversationRs.getInt("conversationID");

				messages.add(new Message(id, user, message, timestamp, conversationId));
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

			int id = dbm.executeInsertAndGetId(query);

			ResultSet messageRs = dbm.executeQuery("SELECT messageID, conversationID, userID, message, time, userName FROM messages NATURAL JOIN Users WHERE messageID = " + id);

			if (messageRs.next()) {
				int returnMessageId = messageRs.getInt("messageID");
				int returnConversationId = messageRs.getInt("conversationID");
				User returnSender = new User(messageRs.getInt("userID"), messageRs.getString("userName"));
				String returnMessage = messageRs.getString("message");
				LocalDateTime returnTimestamp = messageRs.getTimestamp("time").toLocalDateTime();

				broadcastMessage(new Message(returnMessageId, returnSender, returnMessage, returnTimestamp, returnConversationId));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void broadcastMessage(Message message) {
		try {
//			clientsMap.get(message.getSenderId()).receiveMessage(message);

//			for (IChatClient client : clientsMap.values()) {
//				client.receiveMessage(message);
//			}
			if (!conversationsMap.containsKey(message.getConversationId())) {
				conversationsMap.put(message.getConversationId(), loadConversationUsersIds(message.getConversationId()));
			}

			for (int userId : conversationsMap.get(message.getConversationId())) {
				if (clientsMap.containsKey(userId)) {
					clientsMap.get(userId).receiveMessage(message);
				}
			}

		} catch (RemoteException ex) {
			Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static List<Integer> loadConversationUsersIds(int conversationId) {
		try {
			String query = ""
							+ "SELECT userID "
							+ "FROM UsersInConversations "
							+ "WHERE conversationID = " + conversationId;

			ResultSet usersRs = dbm.executeQuery(query);

			List<Integer> users = new ArrayList<>();

			while (usersRs.next()) {
				users.add(usersRs.getInt("userID"));
			}

			return users;
		} catch (SQLException ex) {
			Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static void deleteConversation(int conversationId) {
		String query = ""
						+ "DELETE FROM Conversations"
						+ "WHERE conversationID = " + conversationId;

		dbm.executeUpdate(query);
	}

	public static Conversation getUserConversation(int userId1, int userId2) {
//		String query = ""
//						+ "SELECT conversationID\n"
//						+ "FROM (SELECT COUNT(userID), conversationID\n"
//						+ "FROM usersInConversations\n"
//						+ "GROUP BY conversationID) AS count\n"
//						+ "NATURAL JOIN UsersInConversations\n"
//						+ "NATURAL JOIN Conversations\n"
//						+ "WHERE count.count = 2 AND userID IN (" + userId1 + "," + userId2 +") AND type = 'users'";

		String query = ""
						+ "SELECT conversationID "
						+ "FROM ("
						+ "	SELECT COUNT(userID), conversationID "
						+ "	FROM ("
						+ "		SELECT userID, conversationID "
						+ "		FROM UsersInConversations "
						+ "		WHERE userID IN (" + userId1 + ", " + userId2 + ")) AS CommonConversations "
						+ "	GROUP by conversationID) AS Count "
						+ "WHERE Count.count = 2";

		ResultSet conversationRs = dbm.executeQuery(query);

		int conversationId = -1;

		try {
			if (conversationRs.next()) {
				conversationId = conversationRs.getInt("conversationID");
			} else {
				String newConversationQuery = "INSERT INTO Conversations VALUES (DEFAULT, 'users'); ";

				conversationId = dbm.executeInsertAndGetId(newConversationQuery);

				String insertUsersQuery = "INSERT INTO UsersInConversations "
								+ "VALUES (" + conversationId + "," + userId1 + "), (" + conversationId + ", " + userId2 + ");";

				dbm.executeUpdate(insertUsersQuery);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}

		return getConversation(new Conversation(conversationId, "users"));
	}

}
