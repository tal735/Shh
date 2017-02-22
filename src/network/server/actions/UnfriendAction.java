package network.server.actions;

import types.Contact;
import misc.Constants.NetworkItemType;
import network.NetworkItem;
import network.Server;



/**
 * Remove contact from friend list
 * Input from user: 1. int - id of myself
 * 					2. int - id of friend to remove
 * Output:			boolean - success/fail
 */


public class UnfriendAction extends Action{

	public UnfriendAction(Object networkObject, Server server) {
		super(networkObject, server);
	}

	@Override
	public void exectue() {
		//get input
		Integer[] input = (Integer []) this.networkObject;
		int myId = input[0];
		int friendToRemoveId = input[1];
		boolean removed = false;
		
		for(Contact friend : this.server.getIdToFriends().get(myId)){
			if(friend.getId()==friendToRemoveId){
				//this.server.getIdToFriends().get(myId).remove(index);
				removed = this.server.getIdToFriends().get(myId).remove(friend);
				System.out.println("Removed " + friend.getUsername() + " from " + this.server.getIdToContactMap().get(myId).getUsername() + "? " + removed);
				break;
			}
		}
		
		//set response to user
		response =  new NetworkItem(NetworkItemType.Unfriend, removed);
	}
}
