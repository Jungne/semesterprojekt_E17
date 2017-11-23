package interfaces;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Trip implements Serializable {

	private int id;
	private String title;
	private String description;
	private List<Category> categories = new ArrayList<>();
	private double price;
	private LocalDateTime timeStart;
	private Location location;
	private String meetingAddress;
	private int participantLimit;
	private User organizer;
	private List<User> participants = new ArrayList<>();
	private List<InstructorListItem> instructors = new ArrayList<>();
	private List<OptionalPrice> optionalPrices = new ArrayList<>();
	private Conversation conversation;
	private Set<String> tags = new HashSet<>();
	private List<byte[]> images = new ArrayList<>();

	/**
	 * Constructor to add all information
	 *
	 * @param id
	 * @param title
	 * @param description
	 * @param price
	 * @param timeStart
	 * @param location
	 * @param meetingAddress
	 * @param participantLimit
	 * @param organizer
	 * @param participants
	 * @param instructors
	 * @param optionalPrices
	 * @param conversation
	 * @param categories
	 * @param tags
	 * @param images
	 */
	public Trip(int id, String title, String description, List<Category> categories, double price, LocalDateTime timeStart, Location location, String meetingAddress, int participantLimit, User organizer, List<User> participants, List<InstructorListItem> instructors, List<OptionalPrice> optionalPrices, Conversation conversation, Set<String> tags, List<byte[]> images) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.categories = categories;
		this.price = price;
		this.timeStart = timeStart;
		this.location = location;
		this.meetingAddress = meetingAddress;
		this.participantLimit = participantLimit;
		this.organizer = organizer;
		this.participants = participants;
		this.instructors = instructors;
		this.optionalPrices = optionalPrices;
		this.conversation = conversation;
		this.tags = tags;
		this.images = images;
	}

	public Trip(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public Trip(int id, String title, String description, double price, byte[] image) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		images.add(image);
	}

	public Trip(int id, String title, String description, List<Category> categories, double price, byte[] image) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.categories = categories;
		this.price = price;
		images.add(image);
	}

	/**
	 * Constructor used by client to create new trip and send the necessary
	 * information to the server.
	 *
	 * @param title
	 * @param description
	 * @param categories
	 * @param price
	 * @param timeStart
	 * @param location
	 * @param meetingAddress
	 * @param participantLimit
	 * @param organizer
	 * @param organizerInstructorIn
	 * @param optionalPrices
	 * @param tags
	 * @param images
	 * @throws java.lang.Exception
	 */
	public Trip(String title, String description, List<Category> categories, double price, LocalDateTime timeStart, Location location, String meetingAddress, int participantLimit, User organizer, List<Category> organizerInstructorIn, List<OptionalPrice> optionalPrices, Set<String> tags, List<byte[]> images) throws Exception {
		this.title = title;
		this.description = description;
		this.categories = categories;
		this.price = price;
		this.timeStart = timeStart;
		this.location = location;
		this.meetingAddress = meetingAddress;
		this.participantLimit = participantLimit;
		this.organizer = organizer;
		//Promotes the organizer as instructor the desired categories
		for (Category category : organizerInstructorIn) {
			this.instructors.add(new InstructorListItem(organizer, category));
		}
		this.optionalPrices = optionalPrices;
		this.tags = tags;
		this.images = images;
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
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
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
	public LocalDateTime getTimeStart() {
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
	public int getParticipantLimit() {
		return participantLimit;
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
	public List<InstructorListItem> getInstructors() {
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
	 * @return the tags
	 */
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * @return the images
	 */
	public List<byte[]> getImages() {
		return images;
	}

}
