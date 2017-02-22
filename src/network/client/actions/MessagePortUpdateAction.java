package network.client.actions;

import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

/**
 * Update listening port at server for incoming messages 
 * Input: Int (port number)
 * Output: none
 */

	
public class MessagePortUpdateAction extends Action {

	public MessagePortUpdateAction(Object input, Client client) {
		super(input, client);
	}

	@Override
	public void exectue() {
		//update server with new port
		Integer values[] = {(Integer) this.input, this.client.getMessageSocket().getLocalPort()};
		Operations.sendItem(new NetworkItem(NetworkItemType.MessagePortUpdate, values), this.client.getSocket());
	}
}
