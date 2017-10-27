package interfaces;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javafx.scene.image.Image;

public class Trip implements Serializable {

	private int id;
	private String title;
	private String description;
	private double price;
	private User organizer;
	private Date timeStart;
	private String meetingAddress;
	private Location location;
	private int participantlimit;
	private Conversation conversation;
	private String category;
	private List<User> participants;
	private List<User> instructors;
	private List<Category> categories;
	private List<OptionalPrice> optionalPrices;
	private Image image;
	private List<String> tags;

	public Trip(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public Trip(int id, String title, String description, double price, Image image) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.image = image;
	}

	public Trip(int id, String title, String description, double price, User organizer, Date timeStart, String meetingAddress, Location location, int participantlimit, Conversation conversation, String category, List<User> participants, List<User> instructors, List<Category> categories, List<OptionalPrice> optionalPrices, List<String> tags) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.organizer = organizer;
		this.timeStart = timeStart;
		this.meetingAddress = meetingAddress;
		this.location = location;
		this.participantlimit = participantlimit;
		this.conversation = conversation;
		this.category = category;
		this.optionalPrices = optionalPrices;
		this.participants = participants;
		this.instructors = instructors;
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
	 * @return the organizer
	 */
	public User getOrganizer() {
		return organizer;
	}

	/**
	 * @return the timeStart
	 */
	public Date getTimeStart() {
		return timeStart;
	}

	/**
	 * @return the meetingAddress
	 */
	public String getMeetingAddress() {
		return meetingAddress;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the participantlimit
	 */
	public int getParticipantlimit() {
		return participantlimit;
	}

	/**
	 * @return the conversation
	 */
	public Conversation getConversation() {
		return conversation;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return the prices
	 */
	public List<OptionalPrice> getOptionalPrices() {
		return optionalPrices;
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

}
