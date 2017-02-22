package types;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import main.MainContacts;

public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Contact fromContact, toContact;
	private String text;
	private Date timestamp;
	private Boolean sentSuccessful = false;
	
	public Message(String text, Contact contact){
		this.fromContact = MainContacts.getCurrentUser();
		this.toContact = contact;
		this.text = text;
		this.timestamp = Calendar.getInstance().getTime();
	}
	
	public String getText(){
		return this.text;
	}
	
	public Boolean getSendStatus(){
		return this.sentSuccessful;
	}
	
	public void setSendStatus(boolean status){
		this.sentSuccessful = status;
	}
	
	public Contact getFromContact(){
		return this.fromContact;
	}
	
	public Contact getToContact(){
		return this.toContact;
	}
	
	public Date getTimestap(){
		return this.timestamp;
	}
	
	@Override
	public String toString() {
		return "From: " + fromContact.toString() + " ,to " + toContact.toString() + " timestamp: " + timestamp.toString();
	}
}
