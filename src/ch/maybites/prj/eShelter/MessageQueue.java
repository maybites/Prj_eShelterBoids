package ch.maybites.prj.eShelter;

import java.util.ArrayList;
import com.illposed.osc.*;

public class MessageQueue {
	private ArrayList<OSCMessage> messages;
	
	public MessageQueue(){
		messages = new ArrayList<OSCMessage>();
	}
	
	public void addMessage(OSCMessage _message){
		messages.add(_message);
	}
	
	public boolean hasMoreMessages(){
		return (messages.size() > 0)? true: false;
	}
	
	public OSCMessage removeNextMessage(){
		return messages.remove(0);
	}

}
