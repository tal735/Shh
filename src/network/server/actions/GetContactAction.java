package network.server.actions;

import misc.Constants.NetworkItemType;
import network.NetworkItem;
import network.Server;




/**
 * Get specific contact by name
 * Input from user: String - username to add
 */

	
public class GetContactAction extends Action {

	
	public GetContactAction(Object netowrkObject, Server server) {
		super(netowrkObject, server);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exectue() {
		this.response = 
				new NetworkItem(
						NetworkItemType.GetContact, 
						
						server
						.getIdToContactMap()
						.get(
								server
								.getNickToIdMap()
								.get(
										((String) this.networkObject).toLowerCase()
										)
							)
					);
	}

}
