package shared;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Conversation holds the messages and the participants of the conversation.
 * Conversation is used by trip as a group conversation and is also used when
 * users directly message each other.
 *
 * @author group 12
 */
public class Conversation implements Serializable {

	private int id;
	private String type;
	private Set<User> participants = new HashSet<>();
	private Set<Message> messages = new TreeSet<>();

	public Conversation(int id, String type, Set<User> participants, Set<Message> messages) {
		this.id = id;
		this.type = type;
		this.participants = participants;
		this.messages = messages;
	}

	public Conversation(int id, String type) {
		this.id = id;
		this.type = type;
	}

	/**
	 * @return the conversation id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return a list of all the participants in a trip
	 */
	public Set<User> getParticipants() {
		return participants;
	}

	/**
	 * @return a message
	 */
	public Set<Message> getMessages() {
		return messages;
	}

}
