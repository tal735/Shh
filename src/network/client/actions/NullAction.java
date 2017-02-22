package network.client.actions;

import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

/**
 * Default action (when no correct action is found). Just send Null to the server
 * Input: none
 * Output: none
 */

	
public class NullAction extends Action {

	public NullAction(Object input, Client client) {
		super(input, client);
	}

	@Override
	public void exectue() {
		NetworkItem netItem = new NetworkItem(NetworkItemType.NullItem, null);
		Operations.sendItem(netItem, this.client.getSocket());
	}
}
