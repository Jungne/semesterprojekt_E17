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
    private User sender;
    private String message;
    private Date timestamp;

    public Message(User senderId, String message) {
	this.sender = senderId;
	this.message = message;
	timestamp = new Date();
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
