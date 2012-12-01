package com.hackrgt.katanalocate;

public class UserTable {
	private String UserID;
    private String GcmRegId;
    private String Name;
    
    public String getName()
    {
    	return Name;
    }
    
    public UserTable(String A, String B)
    {
    	UserID = A;
    	GcmRegId = B;
    }
    
    public UserTable()
    {
    	
    }
    
    public String getUserID()
    {
    	return UserID;
    }
    
    public String getGcmRegId()
    {
    	return GcmRegId; 
    }
}
