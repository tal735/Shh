package network.client.actions;

import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

public class GetStatusAction extends Action {

	/**
	 * Get Availability of contact list
	 * Input from user: Integer[] - ids of friends
	 * Output: Boolean[] - true/false for each id
	 */

	
	public GetStatusAction(Object input, Client client) {
		super(input, client);
	}

	@Override
	public void exectue() {
		NetworkItem ni = new NetworkItem(NetworkItemType.GetStatus, this.input);
		//send user ids
		Operations.sendItem(ni, this.client.getSocket());
		//get availability
		NetworkItem response = Operations.recieveItem(this.client.getSocket());
		//return availability
		Boolean[] results = (Boolean[]) response.getNetworkItem();
		
		this.setOutput(results);

	}

}
