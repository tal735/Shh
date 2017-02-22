package network.server.actions;

import misc.Constants.NetworkItemType;
import network.NetworkItem;
import network.Server;

public class NullAction extends Action{

	public NullAction(Object netowrkObject, Server server) {
		super(netowrkObject, server);
	}

	@Override
	public void exectue() {
		response = new NetworkItem(NetworkItemType.NullItem, null);		
	}
	
}
