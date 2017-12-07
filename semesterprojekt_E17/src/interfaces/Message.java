package interfaces;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable, Comparable {

	private int id;
	private int senderId;
	private User sender;
	private String message;
	private LocalDateTime timestamp;
	private int conversationId;

	public Message(int id, User sender, String message, LocalDateTime timestamp, int conversationId) {
		this.id = id;
		this.sender = sender;
		this.message = message;
		this.timestamp = timestamp;
		this.conversationId = conversationId;
	}

	public Message(int senderId, String message, int conversationId) {
		this.senderId = senderId;
		this.message = message;
		this.conversationId = conversationId;		
	}

	public int getSenderId() {
		return sender != null ? sender.getId() : senderId;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public int getConversationId() {
		return conversationId;
	}

	public int getId() {
		return id;
	}	

	public User getSender() {
		return sender;
	}
	
	@Override
	public int compareTo(Object o) {
		Message m = (Message) o;
		if (timestamp.isAfter(m.getTimestamp())) {
			return -1;
		} else if (timestamp.isBefore(m.getTimestamp())) {
			return 1;
		} else {
			return 0;
		}
	}	
}
