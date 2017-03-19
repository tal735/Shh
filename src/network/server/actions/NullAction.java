package network.server.actions;

import database.DBConnector;
import misc.Constants.NetworkItemType;
import network.NetworkItem;

public class NullAction extends Action{

	public NullAction(Object netowrkObject, DBConnector databaseConnector) {
		super(netowrkObject, databaseConnector);
	}

	@Override
	public void exectue() {
		setResponse(new NetworkItem(NetworkItemType.NullItem, null));
		//response = new NetworkItem(NetworkItemType.NullItem, null);		
	}
	
}
