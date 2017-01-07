package network;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import network.NetworkItem.NetworkItemType;
import misc.Constants;
import security.*;
import types.Contact;
import types.Message;

public class Client {
	
	private static Socket clientSocket = null;
	private static ServerSocket messageSocket = null;
	

	
	public Client(){
		
		//create socket
		try {
			if(clientSocket==null){
				clientSocket = new Socket("localhost", Constants.APPLICATION_NETWORK_PORT);
			}
		} catch (Exception e) {
			//print error message
		}
		
		//create socket for receiving messages
		if(messageSocket==null){
			try {
				messageSocket = new ServerSocket(Constants.APP_CLIENT_LISTEN_PORT_MESSAGES);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	public static void readMessagesLoop() throws IOException{
		while(true){
			Socket serverSocket = messageSocket.accept();
			//get message
			NetworkItem ni = networkOperations.recieveItem(serverSocket);
			if(ni.getNetworkItemType().equals(NetworkItemType.Message)){
				//handle message
				Message m = (Message) ni.getNetworkItem();
				//String from = m.getFrom();
				//update text in chat gui
				
				//send back success
				m.setSendStatus(true);
				NetworkItem response = new NetworkItem(NetworkItemType.Message, m);
				networkOperations.sendItem(response, getSocket());
			}
			//done
			serverSocket.close();
		}
	}
	
	public static ServerSocket getMessageSocket(){
		return messageSocket;
	}
	
	public static Socket getSocket(){
		return clientSocket;
	}

	
	static CryptHelper cryptHelper = new CryptHelper();
	static Operations networkOperations = new Operations();
	
	public boolean autenticate(String username, char[] password) {		
		//hash password
		byte[] hash_pass = null;
		try {
			hash_pass = cryptHelper.hash_string(password);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, misc.Constants.ERROR_OOPS);
			return false;
		}
		
		//validate credentials with server
		try {
			NetworkItem networkItem = new NetworkItem(NetworkItem.NetworkItemType.NetworkLoginInfo, new NetworkLoginInfo(username, hash_pass));
			//send creds to sever
			networkOperations.sendItem(networkItem, getSocket());
			//get response from server
			NetworkItem recNetItem = (NetworkItem) networkOperations.recieveItem(getSocket());
			//check if user is allowed to log
			if(recNetItem.type.equals(NetworkItem.NetworkItemType.NetworkLoginInfo)){
				return ((NetworkLoginInfo)recNetItem.getNetworkItem()).isLoginSuccess();
			}
		} catch (Exception e) {
			return false;
		}
		
		return false;
	}

	public int getId(String username) {
		int id = -1;
		//send req for id
		networkOperations.sendItem(new NetworkItem(NetworkItem.NetworkItemType.Id, username), getSocket());
		//get response of id
		NetworkItem idItem = networkOperations.recieveItem(getSocket());
		id = (Integer) idItem.getNetworkItem();
		
		return id;
	}

	public Boolean addContact(String myUsername, String contactNickname) {
		Boolean c = null;
		//send request to add contact from server
		networkOperations.sendItem(new NetworkItem(NetworkItem.NetworkItemType.AddContact, new String[] {myUsername,contactNickname}), clientSocket);
		//get response from server (contact if OK, null if not ok)
		NetworkItem networkItem = (NetworkItem) networkOperations.recieveItem(getSocket());
		if(networkItem.getNetworkItemType().equals(NetworkItem.NetworkItemType.AddContact)){
			c = (Boolean) networkItem.getNetworkItem();
		}
		
		return c;
	}

	public Boolean sendMessage(Message M){
		Boolean result = false;
		NetworkItem response;
		//prepare network item of Message type
		NetworkItem netItem = new NetworkItem(NetworkItem.NetworkItemType.Message, M);
		//send message to server
		networkOperations.sendItem(netItem, getSocket());
		//get reply of fail/success
		response = networkOperations.recieveItem(getSocket());
		if(response.getNetworkItemType().equals(NetworkItemType.Message)){
			result = ((Message) response.getNetworkItem()).getSendStatus();
		}
		
		return result;
	}
	
	public void disconnect() {
		//send terminate connection request
		networkOperations.sendItem(new NetworkItem(NetworkItem.NetworkItemType.TerminateConnection, null), getSocket());
		//close connection
		try {
			getSocket().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Message getMessage(){
		Message m = null;
		
		return m;
	}

	@SuppressWarnings("unchecked")
	public List<Contact> getContacts(String username) {
		List<Contact> contacts = null;
		//get all contacts of this username
		NetworkItem ni = new NetworkItem(NetworkItemType.GetContacts, username);
		networkOperations.sendItem(ni, getSocket());
		// get contacts
		NetworkItem response = networkOperations.recieveItem(getSocket());
		if(response.getNetworkItemType().equals(NetworkItemType.GetContacts)){
			contacts = (List) response.getNetworkItem();
		}
		
		return contacts;
	}
}