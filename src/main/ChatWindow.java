package main;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import misc.Constants.NetworkItemType;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.JLabel;

import types.Contact;
import types.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ChatWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane = null;
	private static JTabbedPane chatTabsPane = null;
	private static HashMap<Integer,ChatTabComponents> contactToComponents = new HashMap<Integer, ChatTabComponents>();

	public static JTabbedPane getChatTabsPane(){
		return chatTabsPane;
	}
	/**
	 * Create the frame.
	 */
	public ChatWindow() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setBounds(100, 100, 606, 529);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		
		chatTabsPane = new JTabbedPaneCloseButton();
		contentPane.add(chatTabsPane);
		
	}
	
	public static void addChatTab(final Contact targetContact){
		if(contactToComponents.containsKey(targetContact)){
			System.out.println("found chat berfore");
			chatTabsPane.addTab(targetContact.getUsername(), null, contactToComponents.get(targetContact).getChatTab(), null);
			return;
		}
		
		JPanel chatTab = new JPanel();
		chatTab.setLayout(new MigLayout("", "[grow][][]", "[407.00,grow][grow]"));
		
		chatTabsPane.addTab(targetContact.getUsername(), null, chatTab, null);
		
		JPanel chatHistoryPanel = new JPanel();
		
		JScrollPane scrollableHistoryPane = new JScrollPane(chatHistoryPanel);
		
		chatHistoryPanel.setLayout(new BoxLayout(chatHistoryPanel, BoxLayout.Y_AXIS));
		scrollableHistoryPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatTab.add(scrollableHistoryPane, "cell 0 0 3 1,grow");
		
		scrollableHistoryPane.setViewportView(chatHistoryPanel);
	
		final JTextField textInputField = new JTextField();
		//save necessary fields into a class
		ChatTabComponents ctc = new ChatTabComponents(chatHistoryPanel, textInputField, targetContact, chatTab);
		contactToComponents.put(targetContact.getId(), ctc);
		
		chatTab.add(textInputField, "cell 0 1,growx");
		
		final JButton sendMessageButton = new JButton("");
		sendMessageButton.setIcon(new ImageIcon(ChatWindow.class.getResource("/pictures/generic_dbl_arrow_right.gif")));
		sendMessageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//send text and print to screen
				if(!textInputField.getText().isEmpty()){
					/*Thread thread = new Thread(){
						public void run(){
							//send message to server and get response from server
							Message m = new Message(textInputField.getText(), targetContact); //text, from, to
							Boolean isSent = MainContacts.getClient().sendMessage(m);
							String msgToAppend= prepareMessage(m.getFromContact().getUsername() ,textInputField.getText(),isSent);
							//show message on screen
							addMessage(m, msgToAppend);		
							//reset input field
							textInputField.setText("");
							textInputField.requestFocus();
						}
					};
					//thread.start();	
					*/
					
					//send message to server and get response from server
					Message m = new Message(textInputField.getText(), targetContact); //text, from, to
					Boolean isSent = (Boolean) MainContacts.getClient().doAction(NetworkItemType.Message, m);
					String msgToAppend= prepareMessage(m.getToContact().getUsername() ,textInputField.getText(),isSent);
					//show message on screen
					addMessage(m, msgToAppend);		
					//reset input field
					textInputField.setText("");
					textInputField.requestFocus();
				}
			}
		});
		chatTab.add(sendMessageButton, "cell 1 1");
		
		textInputField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyChar() == KeyEvent.VK_ENTER){
					sendMessageButton.doClick();
					return;
				}
			}
		});
		
		JButton emojiButton = new JButton("");
		emojiButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//open emoji here
				JOptionPane.showMessageDialog(null, "SUP???????");
			}
		});
		emojiButton.setIcon(new ImageIcon(ChatWindow.class.getResource("/pictures/icon_smile.gif")));
		chatTab.add(emojiButton, "cell 2 1");
		
		textInputField.requestFocus();
	}
	
	public static String prepareMessage(String contactNickname, String text, boolean isSent){
		String middle = isSent ? "" : "Failed sending message [" + contactNickname + " is Offline]!\n";
		return middle + text;
	}
	
	public static void addMessage(Message m, String msgToAppend){
		//find tab
		boolean incoming = m.getToContact().getId() == MainContacts.getCurrentUser().getId();
		Contact contact = incoming ?  m.getFromContact() : m.getToContact();
		
		if(!contactToComponents.containsKey(contact.getId())){
			addChatTab(contact);
		}
		
		JPanel chatHistoryPanel = contactToComponents.get(contact.getId()).getChatHistoryPanel();
		
		//add message to tab
		JPanel messagePanel = new JPanel();
		messagePanel.setAlignmentY(0.0f);
		
		messagePanel.setLayout(new MigLayout("", "[144.00,left][grow][280.00,grow]", "[][57.00,top]"));
		
		//JLabel label = new JLabel(new SimpleDateFormat("HH:mm:ss").format(m.getTimestap()));
		JLabel label = new JLabel(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
		messagePanel.add(label, "cell 1 0");
		
		JLabel lblFrom = new JLabel(incoming ? contact.getUsername() : m.getFromContact().getUsername());
		messagePanel.add(lblFrom, "cell 0 0");
		
		JTextField messageText = new JTextField();
		messageText.setBackground(incoming ? Color.ORANGE : Color.PINK);
		messageText.setText(msgToAppend);
		messageText.setEditable(false);
		messagePanel.add(messageText, "cell 0 1 3 1,grow");
		messageText.setColumns(10);
		
		chatHistoryPanel.add(messagePanel);
		//make sure new message is shown on screen
		chatTabsPane.requestFocus();
		
		//scroll down
		chatHistoryPanel.scrollRectToVisible(new Rectangle(0,chatHistoryPanel.getHeight()+92,1,1));
		contactToComponents.get(contact.getId()).getTextInputField().requestFocus();
		
	}

	public void switchToContact(Contact c){
		
		if(chatTabsPane.indexOfTab(c.getUsername())==-1){
			//add tab if not
			addChatTab(c);
		}
		
		//switch to chat of selected contact
		chatTabsPane.setSelectedIndex(chatTabsPane.indexOfTab(c.getUsername()));
		
	}
	
	public void updateChat(Message m){
		String fromNickname = (m.getFromContact().getUsername()).toLowerCase();
		
		//add tab if not exists
		if(chatTabsPane.indexOfTab(fromNickname)==-1){
			addChatTab(m.getFromContact());
		}
		
		//add content to chat history
		addMessage(m, m.getText());
		
		//flash tab
		if(chatTabsPane.getSelectedIndex()!=chatTabsPane.indexOfTab(fromNickname)){
			contactToComponents.get(m.getFromContact().getId()).flashChatTab();
		}
	}
}
