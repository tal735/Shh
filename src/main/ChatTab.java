package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import types.Contact;

public class ChatTab {

	JTextArea chatHistoryLogTextarea;
	private Contact contact;
	

	public ChatTab(Contact c) {
		this.contact = c;
		chatHistoryLogTextarea = new JTextArea();
	}
	
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}


	public void appendMessage(String message){
		chatHistoryLogTextarea.append(message);
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
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tabbedPane.indexOfTab(contact.getUsername())==tabbedPane.getSelectedIndex()){
					timer.stop();
					int index = tabbedPane.indexOfTab(contact.getUsername());
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
		int index = tabbedPane.indexOfTab(contact.getUsername());
		
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
