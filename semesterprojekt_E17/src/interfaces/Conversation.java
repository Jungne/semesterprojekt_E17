package interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Conversation implements Serializable {

	private int id;
	private String type;
	private List<User> participants = new ArrayList<>();
	private List<Message> messages = new ArrayList<>();

	public Conversation(int id, String type, List<User> participants, List<Message> messages) {
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
	
	/**
	 * @return the participants
	 */
	public List<User> getParticipants() {
		return participants;
	}

	/**
	 * @return the messages
	 */
	public List<Message> getMessages() {
		return messages;
	}

}
