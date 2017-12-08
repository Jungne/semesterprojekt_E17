package interfaces;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The Message class
 *
 * @author group 12
 */
public class Message implements Serializable, Comparable {

	private int id;
	private int senderId;
	private User sender;
	private String message;
	private LocalDateTime timestamp;
	private int conversationId;

	/**
	 * This constructor
	 *
	 * @param id
	 * @param sender
	 * @param message
	 * @param timestamp
	 * @param conversationId
	 */
	public Message(int id, User sender, String message, LocalDateTime timestamp, int conversationId) {
		this.id = id;
		this.sender = sender;
		this.message = message;
		this.timestamp = timestamp;
		this.conversationId = conversationId;
	}

	/**
	 * This constructor
	 *
	 * @param senderId
	 * @param message
	 * @param conversationId
	 */
	public Message(int senderId, String message, int conversationId) {
		this.senderId = senderId;
		this.message = message;
		this.conversationId = conversationId;
	}

	/**
	 * @return the sender id
	 */
	public int getSenderId() {
		return sender != null ? sender.getId() : senderId;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the timestamp
	 */
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the conversation id
	 */
	public int getConversationId() {
		return conversationId;
	}

	/**
	 * @return the message id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the user that is sending the message
	 */
	public User getSender() {
		return sender;
	}

	/**
	 * The method
	 *
	 * @return the user that is sending the message
	 */
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
