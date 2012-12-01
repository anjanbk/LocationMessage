package com.hackrgt.katanalocate;

import java.sql.Timestamp;

/**
 * Represents a message
 *
 */
public class Message {
	
	private int id;
	private Timestamp dateTime;
	private String location;
	private String subject;
	private String message;
	private String user;
	private int type;

	/**
	 * Default constructor
	 */
	public Message() {
		
	}
	
	/**
	 * Complete constructor for a message
	 * @param id message ID
	 * @param dateTime The timestamp for the message
	 * @param location Location to receive message
	 * @param subject The subject
	 * @param message Main content of the message
	 * @param user Receiver or sender of message depending on view
	 * @param type Determines if sent or recieved
	 */
	public Message(int id, Timestamp dateTime, String location, String subject, String message, String user, int type) {
		this.id = id;
		this.dateTime = dateTime;
		this.location = location;
		this.subject = subject;
		this.message = message;
		this.user = user;
		this.type = type;
	}
	
	/**
	 * Constructs a message for viewing
	 * @param dateTime Timestamp for the message
	 * @param location Location to receive message
	 * @param subject The subject of the message
	 * @param message Main content of the message
	 * @param user Receiver or sender of message depending on view
	 */
	public Message(Timestamp dateTime, String location, String subject, String message, String user) {
		this.dateTime = dateTime;
		this.location = location;
		this.subject = subject;
		this.message = message;
		this.user = user;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}
	
	public Timestamp getDateTime() {
		return this.dateTime;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
}
