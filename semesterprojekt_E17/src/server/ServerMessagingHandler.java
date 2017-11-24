package server;

import interfaces.IChatClient;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hjaltefromholtrindom
 */
public class ServerMessagingHandler {

	/**
	 * All clients of the server, in the form of remote objects
	 */
	Set<IChatClient> clients = new HashSet<IChatClient>();

	/**
	 * Register clients in the form of remote objects
	 */
	public void registerClient(IChatClient client) {
		clients.add(client);
	}
}
