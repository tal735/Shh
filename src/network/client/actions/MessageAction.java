package network.client.actions;

import types.Message;
import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

/**
 * Send message to target user
 * Input: Message instace - M
 * Output: Boolean - sending was successful
 */

	
public class MessageAction extends Action {

	public MessageAction(Object input, Client client) {
		super(input, client);
	}

	@Override
	public void exectue() {
		//get message input from user
		Message M = (Message) this.input;
		//indicator of transmission success
		Boolean result = false;
		//prepare network item of Message type
		NetworkItem netItem = new NetworkItem(NetworkItemType.Message, M);
		//send message to server
		Operations.sendItem(netItem, this.client.getSocket());
		//get reply of fail/success
		try{
			NetworkItem srcMessage = Operations.recieveItem(this.client.getSocket());
			if(srcMessage.getNetworkItemType().equals(NetworkItemType.Message)){
				result = ((Message) srcMessage.getNetworkItem()).getSendStatus();
			}
		}
		finally{
			this.setOutput(result);
		}
	}

	

}
