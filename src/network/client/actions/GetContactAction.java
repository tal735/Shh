package network.client.actions;

import types.Contact;
import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

/**
 * Get specific contact by name
 * Input from user: String - username to add
 * Output: desired contact instance
 */

	
public class GetContactAction extends Action {

	public GetContactAction(Object input, Client client) {
		super(input, client);
	}

	@Override
	public void exectue() {
		Contact contact = null;
		String contactNickname = (String) input;
		//send request to get contact
		NetworkItem ni = new NetworkItem(NetworkItemType.GetContact, contactNickname);
		Operations.sendItem(ni, this.client.getSocket());
		//get contact
		NetworkItem response = Operations.recieveItem(this.client.getSocket());
		if(response.getNetworkItemType().equals(NetworkItemType.GetContact)){
			contact = (Contact) response.getNetworkItem();
		}
		this.setOutput(contact);
	}

}
