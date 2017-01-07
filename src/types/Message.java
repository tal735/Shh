package types;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Message implements Serializable {
	Contact contact;
	String text;
	String from, to;
	Boolean sentSuccessful = false;
	Date timestamp;
	
	public Message(String text, String from, String to){
		this.text = text;
		this.from = from;
		this.to = to;
		this.timestamp = Calendar.getInstance().getTime();
	}
	
	public String getFrom(){
		return this.from;
	}
	
	public String getTo(){
		return this.to;
	}
	
	public Message(String text, Contact contact){
		this.contact = contact;
		this.text = text;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getText(){
		return this.text;
	}
	
	public void setContact(Contact c){
		this.contact = c;
	}
	
	public Contact getContact(){
		return this.contact;
	}
	
	public Boolean getSendStatus(){
		return this.sentSuccessful;
	}
	
	public void setSendStatus(boolean status){
		this.sentSuccessful = status;
	}
}
