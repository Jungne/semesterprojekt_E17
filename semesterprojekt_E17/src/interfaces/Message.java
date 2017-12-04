package interfaces;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

	private int id;
	private int senderId;
	private String message;
	private LocalDateTime timestamp;
	private int conversationId;

	public Message(int id, int senderId, String message, LocalDateTime timestamp, int conversationId) {
		this.id = id;
		this.senderId = senderId;
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
		return senderId;
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
	
}
