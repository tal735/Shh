package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import network.server.actions.Action;
import network.server.actions.ActionFactory;
import security.CryptHelper;
import types.Contact;
import types.Message;
import misc.Constants;

public class Server{
	private ServerSocket listener=null;
	
	private HashMap<String, Integer> nickToIdMap = new HashMap<String, Integer>();					// NICKNAME --> ID
	
	/**
	 * Getters!
	 * @return
	 */

	public HashMap<String, Integer> getNickToIdMap() {
		return nickToIdMap;
	}

	public HashMap<Integer, Contact> getIdToContactMap() {
		return idToContactMap;
	}

	public HashMap<String, byte[]> getNickToPass() {
		return nickToPass;
	}

	public HashMap<Integer, List<Contact>> getIdToFriends() {
		return idToFriends;
	}


	public HashMap<Integer, Integer> getIdToPort() {
		return idToPort;
	}
	
	public HashMap<Integer, Socket> getIdToSocket() {
		return idToSocket;
	}

	public HashMap<Integer, List<Message>> getIdToUnsentMessages() {
		return idToUnsentMessages;
	}

	/**
	 * End of Getters/Setters
	 */
	
	
	private HashMap<Integer, Contact> idToContactMap = new HashMap<Integer, Contact>();				// ID --> Contact
	private HashMap<String, byte[]> nickToPass = new HashMap<String, byte[]>();						// Nick --> Pass
	private HashMap<Integer, List<Contact>> idToFriends = new HashMap<Integer, List<Contact>>();	// ID --> Contact List (friends)							
	private HashMap<Integer, Integer> idToPort = new HashMap<Integer, Integer>();					//ID-->LISTENING PORT FOR MESSAGES
	private HashMap<Integer, Socket> idToSocket = new HashMap<Integer, Socket>();					//ID-->SOCKET
	private HashMap<Integer, List<Message>> idToUnsentMessages = new HashMap<Integer, List<Message>>();	// ID --> Contact List (friends)
	
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
		idToContactMap.put(uniqueId, new Contact(uniqueId, lower_nickname));
		nickToPass.put(nickname, new CryptHelper().hash_string("1234".toCharArray()));
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
				NetworkItem ni = Operations.recieveItem(socket);
				//handle item
				response = handleItem(ni, socket);
				//send response
				Operations.sendItem(response, socket);
			}
			System.out.println("Terminated connection with " + socket.toString());
		}
	}

	//handle requests from user
	private ActionFactory actionFactory = new ActionFactory();
	
	private NetworkItem handleItem(NetworkItem ni, Socket socket) {
		Action action = actionFactory.getAction(ni, this, socket);
		action.exectue();
		return action.getGeneratedResponse();
	}



}
