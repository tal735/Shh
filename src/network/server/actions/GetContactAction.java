package network.server.actions;

import database.DBConnector;
import misc.Constants.NetworkItemType;
import network.NetworkItem;

/**
 * Get specific contact by name
 * Input from user: String - username to add
 */

	
public class GetContactAction extends Action {

	
	public GetContactAction(Object netowrkObject, DBConnector databaseConnector) {
		super(netowrkObject, databaseConnector);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exectue() {
		
		this.setResponse(new NetworkItem(
						NetworkItemType.GetContact, 
						
						databaseConnector.
						getIdToContactMap().
						get(
								databaseConnector
								.getNickToIdMap()
								.get(
										((String) this.networkObject).toLowerCase()
									)
							)
					));
		/*
		this.response = 
				new NetworkItem(
						NetworkItemType.GetContact, 
						
						databaseConnector.
						getIdToContactMap().
						get(
								databaseConnector
								.getNickToIdMap()
								.get(
										((String) this.networkObject).toLowerCase()
										)
							)
					);
	}
	*/
	}
}
