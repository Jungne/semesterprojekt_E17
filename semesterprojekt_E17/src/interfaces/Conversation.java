package interfaces;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public Set<Message> getMessages() {
		return messages;
	}
}
