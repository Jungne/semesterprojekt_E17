package server;

import interfaces.IChatClient;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hjaltefromholtrindom
 */
public class ServerMessagingHandler {

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
}
