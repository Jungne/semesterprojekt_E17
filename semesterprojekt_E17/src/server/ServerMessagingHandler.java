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

	DBManager dbm = DBManager.getInstance();

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

	public List<Conversation> loadMyConversations(User user) {
		String query = ""
						+ "SELECT conversationID\n"
						+ "FROM usersInConversations\n"
						+ "WHERE userId = " + user.getId() + ";";
		
		ResultSet conversationsRs = dbm.executeQuery(query);
		ArrayList<Conversation> conversations = new ArrayList<>();
		
		try {
			while (conversationsRs.next()) {
				conversations.add(new Conversation(conversationsRs.getInt(1)));
			}
		} catch (SQLException ex) {
			Logger.getLogger(ServerMessagingHandler.class.getName()).log(Level.SEVERE, null, ex);
		}
		return conversations;
	}
}
