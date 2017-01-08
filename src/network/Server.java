package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import network.NetworkItem.NetworkItemType;
import security.CryptHelper;
import types.Contact;
import types.Message;
import misc.Constants;

public class Server {
	private static ServerSocket listener=null;
	private static Operations serverOperations = new Operations();
	private static CryptHelper cryptHelper = new CryptHelper();
	
	private HashMap<String, Integer> nickToIdMap = new HashMap<String, Integer>();					// NICKNAME --> ID
	private HashMap<Integer, Contact> idToContactMap = new HashMap<Integer, Contact>();				// ID --> Contact
	private HashMap<String, byte[]> nickToPass = new HashMap<String, byte[]>();						// Nick --> Pass
	private HashMap<Integer, List<Contact>> idToFriends = new HashMap<Integer, List<Contact>>();	// ID --> Contact List (friends)
	private HashMap<Integer, HashMap<Integer, List<Message>>> idToidMessages = 						//ID --> <ID-->List<Messages>>
			new HashMap<Integer, HashMap<Integer, List<Message>>>(); 								
	
	private HashMap<Integer, Integer> idToPort = new HashMap<Integer, Integer>();					//ID-->LISTENING PORT FOP MESSAGES
	static int uniqueId = -1;
	
	public Server(){
		initServerSocket();
	}
	
	public void initServerSocket(){
		if(listener==null){
			try {
				listener = new ServerSocket(Constants.APPLICATION_NETWORK_PORT);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		initTestValues();	//remove this when going live
	}

	private void initTestValues(){
		addUserToDb("tal");
		addUserToDb("yamit");
		addUserToDb("tafat");
	}
	
	
	private void addUserToDb(String nickname){
		String lower_nickname = nickname.toLowerCase();
		uniqueId++;
		nickToIdMap.put(nickname, uniqueId);
		idToContactMap.put(uniqueId, new Contact(uniqueId, lower_nickname, null));
		nickToPass.put(nickname, cryptHelper.hash_string("1234".toCharArray()));
		idToFriends.put(uniqueId, new ArrayList<Contact>());
	}
	
	
	public void mainServerLoop() throws IOException{
		try {
			System.out.println("Server is running...");
			while (true) {
				Socket socket = listener.accept();
				//open client thread
				Thread thread = new Thread(new clientLoop(socket));
				thread.start();
			}
		}
		catch	(Exception e)	{} 
		finally {	listener.close();	}
	}

	
	/**
	 * 
	 * @author talsim
	 * each connected user will be running this loop inside the server. The server will wait for request from client and will handle the requests
	 */
	class clientLoop implements Runnable{
		private Socket socket;
		
		public clientLoop (Socket socket) {
		       this.socket = socket;
		   }
		@Override
		public void run() {
			while(!socket.isClosed()){
				NetworkItem response = null;
				//get item from client
				NetworkItem ni = serverOperations.recieveItem(socket);
				//handle item
				response = handleItem(ni, socket);
				//send response
				serverOperations.sendItem(response, socket);
			}
			System.out.println("Terminated connection with " + socket.toString());
		}
	}
	
	
	//handle requests from user
	private NetworkItem handleItem(NetworkItem ni, Socket socket) {
		
		NetworkItem response = null;
		
		//check login and register socket of nickname in server
		if(ni.getNetworkItemType().equals(NetworkItem.NetworkItemType.NetworkLoginInfo)){
			//check login
			NetworkLoginInfo nli = (NetworkLoginInfo)ni.getNetworkItem();
			
			//check if username exists
			String username = nli.getUsername().toLowerCase();
			Integer id = nickToIdMap.get(username);
			if(id == null){
				//no such username
				nli.setLoginSuccess(false);
				return ni;
			}
			
			//check if password matches
			nli.setLoginSuccess(Arrays.equals((byte[]) nickToPass.get(username), nli.getHashedPassword()));		
			
			//register socket.
			if(nli.isLoginSuccess()){
				idToContactMap.get(id).setSocket(socket);
			}
			
			response = ni;
			
		//request for id
		}else if(ni.getNetworkItemType().equals(NetworkItem.NetworkItemType.Id)){
			String username = ((String)ni.getNetworkItem()).toLowerCase();
			int id = -1;
			
			if(nickToIdMap.containsKey(username)){
				id = nickToIdMap.get(username); 
			}
			
			//send id
			response = new NetworkItem(NetworkItem.NetworkItemType.Id, id);
			
		}else if(ni.getNetworkItemType().equals(NetworkItem.NetworkItemType.AddContact)){
			//[0] = myUsername
			//[1] = contactNickname
			String values[] = (String []) ni.getNetworkItem();
			String curentUsername = values[0].toLowerCase();
			String contactToAdd = values[1].toLowerCase();
			Boolean result = false;
			
			//check if username exists
			Integer idOfFriend = nickToIdMap.get(contactToAdd);
			Contact friend = idToContactMap.get(idOfFriend);
			if(friend!=null){
				//check if user is not already friend
				if(!idToFriends.get(nickToIdMap.get(curentUsername)).contains(friend)){ //this is already checked in client. but just in case
					//add friend
					idToFriends.get(nickToIdMap.get(curentUsername)).add(friend);
					result = true;
					System.out.println("added " + idToFriends.get(nickToIdMap.get(curentUsername)).get(0).getUsername() + " to " + curentUsername);
					
				}
			}
			
			//send true/false		
			response = new NetworkItem(NetworkItem.NetworkItemType.AddContact, friend);
			
		}else if(ni.getNetworkItemType().equals(NetworkItem.NetworkItemType.TerminateConnection)){
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			socket=null;
		//receive and send message
		}else if(ni.getNetworkItemType().equals(NetworkItem.NetworkItemType.Message)){
			Boolean isSent = false;
			//get message from source
			Message m = (Message) ni.getNetworkItem();
			int idFrom = m.getFromContact().getId();
			int idTo = m.getToContact().getId();
			
			//create db of messages for FROM-user if not exists
			if(!idToidMessages.containsKey(idFrom)){
				idToidMessages.put(idFrom, new HashMap<Integer, List<Message>>());
			}
			
			//if user never send message to TO-USER add him
			if(!idToidMessages.get(idFrom).containsKey(idTo)){
				idToidMessages.get(idFrom).put(idTo, null);
			}
			
			//should get here only 1st time
			if(idToidMessages.get(idFrom).get(idTo)==null){
				idToidMessages.get(idFrom).put(idTo, new ArrayList<Message>());
			}
			
			
			//send message to user
			//get socket of dest user
			Socket toSocket = idToContactMap.get(idTo).getSocket();
			if(toSocket!=null){
				//prepare socket of target user
				Socket toMessageSocket = null;
				try {
					toMessageSocket = new Socket(toSocket.getLocalAddress().getHostAddress(), idToPort.get(idTo));
					//send message
					serverOperations.sendItem(ni, toMessageSocket);
					//set message flag to sent
					isSent = true;
				}
				catch (Exception e) {
					isSent = false;
				}
				finally{
					try {
						if(toMessageSocket!=null)
							toMessageSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			m.setSendStatus(isSent);
			idToidMessages.get(idFrom).get(idTo).add(m);
			
			//send back if success or fail
			response = new NetworkItem(NetworkItem.NetworkItemType.Message, m);
		}else if(ni.getNetworkItemType().equals(NetworkItem.NetworkItemType.GetFriends)){
			//get input username
			String username = (String) ni.getNetworkItem();
			//get id of user
			Integer id = nickToIdMap.get(username);
			//get friends
			if(idToFriends.get(id)!=null){
				List<Contact> contactList = idToFriends.get(id);
				response = new NetworkItem(NetworkItemType.GetFriends, contactList);
			}else{
				response = new NetworkItem(NetworkItemType.GetFriends, Collections.<Contact>emptyList());
			}
		}else if(ni.getNetworkItemType().equals(NetworkItem.NetworkItemType.GetContact)){
			//get contact by id of username
			//Contact contact = idToContactMap.get(nickToIdMap.get(((String) ni.getNetworkItem()).toLowerCase()));
			response = new NetworkItem(NetworkItemType.GetContact, idToContactMap.get(nickToIdMap.get(((String) ni.getNetworkItem()).toLowerCase())));
		}else if(ni.getNetworkItemType().equals(NetworkItem.NetworkItemType.MessagePortUpdate)){
			//read id of contact + port number
			//[0] = id of contact
			//[1] = port of listening to messages of contact
			Integer values[] = (Integer []) ni.getNetworkItem();
			Integer id = values[0];
			Integer newPort = values[1];
			//update port
			if(id!=null && newPort!=null){
					System.out.println("updating port " + newPort + " to id " + id);
					idToPort.put(id, newPort);
			}
			//no need to return anything
		}
		
		//return the response item to caller
		return response;
	}
	

}
