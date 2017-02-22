package network.client.actions;

import types.Contact;
import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

/**
 * Add contact by name to my friends list
 * Input from user: String[] - [0]=myUsername [1]=contactNickname
 * Output: desired contact instance
 */

	
public class AddContactAction extends Action {

	public AddContactAction(Object input, Client client) {
		super(input, client);
	}

	@Override
	public void exectue() {
		Contact c = null;
		//send request to add contact from server
		Operations.sendItem(new NetworkItem(NetworkItemType.AddContact, this.input), this.client.getSocket());
		//get response from server (contact if OK, null if not ok)
		NetworkItem networkItem = (NetworkItem) Operations.recieveItem(this.client.getSocket());
		if(networkItem.getNetworkItemType().equals(NetworkItemType.AddContact)){
			if(networkItem.getNetworkItem() != null){
				c = (Contact) networkItem.getNetworkItem();
			}
		}
		
		this.setOutput(c);
	}

	

}
