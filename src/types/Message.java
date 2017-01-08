package types;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import main.Chat;

public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String text;
	private Boolean sentSuccessful = false;
	private Date timestamp;
	private Contact fromContact, toContact;
	
	public Message(String text, Contact contact){
		this.fromContact = Chat.getCurrentUser();
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
	
}
