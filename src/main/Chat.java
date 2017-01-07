package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import types.Contact;
import types.Message;
import network.Client;
import misc.Constants;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JTabbedPane;

public class Chat extends JFrame {

	private JPanel contentPane;
	private JTextField inputTextField;
	private JTextArea chatHistoryLogTextarea;
	private JButton btnSend;
	private JButton btnAddNewContact;
	private JScrollPane scrollPane;
	private JScrollPane chatHistoryScrollPane;
	private JTextField newContactTextField;
	private static Client client = new Client();
	private JList<String> contactsList;
	private static Contact currentUser;
	private DefaultListModel<String> contactListModel = new DefaultListModel<String>();
	static private JTabbedPaneCloseButton tabbedPane;
	private HashMap<String, ChatTab> nickToTabMap = new HashMap<String, ChatTab>();
	
	
	public static JTabbedPane getTabbedPane(){
		return tabbedPane;
	}
	
	public static Client getClient(){
		return client;
	}
	
	public static void setCurrentUser(int id, String username, Socket socket){
		currentUser = new Contact(id, username, socket);
	}
	
	/**
	 * Create the frame.
	 */
	public Chat() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				client.disconnect();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 802, 586);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{657, 0, 0};
		gbl_contentPane.rowHeights = new int[]{36, 0, 0, 0, 0, 0, 400, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		tabbedPane = new JTabbedPaneCloseButton();//new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridheight = 7;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		contentPane.add(tabbedPane, gbc_tabbedPane);
		
		/*chatHistoryScrollPane = new JScrollPane();
		tabbedPane.addTab("New tab", null, chatHistoryScrollPane, null);
		chatHistoryScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		chatHistoryLogTextarea = new JTextArea();
		chatHistoryScrollPane.setViewportView(chatHistoryLogTextarea);
		chatHistoryLogTextarea.setEditable(false);
		*/
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//send text to user
				
				writeText();
				inputTextField.requestFocus();
			}
		});
		
		inputTextField = new JTextField();
		inputTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyChar() == KeyEvent.VK_ENTER){
					btnSend.doClick();
					return;
				}
			}
		});
		
		btnAddNewContact = new JButton("Add New Contact");
		btnAddNewContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(newContactTextField.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, Constants.ERROR_EMPTY_CONTACT_TEXTFIELD);
					return;
				}
				
				if(contactListModel.contains(newContactTextField.getText().toLowerCase())){
					JOptionPane.showMessageDialog(null, "You already have this contact");
					return;
				}
				
				//request server to add contact
				Boolean addedStatus = client.addContact(currentUser.getUsername(), newContactTextField.getText());
				//get true/false confirmation from server
				if(addedStatus!=null && addedStatus==true){
					//if true, add username to list
					contactListModel.addElement(newContactTextField.getText().toLowerCase());
				}else{
					//if false, show error
					JOptionPane.showMessageDialog(null, "Contact doesn't exist");
				}
			}
		});
		GridBagConstraints gbc_btnAddNewContact = new GridBagConstraints();
		gbc_btnAddNewContact.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddNewContact.gridx = 1;
		gbc_btnAddNewContact.gridy = 2;
		contentPane.add(btnAddNewContact, gbc_btnAddNewContact);
		
		newContactTextField = new JTextField();
		GridBagConstraints gbc_newContactTextField = new GridBagConstraints();
		gbc_newContactTextField.insets = new Insets(0, 0, 5, 0);
		gbc_newContactTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_newContactTextField.gridx = 1;
		gbc_newContactTextField.gridy = 4;
		contentPane.add(newContactTextField, gbc_newContactTextField);
		newContactTextField.setColumns(10);
		
		
		JLabel lblContacts = new JLabel("Contacts");
		GridBagConstraints gbc_lblContacts = new GridBagConstraints();
		gbc_lblContacts.insets = new Insets(0, 0, 5, 0);
		gbc_lblContacts.gridx = 1;
		gbc_lblContacts.gridy = 5;
		contentPane.add(lblContacts, gbc_lblContacts);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 6;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		contactsList = new JList<String>();
		
		contactsList.addMouseListener(new MouseAdapter()
		 {
			
			public void mouseClicked(MouseEvent e) {
				@SuppressWarnings("unchecked")
				JList<String> theList = (JList<String>) e.getSource();
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
					if (e.getClickCount() == 2) {
						int index = theList.locationToIndex(e.getPoint());
						if (index >= 0) {
							Object o = theList.getModel().getElementAt(index);
							//check if tab already exists
							if(tabbedPane.indexOfTab(o.toString())==-1){
								//add tab if not
								addChatTab(o.toString());
							}
							//switch to chat of selected contact
							tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(o.toString()));
						}
					}
				}
			}
		 });
		
		scrollPane.setColumnHeaderView(contactsList);
		contactsList.setModel(contactListModel);
		
		
		GridBagConstraints gbc_inputTextField = new GridBagConstraints();
		gbc_inputTextField.insets = new Insets(0, 0, 5, 5);
		gbc_inputTextField.fill = GridBagConstraints.BOTH;
		gbc_inputTextField.gridx = 0;
		gbc_inputTextField.gridy = 7;
		contentPane.add(inputTextField, gbc_inputTextField);
		inputTextField.setColumns(10);
		
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 5, 0);
		gbc_btnSend.fill = GridBagConstraints.BOTH;
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 7;
		contentPane.add(btnSend, gbc_btnSend);
	
		//init client
		//1. load contacts of client
		//		get contacts from server 
		List<Contact> contactList = client.getContacts(currentUser.getUsername());
		Iterator<Contact> iter = contactList.iterator();
		System.out.println("contactList size="+contactList.size());
		while(iter.hasNext()){
			Contact c = iter.next();
			//contactListModel.addElement(c.getUsername().toLowerCase());
			contactListModel.addElement(c.getUsername());
		}
		addChatTab("tafat");
		
		//addChatTab("merav");
		updateChat(new Message("sup","merav",currentUser.getUsername()));
		updateChat(new Message("baby","tafat",currentUser.getUsername()));
		updateChat(new Message("im here","yamit",currentUser.getUsername()));
	}
	
	private void writeText(){
		if(tabbedPane.getTabCount()==0){
			JOptionPane.showMessageDialog(null, "Select a contact to send a message to");
			return;
		}
		int selectedIndex = tabbedPane.getSelectedIndex();
		String nickNameTo = tabbedPane.getTitleAt(selectedIndex);
		
		if(!inputTextField.getText().isEmpty()){
			//send message to server and get response from server
			Message m = new Message(inputTextField.getText(), currentUser.getUsername().toLowerCase(), nickNameTo.toLowerCase()); //text, from, to
			Boolean isSent = client.sendMessage(m);
			//String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			String msgToAppend= prepareMessage(inputTextField.getText(),isSent);
			//show message in log
			nickToTabMap.get(nickNameTo).appendMessage(msgToAppend);
						
			//reset input field
			inputTextField.setText("");
		}
	}
	
	public String prepareMessage(String text, boolean isSent){
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		
		if(isSent){
			return "<" + timeStamp + "> " + text + "\n";
		}else{
			return "<" + timeStamp + "> " + "Couldn't send your message!" + "\n";
		}
	}
	
	
	public void updateChat(Message m){
		//add tab if not exists
		if(tabbedPane.indexOfTab(m.getFrom().toLowerCase())==-1){
			addChatTab(m.getFrom());
		}
		
		//add content to chat history
		nickToTabMap.get(m.getFrom()).appendMessage(prepareMessage(m.getText(),true));
		
		//flash tab
		if(tabbedPane.getSelectedIndex()!=tabbedPane.indexOfTab(m.getFrom().toLowerCase())){
			flashTab(m.getFrom());
		}
	}
	

	
	private void flashTab(String from) {
		nickToTabMap.get(from).startTimer();
	}

    
	private void addChatTab(String contactNickname){
		JTextArea chatHistoryLogTextarea2;
		
		if(nickToTabMap.containsKey(contactNickname)){
			chatHistoryLogTextarea2 = nickToTabMap.get(contactNickname).getChatHistoryLogTextarea();
		}else{
			
			chatHistoryLogTextarea2 = new JTextArea();
			ChatTab ct = new ChatTab(contactNickname, chatHistoryLogTextarea2);
			nickToTabMap.put(contactNickname, ct);
		}
		
		JScrollPane chatHistoryScrollPane2 = new JScrollPane();
		tabbedPane.addTab(contactNickname, null, chatHistoryScrollPane2, null);
		chatHistoryScrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		//JTextArea chatHistoryLogTextarea2 = new JTextArea();
		chatHistoryScrollPane2.setViewportView(chatHistoryLogTextarea2);
		chatHistoryLogTextarea2.setEditable(false);

	}
}
