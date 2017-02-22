package network.client.actions;

import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

/**
 * 
 * @author talsim
 *
 *GET queued messages sent to you while you were offline
 *
 *input: int - id of myself
 *output: none (all messages will be sent to you via messagesocket)
 */

public class GetQueuedMessagesAction extends Action {

	public GetQueuedMessagesAction(Object input, Client client) {
		super(input, client);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exectue() {
		//send request to get all unrecieved messages from server
		NetworkItem ni = new NetworkItem(NetworkItemType.GetQueuedMessages, this.input);
		Operations.sendItem(ni, this.client.getSocket());
		
	}

}
