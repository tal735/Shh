package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
	private HashMap<Integer, Contact> idToContactMap = new HashMap<Integer, Contact>();				// ID --> User
	private HashMap<String, byte[]> nickToPass = new HashMap<String, byte[]>();						// Nick --> Pass
	private HashMap<Integer, List<Contact>> idToFriends = new HashMap<Integer, List<Contact>>();	// ID --> Contact List (friends)
	private HashMap<Integer, HashMap<Integer, List<Message>>> idToidMessages = 						//ID --> <ID-->List<Messages>>
			new HashMap<Integer, HashMap<Integer, List<Message>>>(); 								
	
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
		//names in lower
		String tal = "Tal";
		String Yamit = "Yamit";
		
		tal = tal.toLowerCase();
		Yamit = Yamit.toLowerCase();
		
		// init test values
		nickToIdMap.put(tal, 0);
		nickToIdMap.put(Yamit, 1);
		
		Contact talUser = new Contact(0,tal,null);
		Contact yamitUser = new Contact(1,Yamit,null);
		
		idToContactMap.put(0, talUser);
		idToContactMap.put(1, yamitUser);
		
		nickToPass.put(tal, cryptHelper.hash_string("123123".toCharArray()));
		nickToPass.put(Yamit, cryptHelper.hash_string("1234".toCharArray()));
		
		List<Contact> tals_friends = new ArrayList<Contact>();
		//tals_friends.add(yamitUser);
		List<Contact> yamits_friends = new ArrayList<Contact>();
		//yamits_friends.add(talUser);
		
		idToFriends.put(0, tals_friends);
		idToFriends.put(1, yamits_friends);
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
			/*
			System.out.println("socket.getLocalSocketAddress().toString()="+socket.getLocalSocketAddress().toString());
			System.out.println("socket.getLocalAddress().getHostName()="+socket.getLocalAddress().getHostName());
			System.out.println("socket.getLocalAddress().getHostAddress()="+socket.getLocalAddress().getHostAddress());
			System.out.println("socket.getLocalSocketAddress().toString()="+socket.getLocalSocketAddress().toString());
			*/			
			
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
			if(idOfFriend!=null){
				//check if user is not already friend
				Contact friend = idToContactMap.get(idOfFriend);
				if(!idToFriends.get(nickToIdMap.get(curentUsername)).contains(friend)){ //this is already checked in client. but just in case
					//add friend
					idToFriends.get(nickToIdMap.get(curentUsername)).add(friend);
					result = true;
					System.out.println("added " + idToFriends.get(nickToIdMap.get(curentUsername)).get(0).getUsername() + " to " + curentUsername);
					
				}
			}
			
			//send true/false		
			response = new NetworkItem(NetworkItem.NetworkItemType.AddContact, result);
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
			//get message
			Message m = (Message) ni.getNetworkItem();
			String from = m.getFrom().toLowerCase(), to = m.getTo().toLowerCase();
			//lookup ids of to/from
			Integer fromId = nickToIdMap.get(from), toId = nickToIdMap.get(to);
			if(toId != null){
				
				//create db of messages for FROM-user if not exists
				if(!idToidMessages.containsKey(fromId)){
					HashMap<Integer, List<Message>> hml = new HashMap<Integer, List<Message>>();
					idToidMessages.put(fromId, hml);
				}
				
				//if user never send message to TO-USER add him
				if(!idToidMessages.get(fromId).containsKey(toId)){
					idToidMessages.get(fromId).put(toId, null);
				}
					
				//should get here only 1st time
				if(idToidMessages.get(fromId).get(toId)==null){
					List<Message> messageList = new ArrayList<Message>();
					idToidMessages.get(fromId).put(toId, messageList);
				}

				//send message to user
				//get socket of dest user
				Socket toSocket = idToContactMap.get(toId).getSocket();
				if(toSocket!=null){
					//send message
					NetworkItem messageNI = new NetworkItem(NetworkItemType.Message, m);
					Socket toMessageSocket = null;
					try {
						toMessageSocket = new Socket(toSocket.getLocalAddress().getHostAddress(), Constants.APP_CLIENT_LISTEN_PORT_MESSAGES);
					} catch (Exception e) {}
					
					serverOperations.sendItem(messageNI, toMessageSocket);
					//get reply from user
					NetworkItem toUserResponse = serverOperations.recieveItem(toSocket);
					isSent = ((Message)toUserResponse.getNetworkItem()).getSendStatus();
				}
				//add message to messages map
				m.setSendStatus(isSent);
				idToidMessages.get(fromId).get(toId).add(m);
				//if not sent, add to queue of send later
				if(!m.getSendStatus()){
					//....
				}
			}
			
			//send back if success or fail
			response = new NetworkItem(NetworkItem.NetworkItemType.Message, m);
		}else if(ni.getNetworkItemType().equals(NetworkItem.NetworkItemType.GetContacts)){
			//get input username
			String username = (String) ni.getNetworkItem();
			//get id of user
			Integer id = nickToIdMap.get(username);
			//get friends
			if(idToFriends.get(id)!=null){
				List<Contact> contactList = idToFriends.get(id);
				response = new NetworkItem(NetworkItemType.GetContacts, contactList);
			}else{
				response = new NetworkItem(NetworkItemType.GetContacts, Collections.<Contact>emptyList());
			}
		}
		
		//return the response item to caller
		return response;
	}
	

}
