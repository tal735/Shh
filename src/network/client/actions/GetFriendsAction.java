package network.client.actions;

import java.util.List;

import types.Contact;
import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

/**
 * Get all friends of contact by name
 * Input from user: String - username
 * Output: desired friends list
 */

	
public class GetFriendsAction extends Action {

	public GetFriendsAction(Object input, Client client) {
		super(input, client);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void exectue() {
		List<Contact> contacts = null;
		//get all contacts of this username
		NetworkItem ni = new NetworkItem(NetworkItemType.GetFriends, this.input);
		Operations.sendItem(ni, this.client.getSocket());
		// get contacts
		NetworkItem response = Operations.recieveItem(this.client.getSocket());
		if(response.getNetworkItemType().equals(NetworkItemType.GetFriends)){
			contacts = (List<Contact>) response.getNetworkItem();
		}
		
		this.setOutput(contacts);
	}

	

}
