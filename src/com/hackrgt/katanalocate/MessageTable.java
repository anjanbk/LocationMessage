package com.hackrgt.katanalocate;

/**
 * Represents a message
 *
 */
public class MessageTable {
	//Table_Message Column Names
    
	private int id;
	private long dateTime;
	private double locLat;
	private double locLong;
	private String subject;
	private String message;
	private String user;
	private int type;
	
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
	public MessageTable(int id, long dateTime, double locLat, double locLong, String subject, String message, String user, int type) {
		this.id = id;
		this.dateTime = dateTime;
		this.locLat = locLat;
		this.locLong = locLong;
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
	public MessageTable(long dateTime, double locLat, double locLong, String subject, String message, String user) {
		this.dateTime = dateTime;
		this.locLat = locLat;
		this.locLong = locLong;
		this.subject = subject;
		this.message = message;
		this.user = user;
	}
	
	/**
	 * Complete constructor for a message
	 * @param id message ID
	 * @param dateTime The timestamp for the message
	 * @param location Location to receive message
	 * @param subject The subject
	 * @param message Main content of the message
	 * @param type Determines if sent or recieved
	 */
	public MessageTable(int id, long dateTime, double locLat, double locLong, String subject, String message, int type) {
		this.id = id;
		this.dateTime = dateTime;
		this.locLat = locLat;
		this.locLong = locLong;
		this.subject = subject;
		this.message = message;
		this.type = type;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}
	
	public long getDateTime() {
		return this.dateTime;
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

	public double getLatitude() {
		return locLat;
	}

	public void setLatitude(double locLat) {
		this.locLat = locLat;
	}

	public double getLongitude() {
		return locLong;
	}

	public void setLongitude(double locLong) {
		this.locLong = locLong;
	}

}
