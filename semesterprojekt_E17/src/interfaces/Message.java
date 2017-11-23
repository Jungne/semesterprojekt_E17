package interfaces;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

	private int id;
	private User sender;
	private String message;
	private LocalDateTime timestamp;

	public Message(int id, User sender, String message, LocalDateTime timestamp) {
		this.id = id;
		this.sender = sender;
		this.message = message;
		this.timestamp = timestamp;
	}

	public int getSenderId() {
		return sender.getId();
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
}
