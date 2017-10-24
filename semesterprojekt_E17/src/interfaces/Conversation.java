/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author jungn
 */
public class Conversation implements Serializable{
    private List<User> participants;
    private List<Message> messages;

    public Conversation(List<User> participants, List<Message> messages) {
	this.participants = participants;
	this.messages = messages;
    }
    
    
}
