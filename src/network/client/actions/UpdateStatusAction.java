package network.client.actions;

import main.MainContacts;
import misc.Constants.NetworkItemType;
import network.Client;
import network.NetworkItem;
import network.Operations;

public class UpdateStatusAction extends Action {

	public UpdateStatusAction(Object input, Client client) {
		super(input, client);
	}

	@Override
	public void exectue() {
		//get all contacts of this username
		NetworkItem ni = new NetworkItem(NetworkItemType.UpdateStatus, new Object[] {MainContacts.getCurrentUser().getId(), input});
		//request removal from server
		Operations.sendItem(ni, this.client.getSocket());
	}
}
