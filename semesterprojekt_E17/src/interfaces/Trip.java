/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jungn
 */
public class Trip implements Serializable{


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

    public Trip(String title, String category, Date timedate, String location, double price, String description, List<String> gear, List<User> participants, List<User> instructors, List<User> organizers) {
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
    }
    
    public void participate(User user) {
	participants.add(user);
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
