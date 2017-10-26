package interfaces;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Trip implements Serializable {

    private int id;
    private String title;
    private String category;
    private Date timedate;
    private String location;
    private double price;
    private String description;
    private List<String> gear;
    private List<User> participants;
    private List<User> instructors;
    private List<User> organizers;
    private List<String> tags;

    public Trip(int id, String title) {
	this.id = id;
	this.title = title;
    }
    
    public Trip(int id, String title, String category, Date timedate, String location, double price, String description, List<String> gear, List<User> participants, List<User> instructors, List<User> organizers, List<String> tags) {
	this.id = id;
	this.title = title;
	this.category = category;
	this.timedate = timedate;
	this.location = location;
	this.price = price;
	this.description = description;
	this.gear = gear;
	this.participants = participants;
	this.instructors = instructors;
	this.organizers = organizers;
	this.tags = tags;
    }
   
    public void participate(User user) {
	participants.add(user);
    }

    public int getID() {
        return this.id;
    }
    
    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @return the category
     */
    public String getCategory() {
	return category;
    }

    /**
     * @return the timedate
     */
    public Date getTimedate() {
	return timedate;
    }

    /**
     * @return the location
     */
    public String getLocation() {
	return location;
    }

    /**
     * @return the price
     */
    public double getPrice() {
	return price;
    }

    /**
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return the gear
     */
    public List<String> getGear() {
	return gear;
    }
}
