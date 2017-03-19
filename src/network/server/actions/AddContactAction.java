package network.server.actions;

import database.DBConnector;
import types.Contact;
import misc.Constants.NetworkItemType;
import network.NetworkItem;

/**
 * Find and add contact (by nickname) to a user's contact list 
 * Input: String[]:
 * 					[0] = source Username
 * 					[1] = contact Nickname
 * Output: Contact instance of friend (or null if contact does not exist)
 */

public class AddContactAction  extends Action{

	public AddContactAction(Object netowrkObject, DBConnector databaseConnector) {
		super(netowrkObject, databaseConnector);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exectue() {
			//[0] = myUsername
			//[1] = contactNickname
			String values[] = (String []) networkObject;
			String curentUsername = values[0].toLowerCase();
			String contactToAdd = values[1].toLowerCase();
			
			//check if username exists
			Integer idOfFriend = databaseConnector.getNickToIdMap().get(contactToAdd);
			Contact friend = databaseConnector.getIdToContactMap().get(idOfFriend);
			if(friend!=null){
				//check if user is not already friend
				if(!databaseConnector.getIdToFriends().get(databaseConnector.getNickToIdMap().get(curentUsername)).contains(friend)){ //this is already checked in client. but just in case
					//add friend
					boolean added = databaseConnector.getIdToFriends().get(databaseConnector.getNickToIdMap().get(curentUsername)).add(friend);
					System.out.println("added " + friend.toString() + " to " + curentUsername + "? " + added);
					
				}
			}
			
			//send contact back		
			this.setResponse(new NetworkItem(NetworkItemType.AddContact, friend));
			//response = new NetworkItem(NetworkItemType.AddContact, friend);
	}

}
