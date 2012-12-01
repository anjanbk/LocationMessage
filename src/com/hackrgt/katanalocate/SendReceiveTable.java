package com.hackrgt.katanalocate;

public class SendReceiveTable {
	private String SenderID;
    private String ReceiverID;
    private int MessageID;
    
    public SendReceiveTable()
    {
    	
    }
    
    public SendReceiveTable(String Sender, String Receiver, int Message)
    {
    	SenderID = Sender;
    	ReceiverID = Receiver;
    	MessageID = Message;
    }
    
    int getMessageID()
    {
    	
    	return MessageID;
    }
}
