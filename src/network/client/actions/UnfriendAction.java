package network.client.actions;

import main.MainContacts;
import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

/**
 * Default action (when no correct action is found). Just send Null to the server
 * Input: 	1. int - id of myself
 * 			2. int - id of friend to remove
 * Output: none
 */

	
public class UnfriendAction extends Action {

	public UnfriendAction(Object input, Client client) {
		super(input, client);
	}

	@Override
	public void exectue() {
		Boolean removalResult = false;
		
		//get all contacts of this username
		NetworkItem ni = new NetworkItem(NetworkItemType.Unfriend, new Integer[] {MainContacts.getCurrentUser().getId(), (Integer) input});
		//request removal from server
		Operations.sendItem(ni, this.client.getSocket());
		//get response from server
		try{
			NetworkItem response = Operations.recieveItem(this.client.getSocket());
			removalResult = (Boolean)(response.getNetworkItem());
		}catch(Exception e){
			removalResult = false;
		}
		
		//send result to user
		this.setOutput(removalResult);
	}
}
