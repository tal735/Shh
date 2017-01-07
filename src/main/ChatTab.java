package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import types.Contact;



public class ChatTab {

	public ChatTab(String toContact, JTextArea chatHistoryLogTextarea) {
		super();
		this.toContact = toContact;
		chatHistory = new StringBuilder();
		this.chatHistoryLogTextarea = chatHistoryLogTextarea;
	}

	public ChatTab(String toContact, StringBuilder chatHistory) {
		super();
		this.toContact = toContact;
		this.chatHistory = chatHistory;
	}

	JTextArea chatHistoryLogTextarea;
	String toContact = null;
	public StringBuilder chatHistory;
	private Contact contact;
	
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getToContact() {
		return toContact;
	}

	public void setToContact(String toContact) {
		this.toContact = toContact;
	}

	public StringBuilder getChatHistory() {
		return chatHistory;
	}

	public void setChatHistory(StringBuilder chatHistory) {
		this.chatHistory = chatHistory;
	}

	public void appendMessage(String message){
		chatHistoryLogTextarea.append(message);
		chatHistory.append(message);
	}

	public JTextArea getChatHistoryLogTextarea() {
		return chatHistoryLogTextarea;
	}

	public void setChatHistoryLogTextarea(JTextArea chatHistoryLogTextarea) {
		this.chatHistoryLogTextarea = chatHistoryLogTextarea;
	}

	private Timer timer = null;
	
	public void startTimer(){
		timer = new Timer(500, new ActionListener() {
			JTabbedPane tabbedPane = Chat.getTabbedPane();
			boolean blinkFlag = false;
			int index = tabbedPane.indexOfTab(toContact);
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tabbedPane.indexOfTab(toContact)==tabbedPane.getSelectedIndex()){
					timer.stop();
					tabbedPane.setBackgroundAt(index, null);
					tabbedPane.setForegroundAt(index, null);
					return;
				}
				blink(blinkFlag);
				blinkFlag = !blinkFlag;
			}
		});
		timer.start();
	}
	
	private void blink(boolean blinkFlag) {
		JTabbedPane tabbedPane = Chat.getTabbedPane();
		int index = tabbedPane.indexOfTab(toContact);
		
		if (index!=-1){
			if(blinkFlag) {
				tabbedPane.setBackgroundAt(index, Color.orange);
				tabbedPane.setForegroundAt(index, Color.pink);
			} else {
				tabbedPane.setBackgroundAt(index, null);
				tabbedPane.setForegroundAt(index, null);
			}
			tabbedPane.repaint();
		}else{
			timer.stop();
		}
	}

	public void stopTimer(){
		if(timer!=null) timer.stop();
	}
	
}
