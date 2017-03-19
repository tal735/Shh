package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import misc.Constants;
import misc.Constants.NetworkItemType;
import net.miginfocom.swing.MigLayout;
import network.Client;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.Box;

import java.awt.Component;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;

import clientData.ContactGuiItems;
import types.Contact;
import types.Message;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainContacts extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField newContactTextField;
	private JLabel userProfilePicLabel;
	private Box verticalBox;
	private JLabel hiUserLabel;
	private JLabel dateLabel;
	private Component verticalStrut;
	private Component verticalSeperatorActionPanel;
	private JScrollPane contactsScrollablePane;
	private JPanel contactsMainPanel;
	private ChatWindow chatWindow = new ChatWindow();
	
	private static Client client = new Client();
	private static Contact currentUser;
	private List<Contact> contactList = new ArrayList<Contact>();
	
	private Map<Integer, ContactGuiItems> idToContactGuiItems = new HashMap<Integer, ContactGuiItems>(); 
	
	public static Client getClient(){
		return client;
	}
	
	public static void setCurrentUser(Contact owner) {
		currentUser = owner;
	}

	public static Contact getCurrentUser(){
		return currentUser;
	}
	
	/**
	 * Create the frame.
	 */
	public MainContacts() {
		//set this class for reference from Client class
		//client.setMainContacts(this);
		
		//update server with listening port for incoming messages
		client.doAction(NetworkItemType.MessagePortUpdate, getCurrentUser().getId());
		
		//update that we are online
		client.doAction(NetworkItemType.UpdateStatus, true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(contentPane, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	client.doAction(NetworkItemType.UpdateStatus, false);
		        	client.doAction(NetworkItemType.TerminateConnection, currentUser.getId());
		            System.exit(0);
		        }
		    }
		});
		
		
		setBounds(100, 100, 535, 543);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][168.00,grow][]", "[93.00][baseline][grow]"));
		
		userProfilePicLabel = new JLabel("");
		userProfilePicLabel.setIcon(new ImageIcon(MainContacts.class.getResource("/pictures/profile-user-icon.png")));
		contentPane.add(userProfilePicLabel, "cell 0 0");
		
		verticalBox = Box.createVerticalBox();
		contentPane.add(verticalBox, "flowx,cell 1 0");
		
		hiUserLabel = new JLabel("Hi " + currentUser.getUsername() + " :)");
		hiUserLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		verticalBox.add(hiUserLabel);
		
		verticalStrut = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut);
		
		dateLabel = new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		verticalBox.add(dateLabel);
		
		newContactTextField = new JTextField();
		contentPane.add(newContactTextField, "cell 0 1 2 1,grow");
		newContactTextField.setColumns(10);
		
		JButton btnSearch = new JButton("");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/**
				 * Search and add contact
				 */
				
				//empty user string
				if(newContactTextField.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, Constants.ERROR_EMPTY_CONTACT_TEXTFIELD);
					return;
				}
				if(currentUser.getUsername().equalsIgnoreCase(newContactTextField.getText())){
					JOptionPane.showMessageDialog(null, "Can't add yourself");
					return;
				}
				
				//request server to add contact
				String[] addContactInput = {currentUser.getUsername(), newContactTextField.getText()};
				Contact c = (Contact) client.doAction(NetworkItemType.AddContact, addContactInput);

				
				//make validation
				if(c==null){
					JOptionPane.showMessageDialog(null, "Contact doesn't exist");
					return;
				}
				
				//check that contact is not already in list		
				System.out.println("list of friends after pressing ADD:");
				for(Contact friend : contactList){
					System.out.println("Friend: " + friend.toString());
					if(c.getId()==friend.getId()){
						JOptionPane.showMessageDialog(null, "Contact already exists in your list");
						return;
					}
				}
				
				//contact doesnt exist in list. add contact 
				addContactToList(c);

				
			}
		});
		btnSearch.setIcon(new ImageIcon(MainContacts.class.getResource("/pictures/search-icon (1).png")));
		contentPane.add(btnSearch, "cell 2 1,aligny top");
		
		
		contactsMainPanel = new JPanel();
		contactsScrollablePane = new JScrollPane(contactsMainPanel);
		contentPane.add(contactsScrollablePane, "cell 0 2 3 1,grow");
		contactsMainPanel.setLayout(new BoxLayout(contactsMainPanel, BoxLayout.Y_AXIS));
		

		//get contacts from server 
		getContactList();

		//set Availability icon for each user
		findAvailabilityForContacts(contactList);
		
		//get all queued messages in server
		client.doAction(NetworkItemType.GetQueuedMessages, currentUser.getId());
		
		//refresh contact list availability icons every 1 minute (for contacts that you added, but they don't have you in their list)
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
		  @Override
		  public void run() {
		    findAvailabilityForContacts(contactList);
		  }
		}, 0, 1, TimeUnit.MINUTES);
		
		}

	private void getContactList() {
		@SuppressWarnings("unchecked")
		List<Contact> contactList_temp = (List<Contact>) client.doAction(NetworkItemType.GetFriends, currentUser.getUsername()); 
		
		//Iterator<Contact> iter = contactList.iterator();
		int size = contactList_temp.size();
		System.out.println("list of friends from server:");
		for(int i=0; i<size; i++){
			Contact c = contactList_temp.get(i);
			System.out.println("Got friend from server: " + c.toString());
			addContactToList(c);
		}
	}

	private void findAvailabilityForContacts(List<Contact> contactList) {
		if(contactList.isEmpty()){
			System.out.println("setAvailabilityIcons:\tContact list empty. Not requesting availability from server");
			return;
		}
		//get status icons of each user
		Integer[] friendsIds = new Integer[contactList.size()];
		for(int i=0; i<contactList.size(); i++){
			friendsIds[i]=contactList.get(i).getId();
		}
		
		Boolean[] statuses = (Boolean[]) client.doAction(NetworkItemType.GetStatus, friendsIds);
		//error handling
		if(statuses==null) return;
		//set icon for each user
		for(int i=0; i<statuses.length; i++){
			setAvailabilityIconForId(friendsIds[i],statuses[i]);
		}
	}
	
	private void findAvailabilityForId(int id) {
		Boolean[] statuses = (Boolean[]) client.doAction(NetworkItemType.GetStatus, new Integer[] {id});
		//error handling
		if(statuses==null) return;
		//set icon for each user
		for(int i=0; i<statuses.length; i++){
			setAvailabilityIconForId(id,statuses[0]);
		}
	}
	
	public void setAvailabilityIconForId(Integer id, Boolean isOnline) {
		System.out.println("id = " + id + " , status= " + isOnline);
		
		if(isOnline){
			idToContactGuiItems.get(id).getOnlineIcon().setIcon(new ImageIcon(MainContacts.class.getResource("/pictures/greenbar.png")));
		}
		else{
			idToContactGuiItems.get(id).getOnlineIcon().setIcon(new ImageIcon(MainContacts.class.getResource("/pictures/redbar.png")));
		}
	}

	List<JPanel> listOfItemPanel = new LinkedList<JPanel>(); //debug
	
	private void addContactToList(final Contact c){
		final JPanel listItemPanel = new JPanel();
		listItemPanel.setAlignmentY(0.0f);
		contactsMainPanel.add(listItemPanel);
		listOfItemPanel.add(listItemPanel); //debug
		
		listItemPanel.setLayout(new MigLayout("", "[][grow][]", "[]"));
		//listItemPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JPanel contactPhotoPanel = new JPanel();
		listItemPanel.add(contactPhotoPanel, "cell 0 0,alignx left,growy");
		GridBagLayout gbl_contactPhotoPanel = new GridBagLayout();
		gbl_contactPhotoPanel.columnWidths = new int[]{0, 0};
		gbl_contactPhotoPanel.rowHeights = new int[]{0, 0, 0};
		gbl_contactPhotoPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contactPhotoPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contactPhotoPanel.setLayout(gbl_contactPhotoPanel);
		
		JLabel onlineIcon = new JLabel("");
		onlineIcon.setIcon(new ImageIcon(MainContacts.class.getResource("/pictures/redbar.png")));
		GridBagConstraints gbc_onlineIcon = new GridBagConstraints();
		gbc_onlineIcon.insets = new Insets(0, 0, 5, 0);
		gbc_onlineIcon.gridx = 0;
		gbc_onlineIcon.gridy = 0;
		contactPhotoPanel.add(onlineIcon, gbc_onlineIcon);
		
		JLabel contactPictureLabel = new JLabel("");
		contactPictureLabel.setIcon(new ImageIcon(MainContacts.class.getResource("/pictures/user-48x48.png")));
		GridBagConstraints gbc_contactPictureLabel = new GridBagConstraints();
		gbc_contactPictureLabel.gridx = 0;
		gbc_contactPictureLabel.gridy = 1;
		contactPhotoPanel.add(contactPictureLabel, gbc_contactPictureLabel);
		
		JLabel lblFirstnameLastname = new JLabel(c.getUsername());
		listItemPanel.add(lblFirstnameLastname, "cell 1 0");
		
		JPanel contactActionsPanel = new JPanel();
		listItemPanel.add(contactActionsPanel, "cell 2 0,grow");
		contactActionsPanel.setLayout(new BoxLayout(contactActionsPanel, BoxLayout.Y_AXIS));
		
		JButton chatWithFriendButton = new JButton("");
		chatWithFriendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//open chat window
				chatWindow.switchToContact(c);
				chatWindow.setVisible(true);
			}
		});
		chatWithFriendButton.setIcon(new ImageIcon(MainContacts.class.getResource("/pictures/icon-communication.png")));
		contactActionsPanel.add(chatWithFriendButton);
		
		verticalSeperatorActionPanel = Box.createVerticalStrut(20);
		contactActionsPanel.add(verticalSeperatorActionPanel);
		
		JButton removeFriendButton = new JButton("");
		removeFriendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//remove contact from list (unfriend)
				Object isFriendRemoved = client.doAction(NetworkItemType.Unfriend, c.getId());
				if(((Boolean) isFriendRemoved).equals(Boolean.TRUE)){
					System.out.println("list size = " + contactList.size());
					System.out.println("list before removing:");
					for(Contact friend : contactList){
						System.out.println("Friend: " + friend.toString());
					}
					
					//show new gui without contact
					listItemPanel.removeAll();
					listOfItemPanel.remove(listItemPanel);
					contactsMainPanel.revalidate();
					contactsMainPanel.repaint();
					
					//remove from list
					removedFriend(c);
					
					return;
				}else{
					JOptionPane.showMessageDialog(contentPane, "Can't remove friend. Try again later..");
				}				
			}

			private void removedFriend(Contact c) {
					// TODO Auto-generated method stub
					boolean rc = contactList.remove(c);
					System.out.println("Removed " + c.getUsername() + "? " + rc);
					System.out.println("list of friends after removal:");
					for(Contact friend : contactList){
						System.out.println("Friend: " + friend.toString());
					}
			}
		});
		
		removeFriendButton.setIcon(new ImageIcon(MainContacts.class.getResource("/pictures/icon-clear-x.png")));
		contactActionsPanel.add(removeFriendButton);
		
		//show new contact on gui
		contactsMainPanel.revalidate();

		//add contact to list
		contactList.add(c);
		
		//add to map
		ContactGuiItems cgi = new ContactGuiItems(c.getId(), listItemPanel, onlineIcon, contactPictureLabel, chatWithFriendButton, removeFriendButton);
		idToContactGuiItems.put(c.getId(), cgi);
		
		//find availability of user (online/offline)
		findAvailabilityForId(c.getId());
	}

	public void updateChat(Message m){
		chatWindow.updateChat(m);
		chatWindow.setVisible(true);
	}


}
