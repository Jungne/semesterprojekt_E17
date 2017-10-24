/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jungn
 */
public class Message implements Serializable{
    
    private int id;
    private User sender;
    private String message;
    private Date timestamp;

    public Message(int id, User sender, String message, Date timestamp) {
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

    public Date getTimestamp() {
	return timestamp;
    }
}
