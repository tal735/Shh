package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import types.Contact;

public class ChatTabComponents {
	//tab, history, inputline
	JPanel chatHistoryPanel;
	JTextField textInputField;
	Contact contact;
	JPanel chatTab;
	
	public JPanel getChatTab() {
		return chatTab;
	}

	public void setChatTab(JPanel chatTab) {
		this.chatTab = chatTab;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public ChatTabComponents(JPanel chatHistoryPanel, JTextField textInputField, Contact contact, JPanel chatTab){
		setChatHistoryPanel(chatHistoryPanel);
		setTextInputField(textInputField);
		setContact(contact);
		setChatTab(chatTab);
	}
	
	public JPanel getChatHistoryPanel() {
		return chatHistoryPanel;
	}
	public void setChatHistoryPanel(JPanel chatHistoryPanel) {
		this.chatHistoryPanel = chatHistoryPanel;
	}
	public JTextField getTextInputField() {
		return textInputField;
	}
	public void setTextInputField(JTextField textInputField) {
		this.textInputField = textInputField;
	}
	

	private Timer timer = null;

	public void flashChatTab(){
		//if timer already running, don't re-flash
		if(timer!=null && timer.isRunning()){
			return;
		}else{
			startTimer();
		}
		
	}
	
	private void startTimer(){

		
		timer = new Timer(500, new ActionListener() {
			JTabbedPane tabbedPane = ChatWindow.getChatTabsPane();
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
		JTabbedPane tabbedPane = ChatWindow.getChatTabsPane();
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

	
}
