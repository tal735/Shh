package network.client.actions;

import java.io.IOException;

import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

/**
 * Terminate connection with server
 * Input: id of user who terminated connection
 * Output: none
 */

	
public class TerminateConnectionAction extends Action {

	public TerminateConnectionAction(Object input, Client client) {
		super(input, client);
	}

	@Override
	public void exectue() {
		try{
			//send terminate connection request
			Operations.sendItem(new NetworkItem(NetworkItemType.TerminateConnection, input), this.client.getSocket());
			//close connections
			this.client.getSocket().close();
			this.client.getMessageSocket().close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
