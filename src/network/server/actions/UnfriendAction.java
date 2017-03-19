package network.server.actions;

import database.DBConnector;
import types.Contact;
import misc.Constants.NetworkItemType;
import network.NetworkItem;



/**
 * Remove contact from friend list
 * Input from user: 1. int - id of myself
 * 					2. int - id of friend to remove
 * Output:			boolean - success/fail
 */


public class UnfriendAction extends Action{

	public UnfriendAction(Object networkObject, DBConnector databaseConnector) {
		super(networkObject, databaseConnector);
	}

	@Override
	public void exectue() {
		//get input
		Integer[] input = (Integer []) this.networkObject;
		int myId = input[0];
		int friendToRemoveId = input[1];
		boolean removed = false;
		
		for(Contact friend : this.databaseConnector.getIdToFriends().get(myId)){
			if(friend.getId()==friendToRemoveId){
				//this.server.getIdToFriends().get(myId).remove(index);
				removed = this.databaseConnector.getIdToFriends().get(myId).remove(friend);
				System.out.println("Removed " + friend.getUsername() + " from " + this.databaseConnector.getIdToContactMap().get(myId).getUsername() + "? " + removed);
				break;
			}
		}
		
		//set response to user
		setResponse(new NetworkItem(NetworkItemType.Unfriend, removed));
		//response =  new NetworkItem(NetworkItemType.Unfriend, removed);
	}
}
