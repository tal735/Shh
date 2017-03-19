package database;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import security.CryptHelper;
import types.Contact;
import types.Message;

public class DBConnector {

	public DBConnector(){
	}

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
	
	public void initTestValues(){
		addUserToDb("tal");
		addUserToDb("yamit");
		addUserToDb("tafat");
	}
	
	private void addUserToDb(String nickname){
		System.out.println("Adding user " + nickname + " to the database..");
		String lower_nickname = nickname.toLowerCase();
		uniqueId++;
		nickToIdMap.put(nickname, uniqueId);
		idToContactMap.put(uniqueId, new Contact(uniqueId, lower_nickname));
		nickToPass.put(nickname, new CryptHelper().hash_string("1234".toCharArray()));
		idToFriends.put(uniqueId, new ArrayList<Contact>());
	}
}
