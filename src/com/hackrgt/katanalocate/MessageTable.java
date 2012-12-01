package com.hackrgt.katanalocate;

public class MessageTable {
	//Table_Message Column Names
    private int ID;
    private int TimeStamp;
    private String Location;
    private String Subject;
    private String Content;
    private int TypeID;
    
    public MessageTable(){}
    public MessageTable(int _ID, int _TimeStamp, String _Location, String _Subject, String _Content, int _TypeID) {
    	ID = _ID;
    	TimeStamp = _TimeStamp;
    	Location = _Location;
    	Subject = _Subject;
    	Content = _Content;
    	TypeID = _TypeID;
    }
    
    public int getType()
    {
    	return TypeID;
    	
    }
    
    public int getTimeStamp()
    {
    	return TimeStamp;
    }
    
    public String getText()
    {
    	return Content;
    
    }
     
    public String getLocation()
    {
    	return Location;
    }
    
    public int getID()
    {
    	return ID;
    }
    
    public String getSubject()
    {
    	return Subject;
    }
}
