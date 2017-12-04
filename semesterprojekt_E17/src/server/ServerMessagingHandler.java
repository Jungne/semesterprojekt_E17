package server;

import database.DBManager;
import interfaces.Conversation;
import interfaces.IChatClient;
import interfaces.User;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	Map<Integer, IChatClient> clientsMap = new HashMap<>();

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
}
