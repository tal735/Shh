package clientData;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ContactGuiItems {
	private int id;
	private JPanel listItemPanel;
	private JLabel onlineIcon;
	private JLabel contactPictureLabel;
	private JButton chatWithFriendButton;
	private JButton removeFriendButton;
	
	public ContactGuiItems(){
		
	}
	
	public ContactGuiItems(int id, JPanel listItemPanel, JLabel onlineIcon, 
			JLabel contactPictureLabel, JButton chatWithFriendButton, JButton removeFriendButton)
	{
		this.id = id;
		this.listItemPanel = listItemPanel;
		this.onlineIcon = onlineIcon;
		this.contactPictureLabel = contactPictureLabel;
		this.chatWithFriendButton = chatWithFriendButton;
		this.removeFriendButton = removeFriendButton;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public JPanel getListItemPanel() {
		return listItemPanel;
	}
	public void setListItemPanel(JPanel listItemPanel) {
		this.listItemPanel = listItemPanel;
	}
	public JLabel getOnlineIcon() {
		return onlineIcon;
	}
	public void setOnlineIcon(JLabel onlineIcon) {
		this.onlineIcon = onlineIcon;
	}
	public JLabel getContactPictureLabel() {
		return contactPictureLabel;
	}
	public void setContactPictureLabel(JLabel contactPictureLabel) {
		this.contactPictureLabel = contactPictureLabel;
	}
	public JButton getChatWithFriendButton() {
		return chatWithFriendButton;
	}
	public void setChatWithFriendButton(JButton chatWithFriendButton) {
		this.chatWithFriendButton = chatWithFriendButton;
	}
	public JButton getRemoveFriendButton() {
		return removeFriendButton;
	}
	public void setRemoveFriendButton(JButton removeFriendButton) {
		this.removeFriendButton = removeFriendButton;
	}
	
	
}
