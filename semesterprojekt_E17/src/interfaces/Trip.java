package interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.scene.image.Image;

public class Trip implements Serializable {

	private int id;
	private String title;
	private String description;
	private double price;
	private Date timeStart;
	private Location location;
	private String meetingAddress;
	private int participantlimit;
	private User organizer;
	private List<User> participants;
	private List<User> instructors;
	private List<OptionalPrice> optionalPrices;
	private Conversation conversation;
	private List<Category> categories;
	private List<String> tags;
	private List<byte[]> images;

	public Trip(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public Trip(int id, String title, String description, double price, byte[] image) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.images = new ArrayList<>();
		images.add(image);
	}

	public Trip(int id, String title, String description, double price, Date timeStart, Location location, String meetingAddress, int participantlimit, User organizer, List<User> participants, List<User> instructors, List<OptionalPrice> optionalPrices, Conversation conversation, List<Category> categories, List<String> tags) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.timeStart = timeStart;
		this.location = location;
		this.meetingAddress = meetingAddress;
		this.participantlimit = participantlimit;
		this.organizer = organizer;
		this.participants = participants;
		this.instructors = instructors;
		this.optionalPrices = optionalPrices;
		this.conversation = conversation;
		this.categories = categories;
		this.tags = tags;
	}

	public void participate(User user) {
		getParticipants().add(user);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return the timeStart
	 */
	public Date getTimeStart() {
		return timeStart;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the meetingAddress
	 */
	public String getMeetingAddress() {
		return meetingAddress;
	}

	/**
	 * @return the participantlimit
	 */
	public int getParticipantlimit() {
		return participantlimit;
	}

	/**
	 * @return the organizer
	 */
	public User getOrganizer() {
		return organizer;
	}

	/**
	 * @return the participants
	 */
	public List<User> getParticipants() {
		return participants;
	}

	/**
	 * @return the instructors
	 */
	public List<User> getInstructors() {
		return instructors;
	}

	/**
	 * @return the optionalPrices
	 */
	public List<OptionalPrice> getOptionalPrices() {
		return optionalPrices;
	}

	/**
	 * @return the conversation
	 */
	public Conversation getConversation() {
		return conversation;
	}

	/**
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @return the images
	 */
	public List<byte[]> getImages() {
		return images;
	}

}
